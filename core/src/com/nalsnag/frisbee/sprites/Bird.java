package com.nalsnag.frisbee.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.nalsnag.frisbee.screens.PlayScreen;
import com.nalsnag.frisbee.tools.GameVars;

import java.util.Random;

public class Bird extends Sprite {
    private PlayScreen screen;
    private Texture texture;
    private Texture warning;
    private Body body;

    private Random random;

    private float timer = 0;
    private float searchTime = 2.5f;
    private int deadTime;
    private final int MIN_DEAD_TIME = 5;
    private final int MAX_DEAD_TIME = 30;
//    private final int MIN_DEAD_TIME = 1;
//    private final int MAX_DEAD_TIME = 5;
    private final float SPEED = 1.5f;

    private boolean dead = false;

    private CircleShape cs;

    public Bird(PlayScreen screen, World world, float x, float y) {
        texture = new Texture("bird.png");
        warning = new Texture("warning.png");
        super.setTexture(texture);

        this.screen = screen;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        cs = new CircleShape();

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set((x + texture.getWidth() / 2) / GameVars.PPM, (y + texture.getHeight() / 2) / GameVars.PPM);
        body = world.createBody(bdef);

        cs.setRadius(9 / GameVars.PPM);
        fdef.shape = cs;
        fdef.filter.categoryBits = GameVars.BIRD_BIT;
        fdef.filter.maskBits = GameVars.FRISBEE_BIT;
        fdef.isSensor = true;
        body.createFixture(fdef);

        random = new Random();

        kill();
    }

    public void update(float dt) {
        timer += dt;

        if(dead) {
            if(timer < deadTime) {
                return;
            } else {
                timer = 0;
                dead = false;
                body.setTransform(screen.frisbee.getX(), (screen.cam.position.y + screen.cam.viewportHeight / 2 + texture.getHeight()) / GameVars.PPM, body.getAngle());
            }
        }

        if(timer > searchTime) {
            body.setLinearVelocity(0, -SPEED);

            if(body.getPosition().y * GameVars.PPM < screen.cam.position.y - screen.cam.viewportHeight / 2 - texture.getHeight()) {
                kill();
            }
        } else {
            body.setTransform(body.getPosition().x, (screen.cam.position.y + screen.cam.viewportHeight / 2 + texture.getHeight()) / GameVars.PPM, body.getAngle());

            float dx = screen.frisbee.getX() - body.getPosition().x;

            if(dx > .05) {
                body.setLinearVelocity(.3f, 0);
            } else if(dx < -.05) {
                body.setLinearVelocity(-.3f, 0);
            } else {
                body.setLinearVelocity(0, 0);
            }
        }
    }

    @Override
    public void draw(Batch sb) {
        if(dead)
            return;

        sb.draw(texture, body.getPosition().x * GameVars.PPM - texture.getWidth() / 2, body.getPosition().y * GameVars.PPM - texture.getHeight() / 2);

        if(timer < searchTime)
            sb.draw(warning, body.getPosition().x * GameVars.PPM - warning.getWidth() / 2, body.getPosition().y * GameVars.PPM - 50);
    }

    private void kill() {
        dead = true;
        timer = 0;
        deadTime = random.nextInt(MAX_DEAD_TIME) + MIN_DEAD_TIME;
    }

    public void dispose() {
        texture.dispose();
        warning.dispose();
        cs.dispose();
    }
}
