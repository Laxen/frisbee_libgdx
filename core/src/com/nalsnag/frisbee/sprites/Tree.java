package com.nalsnag.frisbee.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.nalsnag.frisbee.tools.GameVars;

public class Tree extends Tile {
    private Body body;
    private CircleShape cs;
    private ChainShape chain;

    public Tree(Texture texture, World world, float x, float y) {
        super(texture, x, y);

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        cs = new CircleShape();
        chain = new ChainShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((x + texture.getWidth() / 2) / GameVars.PPM, (y + texture.getHeight() / 2) / GameVars.PPM);
        body = world.createBody(bdef);

        cs.setRadius(14 / GameVars.PPM);
        cs.setPosition(new Vector2(0, -5 / GameVars.PPM));
        fdef.shape = cs;
        fdef.filter.categoryBits = GameVars.TREE_BIT;
        fdef.filter.maskBits = GameVars.FRISBEE_BIT;
        fdef.isSensor = true;
        body.createFixture(fdef);

        Vector2[] v = new Vector2[3];
        v[0] = new Vector2(-13 / GameVars.PPM, 3 / GameVars.PPM);
        v[1] = new Vector2(0, 18 / GameVars.PPM);
        v[2] = new Vector2(13 / GameVars.PPM, 3 / GameVars.PPM);
        chain.createChain(v);
        fdef.shape = chain;
        body.createFixture(fdef);
    }
    @Override
    public float getX() {
        return body.getPosition().x * GameVars.PPM - getTexture().getWidth() / 2;
    }

    @Override
    public float getY() {
        return body.getPosition().y * GameVars.PPM - getTexture().getHeight() / 2;
    }

    @Override
    public void setY(float y) {
        body.setTransform(body.getPosition().x, (y + getTexture().getHeight() / 2) / GameVars.PPM, body.getAngle());
    }

    @Override
    public void dispose() {
        super.dispose();

        cs.dispose();
        chain.dispose();
    }
}
