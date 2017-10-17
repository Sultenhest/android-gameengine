package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

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

    public World()
    {
        generateBlocks();
    }

    private void generateBlocks()
    {
        blocks.clear();

        for (int y = 50, type = 0; y < 50 + 8 * Block.HEIGHT; y = y + (int)Block.HEIGHT, type++)
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
        }

        //Right edge
        if(ball.x > MAX_X - ball.WIDTH)
        {
            ball.vx = -ball.vx;
            ball.x  = (int) (MAX_X - ball.WIDTH);
        }

        //Top edge
        if(ball.y < MIN_Y)
        {
            ball.vy = -ball.vy;
            ball.y  = (int) MIN_Y;
        }

        //Bottom edge
        if(ball.y > MAX_Y - ball.HEIGHT)
        {
            ball.vy = -ball.vy;
            ball.y  = (int) (MAX_Y - ball.HEIGHT);
        }

        // Paddle properties
        paddle.x = paddle.x + accelX * deltatime * 50;
        if (paddle.x < MIN_X) paddle.x = MIN_X;
        if (paddle.x + Paddle.WIDTH > MAX_X) paddle.x = MAX_X - Paddle.WIDTH;

        collideBallPaddle();
        collideBallBlocks();
    }

    private void collideBallPaddle()
    {
        if (ball.y + Ball.HEIGHT >= paddle.y &&
                ball.x < paddle.x + Paddle.WIDTH &&
                ball.x + Ball.WIDTH > paddle.x)
        {
            ball.y = (int)paddle.y - (int)Ball.HEIGHT - 2;
            ball.vy = -ball.vy;
        }
    }

    private void collideBallBlocks()
    {
        Block block = null;
        for (int i = 0; i < blocks.size(); i++)
        {
            block = blocks.get(i);

            if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, Block.WIDTH, Block.HEIGHT))
            {
                blocks.remove(i);
                i--;
            }
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
}