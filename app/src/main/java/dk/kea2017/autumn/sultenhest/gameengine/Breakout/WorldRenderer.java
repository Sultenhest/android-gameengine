package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap ballImage;

    public WorldRenderer(GameEngine gameEngine, World world)
    {
        this.gameEngine = gameEngine;
        this.world = world;
        ballImage = gameEngine.loadBitmap("breakout_assets/ball.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(ballImage, world.ball.x, world.ball.y);
    }
}
