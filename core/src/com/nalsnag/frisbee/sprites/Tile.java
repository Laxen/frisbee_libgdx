package com.nalsnag.frisbee.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Tile extends Sprite {
    public Tile(Texture texture, float x, float y) {
        super.setTexture(texture);
        super.setPosition(x, y);
    }

    public Tile() {

    }

    @Override
    public void draw(Batch batch) {
        batch.draw(getTexture(), getX(), getY());
    }

    public void dispose() {
        super.getTexture().dispose();
    }
}
