package com.nalsnag.frisbee.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nalsnag.frisbee.Game;
import com.nalsnag.frisbee.Quest;
import com.nalsnag.frisbee.tools.GameData;
import com.nalsnag.frisbee.tools.GameVars;

public class MainMenuScreen implements Screen {
    private Game game;

    private OrthographicCamera cam;
    private Viewport viewport;

    private BitmapFont font;
    private Stage stage;
    private Label mainMenuLabel, coinLabel;

    private Quest[] quests;

    public MainMenuScreen(Game game) {
        this.game = game;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, GameVars.V_WIDTH, GameVars.V_HEIGHT);
        viewport = new FitViewport(GameVars.V_WIDTH, GameVars.V_HEIGHT, cam);
        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        cam.update();

        quests = new Quest[3];
        quests[0] = new Quest();
        quests[1] = new Quest();
        quests[2] = new Quest();

        // STAGE STUFF
        font = new BitmapFont();
        stage = new Stage(viewport, game.batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        mainMenuLabel = new Label("Main Menu", new Label.LabelStyle(font, Color.WHITE));
        coinLabel = new Label("X", new Label.LabelStyle(font, Color.YELLOW));

        table.add(coinLabel).left().padTop(5).padLeft(5);
        table.row();
        table.add(mainMenuLabel).expandX().padTop(10).padBottom(50);
        table.row();
        table.add(quests[0]).expandX().padBottom(30);
        table.row();
        table.add(quests[1]).expandX().padBottom(30);
        table.row();
        table.add(quests[2]).expandX().padBottom(30);

        stage.addActor(table);
//        table.setDebug(true);
    }

    private void handleInput() {
        for(Quest quest : quests) {
            if(!quest.isReady()) {
                return;
            }
        }

        if(Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen(game));
        }
    }

    private void update(float dt) {
        handleInput();

        for(Quest quest : quests) {
            quest.update(dt);
        }
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
        for(int i = 0; i < 3; i++) {
            quests[i].checkCriteria();
        }

        coinLabel.setText("" + GameData.getCoins());

        GameData.reset();
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

    }
}
