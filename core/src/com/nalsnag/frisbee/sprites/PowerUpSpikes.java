package com.nalsnag.frisbee.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.nalsnag.frisbee.Collectible;
import com.nalsnag.frisbee.tools.GameVars;

public class PowerUpSpikes extends Collectible {
    public PowerUpSpikes(World world, Texture texture, float x, float y) {
        super(world, texture, x, y, 7, GameVars.SPIKES_BIT, GameVars.FRISBEE_BIT);

//        texture = new Texture("spikes.png");
        body.setTransform((x + texture.getWidth() / 2) / GameVars.PPM, (y + texture.getHeight() / 2) / GameVars.PPM, 0);
    }
}
