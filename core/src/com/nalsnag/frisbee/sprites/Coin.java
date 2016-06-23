package com.nalsnag.frisbee.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.nalsnag.frisbee.Collectible;
import com.nalsnag.frisbee.tools.GameVars;

public class Coin extends Collectible {
    public Coin(World world, Texture texture, float x, float y) {
        super(world, texture, x, y, 9, GameVars.COIN_BIT, GameVars.FRISBEE_BIT);

        body.setTransform((x + texture.getWidth() / 2) / GameVars.PPM, (y + texture.getHeight() / 2) / GameVars.PPM, 0);
    }
}
