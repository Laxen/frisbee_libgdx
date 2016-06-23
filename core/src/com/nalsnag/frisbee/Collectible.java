package com.nalsnag.frisbee;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.nalsnag.frisbee.tools.GameVars;

public abstract class Collectible {
    private int radius;

    protected Body body;
    private World world;
    private CircleShape cs;

    private float x, y;
    private Texture texture;

    private short categoryBits, maskBits;

    private boolean setToDestroy = false;
    private boolean destroyed = false;

    public Collectible(World world, Texture texture, float x, float y, int radius, short categoryBits, short maskBits) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.world = world;
        this.categoryBits = categoryBits;
        this.maskBits = maskBits;

        createBody();
    }

    public void update(float dt) {
        if(setToDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch) {
        if(!destroyed) {
            batch.draw(texture, x, y);
        }
    }

    private void createBody() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        cs = new CircleShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        cs.setRadius(radius / GameVars.PPM);
        fdef.shape = cs;
        fdef.filter.categoryBits = categoryBits;
        fdef.filter.maskBits = maskBits;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }

    public void destroy() {
        setToDestroy = true;
    }

    public void dispose() {
        texture.dispose();
        cs.dispose();
    }
}
