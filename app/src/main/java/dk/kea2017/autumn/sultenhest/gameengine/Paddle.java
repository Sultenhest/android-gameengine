package dk.kea2017.autumn.sultenhest.gameengine;

import dk.kea2017.autumn.sultenhest.gameengine.Breakout.World;

public class Paddle {
    public static final float WIDTH = 56;
    public static final float HEIGHT = 11;
    public float x = 169 - WIDTH/2;
    public float y = World.MAX_Y - 40 - HEIGHT/2;
}
