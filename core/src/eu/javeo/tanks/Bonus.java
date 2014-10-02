package eu.javeo.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * Created by marcinburczak on 02.10.14.
 */
public class Bonus extends GameObject {

    private final Sprite sprite;
    private SpriteBatch batch;
    private Random random = new Random();
    private float timeCounter;

    public Bonus(Texture texture, SpriteBatch batch) {
        this.sprite = new Sprite(texture);
        this.batch = batch;
    }

    @Override
    public void init() {

    }

    public void draw() {
        timeCounter += Gdx.graphics.getDeltaTime();
        if (timeCounter > 1) {
            sprite.setX(random.nextFloat());
            sprite.setY(random.nextFloat());
            sprite.draw(batch);
        }
    }
}
