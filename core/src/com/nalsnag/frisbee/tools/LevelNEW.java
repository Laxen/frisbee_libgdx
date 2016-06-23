package com.nalsnag.frisbee.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.nalsnag.frisbee.Collectible;
import com.nalsnag.frisbee.screens.PlayScreen;
import com.nalsnag.frisbee.sprites.Coin;
import com.nalsnag.frisbee.sprites.Grass;
import com.nalsnag.frisbee.sprites.GrassNEW;
import com.nalsnag.frisbee.sprites.PowerUpSpikes;
import com.nalsnag.frisbee.sprites.Tile;
import com.nalsnag.frisbee.sprites.Tree;
import com.nalsnag.frisbee.sprites.TreeNEW;

import java.util.Random;

public class LevelNEW {
    private World world;
    private PlayScreen screen;

    private Array<Array<Tile>> borderTrees;
    private Array<Array<Tile>> groundTiles;
    private Array<Array<Tile>> treeTiles;
    private Texture treeTexture;

    private Pool<TreeNEW> treePool;
    private Pool<GrassNEW> grassPool;

    private Texture grass1, grass2, grass3;
    private Texture coinTexture;
    private Texture spikesTexture;

    private Random random;

    private int rows = 14;

    private Vector2 currentTile;

    private Array<Collectible> collectibles;

    public LevelNEW(World world, PlayScreen screen) {
        this.world = world;
        this.screen = screen;

        treeTexture = new Texture("tree2.png");
        borderTrees = new Array<Array<Tile>>();
        groundTiles = new Array<Array<Tile>>();
        treeTiles = new Array<Array<Tile>>();

//        grass1 = new Texture("grass.png");
//        grass2 = new Texture("grass2.png");
//        grass3 = new Texture("grass3.png");

        grass1 = new Texture("grasspoly.png");
        grass2 = new Texture("grasspoly.png");
        grass3 = new Texture("grasspoly.png");

        coinTexture = new Texture("coin.png");
        spikesTexture = new Texture("spikes.png");

        random = new Random();

        collectibles = new Array<Collectible>();

        treePool = new TreePool(world, treeTexture);
        grassPool = new GrassPool(grass1, grass2, grass3);

        createTrees();
    }

    public void update(float dt) {
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
        for(Array<Tile> treeRow : borderTrees) {
            for(Tile tree : treeRow) {
                tree.draw(sb);
            }
        }

        for(Array<Tile> groundRow : groundTiles) {
            for(Tile tile : groundRow) {
                tile.draw(sb);
            }
        }

        for(Collectible collectible : collectibles) {
            collectible.draw(sb);
        }

        for(Array<Tile> treeRow : treeTiles) {
            for(Tile tile : treeRow) {
                tile.draw(sb);
            }
        }
    }

    private void createTrees() {
        // border trees
        for(int y = rows-1; y >= 0; y--) {
            GrassNEW grassLeft = grassPool.obtain();
            GrassNEW grassRight = grassPool.obtain();
            grassLeft.init(0, y * GameVars.TILE_SIZE);
            grassRight.init(GameVars.V_WIDTH - GameVars.TILE_SIZE, y * GameVars.TILE_SIZE);

            TreeNEW left = treePool.obtain();
            TreeNEW right = treePool.obtain();
            left.init(0, y * GameVars.TILE_SIZE);
            right.init(GameVars.V_WIDTH - GameVars.TILE_SIZE, y * GameVars.TILE_SIZE);

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
            Array<Tile> grassRow;
            Array<Tile> treeRow;

            if(currentTile.y > 0 && currentTile.y < 5) {
                grassRow = new Array<Tile>();
                treeRow = new Array<Tile>();

                for(int x = 1; x <= 5; x++) {
                    GrassNEW grass = grassPool.obtain();
                    grass.init(x * GameVars.TILE_SIZE, currentTile.y * GameVars.TILE_SIZE);
                    grassRow.add(grass);
                }

                currentTile.y++;
            } else {
                Array<Tile>[] rows = walk();
                grassRow = rows[0];
                treeRow = rows[1];
            }

            groundTiles.insert(0, grassRow);
            treeTiles.insert(0, treeRow);
        }
    }

    private void loopRow() {
        rows++;

        // border trees
        Array<Tile> treeRowBorder = borderTrees.get(borderTrees.size - 1);
        for(Tile tree : treeRowBorder) {
            tree.setY((rows - 1) * GameVars.TILE_SIZE);
        }
        borderTrees.removeIndex(borderTrees.size - 1);
        borderTrees.insert(0, treeRowBorder);

        // normal trees
        Array<Tile> groundRow = groundTiles.get(groundTiles.size - 1);
        Array<Tile> treeRow = treeTiles.get(treeTiles.size - 1);

        for(Tile tile : groundRow) {
            GrassNEW grass = (GrassNEW) tile;
            grassPool.free(grass);
        }

        for(Tile tile : treeRow) {
            TreeNEW tree = (TreeNEW) tile;
            treePool.free(tree);
        }

        groundRow.clear();
        treeRow.clear();

        Array<Tile>[] rows = walk();
        groundRow = rows[0];
        treeRow = rows[1];

        groundTiles.removeIndex(groundTiles.size - 1);
        groundTiles.insert(0, groundRow);
        treeTiles.removeIndex(treeTiles.size - 1);
        treeTiles.insert(0, treeRow);
    }

    /**
     * IDEA: Weigh the random values depending on how many times we have walked in that direction
     */
    private Array<Tile>[] walk() {
        float targetY = currentTile.y + 1;
        Vector2 previousTile = new Vector2(currentTile.x, currentTile.y);

        Array<Vector2> tiles = new Array<Vector2>();
        tiles.add(new Vector2(currentTile.x, currentTile.y));

        while(currentTile.y < targetY) {
            float rnd = random.nextFloat();

            if(rnd > 0.75f) { // up
                Array<Tile> grassRow = new Array<Tile>();
                Array<Tile> treeRow = new Array<Tile>();

                for(int x = 1; x <= 5; x++) {
                    Vector2 vector = new Vector2(x, currentTile.y);

                    GrassNEW grass = grassPool.obtain();
                    grass.init(vector.x * GameVars.TILE_SIZE, vector.y * GameVars.TILE_SIZE);
                    grassRow.add(grass);

                    if(!tiles.contains(vector, false)) {
                        if(random.nextFloat() > 0.3) {
                            TreeNEW tree = treePool.obtain();
                            tree.init(vector.x * GameVars.TILE_SIZE, vector.y * GameVars.TILE_SIZE);
                            treeRow.add(tree);
                        }
                    } else {
                        if(random.nextFloat() > 0.99) {
                            Collectible power = new PowerUpSpikes(world, spikesTexture, vector.x * GameVars.TILE_SIZE, vector.y * GameVars.TILE_SIZE);
                            collectibles.add(power);
                        } else {
                            if(random.nextFloat() > 0.5) {
                                Coin coin = new Coin(world, coinTexture, vector.x * GameVars.TILE_SIZE, vector.y * GameVars.TILE_SIZE);
                                collectibles.add(coin);
                            }
                        }
                    }
                }

                currentTile.add(0, 1);

                Array<Tile>[] ret = new Array[2];
                ret[0] = grassRow;
                ret[1] = treeRow;

                return ret;
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

    public void dispose() {
        for(Array<Tile> tileRow : borderTrees) {
            for(Tile tile : tileRow) {
                tile.dispose();
            }
        }

        for(Array<Tile> tileRow : groundTiles) {
            for(Tile tile : tileRow) {
                tile.dispose();
            }
        }

        for(Array<Tile> tileRow : treeTiles) {
            for(Tile tile : tileRow) {
                tile.dispose();
            }
        }

        for(Collectible collectible : collectibles) {
            collectible.dispose();
        }

        treeTexture.dispose();
        grass1.dispose();
        grass2.dispose();
        grass3.dispose();
        coinTexture.dispose();
        spikesTexture.dispose();
    }
}
