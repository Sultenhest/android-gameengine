package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Paddle;

public class World
{
    public static final float MIN_X = 0;
    public static final float MAX_X = 319;
    public static final float MIN_Y = 36;
    public static final float MAX_Y = 479;
    Ball ball = new Ball();
    Paddle paddle = new Paddle();
    List<Block> blocks = new ArrayList<Block>();
    GameEngine gameEngine;
    CollisionListener collisionListener;
    boolean gameOver = false;
    int points = 0;
    int lives = 3;
    int paddleHits = 0;
    int advance = 0;
    boolean levelDone = false;

    public World()
    {
        Log.d("World class", "This should never happen...");
        throw new RuntimeException("Yeah this should not have happened");
    }

    public World(GameEngine gameEngine, CollisionListener collisionListener)
    {
        this.gameEngine = gameEngine;
        this.collisionListener = collisionListener;
        generateBlocks();
    }

    private void generateBlocks()
    {
        blocks.clear();

        for (int y = 50, type = 0; y < 50 + 1 * Block.HEIGHT; y = y + (int)Block.HEIGHT, type++)
        //for (int y = 50, type = 0; y < 50 + 8 * Block.HEIGHT; y = y + (int)Block.HEIGHT, type++)
        {
            // For each column
            for (int x = 20; x < MAX_X - Block.WIDTH/2; x = x + (int)Block.WIDTH)
            {
                blocks.add(new Block(x, y, type));
            }
        }
    }

    public void update(float deltatime, float accelX)
    {
        ball.x = (int)(ball.x + ball.vx * deltatime);
        ball.y = (int)(ball.y + ball.vy * deltatime);

        //Left edge
        if(ball.x < MIN_X)
        {
            ball.vx = -ball.vx;
            ball.x  = (int) MIN_X;
            collisionListener.collisionWall();
        }

        //Right edge
        if(ball.x > MAX_X - ball.WIDTH)
        {
            ball.vx = -ball.vx;
            ball.x  = (int) (MAX_X - ball.WIDTH);
            collisionListener.collisionWall();
        }

        //Top edge
        if(ball.y < MIN_Y)
        {
            ball.vy = -ball.vy;
            ball.y  = (int) MIN_Y;
            collisionListener.collisionWall();
        }

        //Bottom edge
        /*
        if(ball.y > MAX_Y - ball.HEIGHT)
        {
            ball.vy = -ball.vy;
            ball.y  = (int) (MAX_Y - ball.HEIGHT);
        }
        */
        //Reset game if ball touches bottom. Naughty.
        if(ball.y + ball.HEIGHT > MAX_Y)
        {
            lives = lives - 1;
            if(lives == 0)
            {
                gameOver = true;
                collisionListener.gameOver();
                return;
            } else {
                ball.y = (int) MAX_Y/2;
                if(ball.vy > 0) ball.vy = -ball.vy;
                collisionListener.collisionBlock();
            }
        }

        // Paddle properties
        paddle.x = paddle.x + accelX * deltatime * 50;
        if (paddle.x < MIN_X) paddle.x = MIN_X;
        if (paddle.x + Paddle.WIDTH > MAX_X) paddle.x = MAX_X - Paddle.WIDTH;

        if(gameEngine.isTouchDown(0))
        {
            if(gameEngine.getTouchY(0) > 450)
            {
                paddle.x = gameEngine.getTouchX(0);
            }
        }

        collideBallPaddle();
        collideBallBlocks(deltatime);

        //If all blocks are removed, regenerate or better: start a new level
        if(blocks.size() == 0)
        {
            levelDone = true;
            //generateBlocks();
        }
    }

    private void collideBallBlocks(float deltatime)
    {
        Block block = null;
        for (int i = 0; i < blocks.size(); i++)
        {
            block = blocks.get(i);

            if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, Block.WIDTH, Block.HEIGHT))
            {
                blocks.remove(i);
                collisionListener.collisionBlock();
                i--;
                float oldvx = ball.vx;
                float oldvy = ball.vy;
                reflectBall(ball, block);
                ball.x = (int)(ball.x - oldvx * deltatime * 1.01f);
                ball.y = (int)(ball.y - oldvy * deltatime * 1.01f);
                points = points + (10 - block.type);
            }
        }
    }

    private void reflectBall(Ball ball, Block block)
    {
        // Check the top left corner of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1, 1))
        {
            if (ball.vx > 0) ball.vx = -ball.vx;
            if (ball.vy > 0) ball.vy = -ball.vy;
            return;
        }

        // Check top right corner
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x - Block.WIDTH, block.y, 1, 1))
        {
            if (ball.vx < 0) ball.vx = -ball.vx;
            if (ball.vy > 0) ball.vy = -ball.vy;
            return;
        }

        // Check the bottom left corner of the block
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y + Block.HEIGHT, 1, 1))
        {
            if (ball.vx > 0) ball.vx = -ball.vx;
            if (ball.vy < 0) ball.vy = -ball.vy;
            return;
        }

        // Check the bottom right corner of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x - Block.WIDTH, block.y + Block.HEIGHT, 1, 1))
        {
            if (ball.vx < 0) ball.vx = -ball.vx;
            if (ball.vy > 0) ball.vy = -ball.vy;
            return;
        }

        // Check the top edge of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, Block.WIDTH, 1))
        {
            //if (ball.vy > 0) ball.vy = -ball.vy;
            ball.vy = -ball.vy;
            return;
        }

        // Check the bottom edge of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y + Block.HEIGHT, Block.WIDTH, 1))
        {
            ball.vy = -ball.vy;
            return;
        }

        // Check the left edge of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1, Block.HEIGHT))
        {
            ball.vx = -ball.vx;
            return;
        }

        // Check the right edge of the block
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x + Block.WIDTH, block.y, 1, Block.HEIGHT))
        {
            ball.vx = -ball.vx;
            return;
        }
    }


    // Refactor this piece of shit
    private boolean collideRects(float x, float y, float width, float height, float x2, float y2, float width2, float height2)
    {
        Rect rect1 = new Rect((int) x, (int)y, (int)x+(int)width, (int)y+(int)height);
        Rect rect2 = new Rect((int) x2, (int)y2, (int)x2+(int)width2, (int)y2+(int)height2);

        return rect1.intersect(rect2);

        /*
        // Teacher's implementation
        if (x < x2+width2 && x + width > x2 && y + height > y2 && y < y2 + height2)
        {
            return true;
        }
        return false;
        */
    }

    private void collideBallPaddle()
    {
        if (ball.y + Ball.HEIGHT >= paddle.y &&
                ball.x < paddle.x + Paddle.WIDTH &&
                ball.x + Ball.WIDTH > paddle.x)
        {
            ball.y = (int)paddle.y - (int)Ball.HEIGHT - 2;
            ball.vy = -ball.vy;
            collisionListener.collisionPaddle();
            paddleHits++;

            if(paddleHits == 3) //To be adjusted for normal play
            {
                paddleHits = 0;
                advance = 10;
                advanceBlocks();
            }
        }
    }

    private void advanceBlocks()
    {
        Block block;
        int size = blocks.size();
        for(int i = 0; i < size; i++)
        {
            block = blocks.get(i);
            block.y = block.y + advance;
        }
    }
}