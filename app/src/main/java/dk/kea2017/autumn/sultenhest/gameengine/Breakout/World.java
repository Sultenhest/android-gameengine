package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import dk.kea2017.autumn.sultenhest.gameengine.Paddle;

public class World
{
    public static final float MIN_X = 0;
    public static final float MAX_X = 319;
    public static final float MIN_Y = 36;
    public static final float MAX_Y = 479;
    Ball ball = new Ball();
    Paddle paddle = new Paddle();

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
}
