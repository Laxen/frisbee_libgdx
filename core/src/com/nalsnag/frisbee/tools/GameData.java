package com.nalsnag.frisbee.tools;

public class GameData {
    private static float totalDistance = 0;
    private static float distanceWithoutTilt = 0;

    private static int coins = 0;

    public GameData() {

    }

    public static void reset() {
        totalDistance = 0;
        distanceWithoutTilt = 0;
//        coins = 0;
    }

    public static void addCoin() {
        coins++;
    }

    public static void addCoins(int amount) {
        coins += amount;
    }

    public static int getCoins() {
        return coins;
    }

    public static void setTotalDistance(float distance) {
        totalDistance = distance;
    }

    public static void setDistanceWithoutTilt(float distance) {
        distanceWithoutTilt = distance;
    }

    public static float getTotalDistance() {
        return totalDistance;
    }

    public static float getDistanceWithoutTilt() {
        return distanceWithoutTilt;
    }
}
