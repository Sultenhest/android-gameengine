package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap ballImage;
    Bitmap paddleImage;
    Bitmap blocksImage;

    public WorldRenderer(GameEngine gameEngine, World world)
    {
        this.gameEngine = gameEngine;
        this.world = world;
        ballImage = gameEngine.loadBitmap("breakout_assets/ball.png");
        paddleImage = gameEngine.loadBitmap("breakout_assets/paddle.png");
        blocksImage = gameEngine.loadBitmap("breakout_assets/blocks.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(ballImage, world.ball.x, world.ball.y);
        gameEngine.drawBitmap(paddleImage, (int)world.paddle.x, (int)world.paddle.y);
        int listSize = world.blocks.size();

        // Draw the blocks in rows and columns
        for (int i = 0; i < listSize; i++)
        {
            Block block = world.blocks.get(i);
            gameEngine.drawBitmap(blocksImage, (int)block.x, (int)block.y,
                    0, (int)(block.type * (int)Block.HEIGHT), (int)Block.WIDTH, (int)Block.HEIGHT);
        }
    }
}
