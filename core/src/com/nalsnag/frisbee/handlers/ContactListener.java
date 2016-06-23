package com.nalsnag.frisbee.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.nalsnag.frisbee.Collectible;
import com.nalsnag.frisbee.tools.GameData;
import com.nalsnag.frisbee.tools.GameVars;
import com.nalsnag.frisbee.sprites.Frisbee;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        int cdef = fa.getFilterData().categoryBits | fb.getFilterData().categoryBits;

        switch(cdef) {
            case GameVars.FRISBEE_BIT | GameVars.TREE_BIT:
                if(fa.getFilterData().categoryBits == GameVars.FRISBEE_BIT)
                    ((Frisbee) fa.getUserData()).crashTree();
                else
                    ((Frisbee) fb.getUserData()).crashTree();
                break;
            case GameVars.FRISBEE_BIT | GameVars.BIRD_BIT:
                if(fa.getFilterData().categoryBits == GameVars.FRISBEE_BIT)
                    ((Frisbee) fa.getUserData()).crashBird();
                else
                    ((Frisbee) fb.getUserData()).crashBird();
                break;
            case GameVars.FRISBEE_BIT | GameVars.SPIKES_BIT:
                if(fa.getFilterData().categoryBits == GameVars.FRISBEE_BIT) {
                    ((Frisbee) fa.getUserData()).spikes();
                    ((Collectible) fb.getUserData()).destroy();
                } else {
                    ((Frisbee) fb.getUserData()).spikes();
                    ((Collectible) fa.getUserData()).destroy();
                }
                break;
            case GameVars.FRISBEE_BIT | GameVars.COIN_BIT:
                GameData.addCoin();

                if(fa.getFilterData().categoryBits == GameVars.COIN_BIT)
                    ((Collectible) fa.getUserData()).destroy();
                else
                    ((Collectible) fb.getUserData()).destroy();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
