package com.nalsnag.frisbee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.nalsnag.frisbee.tools.GameData;
import com.nalsnag.frisbee.tools.GameVars;

import java.util.Random;

public class Quest extends Table {
    private int type;

    private BitmapFont font;
    private Label questLabel;
//    private Label progressLabel;

    private Random random;

    private final float FADE_OUT_TIME = 2f;
    private final float FADE_OUT_RATE = 1f / FADE_OUT_TIME;
    private float timer;
    private boolean fadeOut = false;
    private float alpha = 1f;

    public Quest() {
        random = new Random();

        font = new BitmapFont();
        questLabel = new Label("QUEST", new Label.LabelStyle(font, Color.WHITE));
//        progressLabel = new Label("PROGRESS", new Label.LabelStyle(font, Color.WHITE));

        add(questLabel).expandX();
//        row();
//        add(progressLabel).expandX();

        randomizeQuest();
    }

    public void update(float dt) {
        if(fadeOut) {
            timer += dt;

            alpha = alpha - FADE_OUT_RATE * dt;
            if(alpha > 0) {
                questLabel.setColor(questLabel.getColor().r, questLabel.getColor().g, questLabel.getColor().b, alpha);
            } else {
                questLabel.setColor(questLabel.getColor().r, questLabel.getColor().g, questLabel.getColor().b, 0);
            }

            if(timer > FADE_OUT_TIME) {
                fadeOut = false;
                questLabel.setColor(Color.WHITE);
                alpha = 1f;
                randomizeQuest();
            }
        }
    }

    public void checkCriteria() {
        switch(type) {
            case GameVars.QUEST_TRAVEL_50:
                int dist = (int) GameData.getTotalDistance();
//                progressLabel.setText("" + dist);
                if(dist >= 50) {
                    questLabel.setColor(Color.GREEN);
                    fadeOut = true;
                    GameData.addCoins(50);
                }
                break;
            case GameVars.QUEST_NO_TILT_10:
//                progressLabel.setText("" + (int) GameData.getDistanceWithoutTilt());
                break;
        }
    }

    private void randomizeQuest() {
        if(random.nextFloat() > 0.5) {
            questLabel.setText("Travel 50 meters in one game (50g)");
//            progressLabel.setText("0");
            type = GameVars.QUEST_TRAVEL_50;
        } else {
            questLabel.setText("Don't tilt for 10 meters (100g)");
//            progressLabel.setText("0");
            type = GameVars.QUEST_NO_TILT_10;
        }
    }

    public boolean isReady() {
        return !fadeOut;
    }
}
