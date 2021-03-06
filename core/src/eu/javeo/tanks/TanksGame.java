package eu.javeo.tanks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TanksGame extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    private SpriteBatch batch;
    private Stage stage;
    private Missile missile;

    private Texture explosionTexture;
    private Texture tankTexture;
    private Queue<Tank> tanks = new ConcurrentLinkedQueue<Tank>();
    private Touchpad touchpad;
    private TiledMap tiledMap;

    private Texture missileTexture;
    private Texture bonusTexture;

    private OrthographicCamera camera;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;
    private Bonus bonus;

    @Override
	public void create () {
		batch = new SpriteBatch();
        loadTextures();
		tanks.add(new Tank(tankTexture, batch, Tank.ControlType.HUMAN, createExplosionAnimation(), tiledMap));
        tanks.add(new Tank(tankTexture, batch, Tank.ControlType.COMPUTER, createExplosionAnimation(), tiledMap));

		touchpad = createTouchpad();
        fireMissile();
        StretchViewport viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        stage = new Stage(viewport, batch);
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        tiledMap = new TmxMapLoader().load("levels/level.tmx");
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);

        bonus = new Bonus(bonusTexture, batch);
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        for (Tank tank : tanks) {
            tank.update(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
        }

		batch.begin();
        missile.draw();

        for (Tank tank : tanks) {
            tank.draw();
        }

        bonus.draw();
		batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}

    private void fireMissile() {
        missile = new Missile(missileTexture, new Vector2(50f,50f), new Vector2(0f,1f), batch, tiledMap);
    }

    private void loadTextures() {
        tankTexture = new Texture("tank.png");
        missileTexture = new Texture("missile.png");
        explosionTexture = new Texture("explosion.png");
        bonusTexture = new Texture("bonus.png");
    }

    private Touchpad createTouchpad() {
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("analog_stick_bg.png"));
        touchpadSkin.add("touchKnob", new Texture("analog_stick_knob.png"));
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchpadSkin.getDrawable("touchBackground");
        touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 120, 120);
        return touchpad;
    }

    private Animation createExplosionAnimation() {
        TextureRegion[][] explosionFrames = TextureRegion.split(explosionTexture, 134, 134);
        return new Animation(0.1f, explosionFrames[0]);
    }
}
