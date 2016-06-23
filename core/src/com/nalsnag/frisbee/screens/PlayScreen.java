package com.nalsnag.frisbee.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nalsnag.frisbee.Game;
import com.nalsnag.frisbee.sprites.Bird;
import com.nalsnag.frisbee.tools.GameData;
import com.nalsnag.frisbee.tools.GameVars;
import com.nalsnag.frisbee.handlers.ContactListener;
import com.nalsnag.frisbee.handlers.Input;
import com.nalsnag.frisbee.sprites.Frisbee;
import com.nalsnag.frisbee.tools.Level;
import com.nalsnag.frisbee.tools.LevelNEW;

public class PlayScreen implements Screen {
    private boolean debug = false;

    private Game game;

    public OrthographicCamera cam;
    private Viewport viewport;

    private OrthographicCamera hudcam;
    private BitmapFont font;

    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;

    public Frisbee frisbee;
    private Array<Bird> birds;

    private LevelNEW level;
    private float distance = 0;

    private boolean gameover = false;

    public PlayScreen(Game game) {
        this.game = game;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, GameVars.V_WIDTH, GameVars.V_HEIGHT);
        viewport = new FitViewport(GameVars.V_WIDTH, GameVars.V_HEIGHT, cam);
        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        cam.update();

        hudcam = new OrthographicCamera(GameVars.V_WIDTH, GameVars.V_HEIGHT);
        hudcam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        hudcam.update();
        font = new BitmapFont();

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListener());
        b2dr = new Box2DDebugRenderer();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, GameVars.V_WIDTH / GameVars.PPM, GameVars.V_HEIGHT / GameVars.PPM);

        frisbee = new Frisbee(this, world);
        birds = new Array<Bird>();

        for(int i = 0; i < 5; i++) {
            Bird bird = new Bird(this, world, frisbee.getX() * GameVars.PPM, 0);
            birds.add(bird);
        }

        setUpLevel();
    }

    private void handleInput() {
        if(Input.isDragging) {
            frisbee.handleInput();
        }
    }

    private void update(float dt) {
        handleInput();

        world.step(dt, 6, 2);

        if(gameover) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        level.update(dt);
        frisbee.update(dt);
        for(int i = 0; i < birds.size; i++) {
            Bird bird = birds.get(i);
            bird.update(dt);
        }

        distance += dt*2;
        GameData.setTotalDistance(distance);
    }

    @Override
    public void render(float delta) {
        update(delta);

        if(gameover)
            return;

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.position.set(cam.position.x, frisbee.getY() * GameVars.PPM + GameVars.V_HEIGHT * 0.2f, 0);
        cam.update();
        b2dcam.position.set(b2dcam.position.x, frisbee.getY() + GameVars.V_HEIGHT * 0.2f / GameVars.PPM, 0);
        b2dcam.update();

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();

        level.render(game.batch);
        frisbee.draw(game.batch);
        for(Bird bird : birds) {
            bird.draw(game.batch);
        }

        game.batch.end();

        if(debug)
            b2dr.render(world, b2dcam.combined);

        game.batch.setProjectionMatrix(hudcam.combined);
        game.batch.begin();

        font.draw(game.batch, "" + (int) GameData.getTotalDistance(), 5, GameVars.V_HEIGHT - 5);

        game.batch.end();
    }

    private void setUpLevel() {
        level = new LevelNEW(world, this);
    }

    public void gameover() {
        gameover = true;
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
        Gdx.app.log("PlayScreen", "Disposing...");
        font.dispose();
        world.dispose();
        frisbee.dispose();

        for(Bird bird : birds) {
            bird.dispose();
        }

        level.dispose();

        Gdx.app.log("PlayScreen", "Disposed!");
    }
}
