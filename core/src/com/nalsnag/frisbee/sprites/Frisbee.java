package com.nalsnag.frisbee.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.nalsnag.frisbee.tools.GameData;
import com.nalsnag.frisbee.tools.GameVars;
import com.nalsnag.frisbee.handlers.Input;
import com.nalsnag.frisbee.screens.PlayScreen;

public class Frisbee extends Sprite {
    private PlayScreen screen;
    private World world;
    private Body body;

    private Texture drawTexture, frisbeeFlat, frisbeeLeft, frisbeeLeftMax, frisbeeRight, frisbeeRightMax;
    private Texture spikesTexture;

    private int powerUp = GameVars.POWERUP_NONE;

    private final float forceMultiplier = 0.01f;

    private final float startingVelocity = 0.5f;
//    private final float startingVelocity = 0.3f;

    private boolean tilt = false;
    private float distanceWithoutTilt = 0;

    private CircleShape cs;

    public Frisbee(PlayScreen screen, World world) {
        super(new TextureRegion(new Texture("frisbeepoly.png")));
        frisbeeFlat = new Texture("frisbeepoly.png");
        frisbeeLeft = new Texture("frisbeeLeft.png");
        frisbeeLeftMax = new Texture("frisbeeLeftMax.png");
        frisbeeRight = new Texture("frisbeeRight.png");
        frisbeeRightMax = new Texture("frisbeeRightMax.png");
        spikesTexture = new Texture("spikes_ns.png");
        drawTexture = frisbeeFlat;

        this.screen = screen;
        this.world = world;

        createFrisbee();
    }

    public void update(float dt) {
        body.setLinearVelocity(body.getLinearVelocity().x, startingVelocity);

        if(body.getPosition().x > GameVars.V_WIDTH / GameVars.PPM) {
            body.setTransform(GameVars.V_WIDTH / GameVars.PPM, body.getPosition().y, 0);
            body.setLinearVelocity(0,body.getLinearVelocity().y);
        } else if(body.getPosition().x < 0) {
            body.setTransform(0, body.getPosition().y, 0);
            body.setLinearVelocity(0,body.getLinearVelocity().y);
        }

        if(!tilt) {
            distanceWithoutTilt += dt;

            if(distanceWithoutTilt > GameData.getDistanceWithoutTilt()) {
                GameData.setDistanceWithoutTilt(distanceWithoutTilt);
            }
        } else {
            distanceWithoutTilt = 0;
            tilt = false;
        }
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(drawTexture,
                body.getPosition().x * GameVars.PPM - getTexture().getWidth() / 2,
                body.getPosition().y * GameVars.PPM - getTexture().getHeight() / 2);

        switch(powerUp) {
            case GameVars.POWERUP_SPIKES:
                batch.draw(spikesTexture,
                        body.getPosition().x * GameVars.PPM - getTexture().getWidth() / 2,
                        body.getPosition().y * GameVars.PPM - getTexture().getHeight() / 2);
                break;
        }

        drawTexture = frisbeeFlat;
    }

    private void createFrisbee() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        cs = new CircleShape();

        bdef.position.set(GameVars.V_WIDTH / 2 / GameVars.PPM, GameVars.V_HEIGHT * 0.2f / GameVars.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearVelocity.set(0, startingVelocity);
        body = world.createBody(bdef);

        cs.setRadius(5 / GameVars.PPM);
        fdef.shape = cs;
        fdef.filter.categoryBits = GameVars.FRISBEE_BIT;
        fdef.filter.maskBits = GameVars.TREE_BIT | GameVars.BIRD_BIT | GameVars.SPIKES_BIT | GameVars.COIN_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    public void handleInput() {
        if(Input.dx < -300) {
            drawTexture = frisbeeLeftMax;
        } else if(Input.dx < -10) {
            drawTexture = frisbeeLeft;
        } else if(Input.dx > 300) {
            drawTexture = frisbeeRightMax;
        } else if(Input.dx > 10) {
            drawTexture = frisbeeRight;
        } else {
            drawTexture = frisbeeFlat;
        }

        body.applyForceToCenter(Input.dx * forceMultiplier, 0, true);

        tilt = true;
    }

    public void crashTree() {
        screen.gameover();
    }

    public void crashBird() {
        if(powerUp == GameVars.POWERUP_SPIKES) {
            powerUp = GameVars.POWERUP_NONE;
        } else {
            screen.gameover();
        }
    }

    public void spikes() {
        powerUp = GameVars.POWERUP_SPIKES;
    }

    @Override
    public float getX() {
        return body.getPosition().x;
    }

    @Override
    public float getY() {
        return body.getPosition().y;
    }

    public void dispose() {
        drawTexture.dispose();
        frisbeeFlat.dispose();
        frisbeeLeft.dispose();
        frisbeeLeftMax.dispose();
        frisbeeRight.dispose();
        frisbeeRightMax.dispose();
        spikesTexture.dispose();

        cs.dispose();
    }
}
