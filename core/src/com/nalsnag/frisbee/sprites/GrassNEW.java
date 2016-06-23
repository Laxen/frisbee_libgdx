package com.nalsnag.frisbee.sprites;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class GrassNEW extends Tile {
    private Random random;
    private Texture grass1, grass2, grass3;

    public GrassNEW(Texture grass1, Texture grass2, Texture grass3) {
        random = new Random();
        this.grass1 = grass1;
        this.grass2 = grass2;
        this.grass3 = grass3;
    }

    public void init(float x, float y) {
        float r = random.nextFloat();

        if(r > 1f - 1/3f)
            super.setTexture(grass1);
        else if(r > 1f - 2/3f)
            super.setTexture(grass2);
        else
            super.setTexture(grass3);

        super.setPosition(x, y);
    }
}
