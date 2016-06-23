package com.nalsnag.frisbee.tools;

public class GameVars {
    //public static final int V_WIDTH = 448; // 14 tiles
    //public static final int V_HEIGHT = 768; // 24 tiles

    public static final int TILE_SIZE = 256;

    public static final int V_WIDTH = 7 * TILE_SIZE; // 7 tiles
    public static final int V_HEIGHT = 12 * TILE_SIZE; // 12 tiles

    public static final float PPM = 100;

    public static final short FRISBEE_BIT = 2;
    public static final short TREE_BIT = 4;
    public static final short BIRD_BIT = 8;
    public static final short SPIKES_BIT = 16;
    public static final short COIN_BIT = 32;

    public static final int POWERUP_NONE = 0;
    public static final int POWERUP_SPIKES = 1;

    public static final int QUEST_TRAVEL_50 = 0;
    public static final int QUEST_NO_TILT_10 = 1;
}
