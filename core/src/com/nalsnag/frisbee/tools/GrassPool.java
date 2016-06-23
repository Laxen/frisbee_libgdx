package com.nalsnag.frisbee.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;
import com.nalsnag.frisbee.sprites.GrassNEW;

public class GrassPool extends Pool<GrassNEW> {
    private Texture grass1, grass2, grass3;

    public GrassPool(Texture grass1, Texture grass2, Texture grass3) {
        this.grass1 = grass1;
        this.grass2 = grass2;
        this.grass3 = grass3;
    }

    @Override
    protected GrassNEW newObject() {
        return new GrassNEW(grass1, grass2, grass3);
    }
}
