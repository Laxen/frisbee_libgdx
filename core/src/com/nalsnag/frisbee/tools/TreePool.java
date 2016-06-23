package com.nalsnag.frisbee.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.nalsnag.frisbee.sprites.TreeNEW;

public class TreePool extends Pool<TreeNEW> {
    private Texture texture;
    private World world;

    public TreePool(World world, Texture texture) {
        this.world = world;
        this.texture = texture;
    }

    @Override
    protected TreeNEW newObject() {
        return new TreeNEW(texture, world);
    }
}
