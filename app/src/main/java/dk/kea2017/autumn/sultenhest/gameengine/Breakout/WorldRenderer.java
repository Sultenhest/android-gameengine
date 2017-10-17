package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap ballImage;
    Bitmap paddleImage;

    public WorldRenderer(GameEngine gameEngine, World world)
    {
        this.gameEngine = gameEngine;
        this.world = world;
        ballImage = gameEngine.loadBitmap("breakout_assets/ball.png");
        paddleImage = gameEngine.loadBitmap("breakout_assets/paddle.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(ballImage, world.ball.x, world.ball.y);
        gameEngine.drawBitmap(paddleImage, (int)world.paddle.x, (int)world.paddle.y);
    }
}
