package com.nalsnag.frisbee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nalsnag.frisbee.handlers.InputProcessor;
import com.nalsnag.frisbee.screens.MainMenuScreen;
import com.nalsnag.frisbee.screens.PlayScreen;

public class Game extends com.badlogic.gdx.Game {
	public SpriteBatch batch;

    public MainMenuScreen mainMenuScreen;
	
	@Override
	public void create () {
        Gdx.input.setInputProcessor(new InputProcessor());

		batch = new SpriteBatch();
        mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
        super.render();
	}
}
