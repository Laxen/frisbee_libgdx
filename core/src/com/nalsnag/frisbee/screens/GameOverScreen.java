package com.nalsnag.frisbee.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nalsnag.frisbee.Game;
import com.nalsnag.frisbee.tools.GameData;
import com.nalsnag.frisbee.tools.GameVars;

public class GameOverScreen implements Screen {
    private Game game;
    private BitmapFont font;

    private Viewport viewport;

    private Stage stage;
    private Label gameoverLabel, distanceLabel, coinLabel;

    public GameOverScreen(Game game) {
        this.game = game;

        font = new BitmapFont();

        viewport = new FitViewport(GameVars.V_WIDTH, GameVars.V_HEIGHT);

        // STAGE STUFF
        stage = new Stage(viewport, game.batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        gameoverLabel = new Label("Game Over", new Label.LabelStyle(font, Color.WHITE));
        distanceLabel = new Label("Distance: " + (int) GameData.getTotalDistance(), new Label.LabelStyle(font, Color.WHITE));
        coinLabel = new Label("Coins: " + GameData.getCoins(), new Label.LabelStyle(font, Color.WHITE));

        table.add(gameoverLabel).expandX().padTop(10);
        table.row();
        table.add(distanceLabel).expandX().padTop(30);
        table.row();
        table.add(coinLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    private void handleInput() {
        if(Gdx.input.justTouched()) {
            game.setScreen(game.mainMenuScreen);
            dispose();
        }
    }

    private void update(float dt) {
        handleInput();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Gdx.app.log("GameOverScreen", "Disposing...");
        font.dispose();
        stage.dispose();
        Gdx.app.log("GameOverScreen", "Disposed!");
    }
}
