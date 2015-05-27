package com.ondrejruttkay.weather.android.entity.api;

/**
 * Created by Onko on 5/24/2015.
 */
public class Wind {
    private double speed;
    private double deg;


    public double getSpeed() {
        return speed;
    }


    public Direction getDirection() {
        if (deg <= 22.5) {
            return Direction.N;
        } else if (deg <= 67.5) {
            return Direction.NE;
        } else if (deg <= 112.5) {
            return Direction.E;
        } else if (deg <= 157.5) {
            return Direction.SE;
        } else if (deg <= 202.5) {
            return Direction.S;
        } else if (deg <= 247.5) {
            return Direction.SW;
        } else if (deg <= 292.5) {
            return Direction.W;
        } else if (deg <= 337.5) {
            return Direction.NW;
        }
        return Direction.N;
    }


    public enum Direction {
        N, NE, E, SE, S, SW, W, NW
    }
}
