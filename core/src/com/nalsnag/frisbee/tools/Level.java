package com.nalsnag.frisbee.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nalsnag.frisbee.Collectible;
import com.nalsnag.frisbee.screens.PlayScreen;
import com.nalsnag.frisbee.sprites.Coin;
import com.nalsnag.frisbee.sprites.Grass;
import com.nalsnag.frisbee.sprites.PowerUpSpikes;
import com.nalsnag.frisbee.sprites.Tile;
import com.nalsnag.frisbee.sprites.Tree;

import java.util.Random;

public class Level {
    private World world;
    private PlayScreen screen;

    private Sprite background1, background2;
    private Array<Array<Tile>> borderTrees;
    private Array<Array<Tile>> tileseses;
    private Texture treeTexture;

    private Array<Array<Tile>> groundTiles;
    private Array<Array<Tile>> treeTiles;

    private Texture grass1, grass2, grass3;
    private Texture coinTexture;
    private Texture spikesTexture;

    private Random random;

    private int rows = 14;

    private Vector2 currentTile;

    private Array<Collectible> collectibles;

    public Level(World world, PlayScreen screen) {
        this.world = world;
        this.screen = screen;

        Texture bgtex = new Texture("background.png");
        background1 = new Sprite(bgtex);
        background2 = new Sprite(bgtex);
        background2.setPosition(0, background1.getY() + background1.getHeight());

        treeTexture = new Texture("tree.png");
        borderTrees = new Array<Array<Tile>>();
        tileseses = new Array<Array<Tile>>();

        groundTiles = new Array<Array<Tile>>();
        treeTiles = new Array<Array<Tile>>();

        grass1 = new Texture("grass.png");
        grass2 = new Texture("grass2.png");
        grass3 = new Texture("grass3.png");

        coinTexture = new Texture("coin.png");
        spikesTexture = new Texture("spikes.png");

        random = new Random();

        collectibles = new Array<Collectible>();

        createTrees();
    }

    public void update(float dt) {
        if(screen.cam.position.y - screen.cam.viewportHeight / 2 > background1.getY() + background1.getHeight()) {
            background1.setPosition(0, background2.getY() + background2.getHeight());
        } else if(screen.cam.position.y - screen.cam.viewportHeight / 2 > background2.getY() + background2.getHeight()) {
            background2.setPosition(0, background1.getY() + background1.getHeight());
        }

        for(Array<Tile> treeRow : borderTrees) {
            Tile tree = treeRow.get(0);

            if(tree.getY() < screen.cam.position.y - screen.cam.viewportHeight / 2 - tree.getTexture().getHeight() * 2) { // Tree outside screen
                loopRow();
            }
        }

        for(Collectible collectible : collectibles) {
            collectible.update(dt);
        }
    }

    public void render(SpriteBatch sb) {
        background1.draw(sb);
        background2.draw(sb);

        for(Array<Tile> treeRow : borderTrees) {
            for(Tile tree : treeRow) {
                tree.draw(sb);
            }
        }

        for(Array<Tile> tileRow : tileseses) {
            for(Tile tile : tileRow) {
                tile.draw(sb);
            }
        }

        for(Collectible collectible : collectibles) {
            collectible.draw(sb);
        }
    }

    private void createTrees() {
        // border trees
        for(int y = rows-1; y >= 0; y--) {
            Grass grassLeft = new Grass(grass1, 0, y * 32);
            Grass grassRight = new Grass(grass1, GameVars.V_WIDTH - 32, y * 32);
            Tree left = new Tree(treeTexture, world, 0, y * 32);
            Tree right = new Tree(treeTexture, world, GameVars.V_WIDTH - 32, y * 32);

            Array<Tile> treeRow = new Array<Tile>();
            treeRow.add(grassLeft);
            treeRow.add(grassRight);
            treeRow.add(left);
            treeRow.add(right);

            borderTrees.add(treeRow);
        }

        // normal tiles
        currentTile = new Vector2(3, 0);

        while(currentTile.y < rows) {
            Array<Tile> tileRow;

            if(currentTile.y > 0 && currentTile.y < 5) {
                tileRow = new Array<Tile>();
                currentTile.y++;
            } else {
                tileRow = walk();
            }

            tileseses.insert(0, tileRow);
        }
    }

    private void loopRow() {
        rows++;

        // border trees
        Array<Tile> treeRow = borderTrees.get(borderTrees.size - 1);
        for(Tile tree : treeRow) {
            tree.setY((rows - 1) * 32);
        }
        borderTrees.removeIndex(borderTrees.size - 1);
        borderTrees.insert(0, treeRow);

        // normal trees
        Array<Tile> tileRow = tileseses.get(tileseses.size - 1);
        tileRow.clear();
        tileRow = walk();
        tileseses.removeIndex(tileseses.size - 1);
        tileseses.insert(0, tileRow);
    }

    /**
     * IDEA: Weigh the random values depending on how many times we have walked in that direction
     */
    private Array<Tile> walk() {
        float targetY = currentTile.y + 1;
        Vector2 previousTile = new Vector2(currentTile.x, currentTile.y);

        Array<Vector2> tiles = new Array<Vector2>();
        tiles.add(new Vector2(currentTile.x, currentTile.y));

        while(currentTile.y < targetY) {
            float rnd = random.nextFloat();

            if(rnd > 0.75f) { // up
                Array<Tile> tileRow= new Array<Tile>();
                for(int x = 1; x <= 5; x++) {
                    Vector2 vector = new Vector2(x, currentTile.y);

                    float r = random.nextFloat();
                    Grass grass;
                    if(r > 1f - 1/3f)
                        grass = new Grass(grass1, vector.x * 32, vector.y * 32);
                    else if(r > 1f - 2/3f)
                        grass = new Grass(grass2, vector.x * 32, vector.y * 32);
                    else
                        grass = new Grass(grass3, vector.x * 32, vector.y * 32);

                    tileRow.add(grass);

                    if(!tiles.contains(vector, false)) {
                        if(random.nextFloat() > 0.3) {
                            Tree tree = new Tree(treeTexture, world, vector.x * 32, vector.y * 32);
                            tileRow.add(tree);
                        }
                    } else {
                        if(random.nextFloat() > 0.99) {
                            Collectible power = new PowerUpSpikes(world, spikesTexture, vector.x * 32, vector.y * 32);
                            collectibles.add(power);
                        } else {
                            if(random.nextFloat() > 0.5) {
                                Coin coin = new Coin(world, coinTexture, vector.x * 32, vector.y * 32);
                                collectibles.add(coin);
                            }
                        }
                    }
                }

                currentTile.add(0, 1);

                return tileRow;
            } else if(rnd > 0.375) { // right
                if(currentTile.x < 5 && Math.abs(previousTile.x - currentTile.x) <= 2) {
                    currentTile.add(1, 0);
                    tiles.add(new Vector2(currentTile.x, currentTile.y));
                }
            } else { // left
                if(currentTile.x > 1 && Math.abs(previousTile.x - currentTile.x) <= 2) {
                    currentTile.add(-1, 0);
                    tiles.add(new Vector2(currentTile.x, currentTile.y));
                }
            }
        }

        return null; // will never happen
    }
}
