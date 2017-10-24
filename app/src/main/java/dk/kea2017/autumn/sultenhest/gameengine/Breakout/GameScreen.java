package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import java.util.List;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Screen;
import dk.kea2017.autumn.sultenhest.gameengine.Sound;
import dk.kea2017.autumn.sultenhest.gameengine.TouchEvent;

public class GameScreen extends Screen
{
    enum State
    {
        Paused,
        Running,
        GameOver
    }

    World world = null;
    WorldRenderer worldRenderer = null;
    State  state      = State.Running;

    Bitmap background = null;
    Bitmap resume     = null;
    Bitmap gameOver   = null;

    Typeface font     = null;

    Sound bounceSound   = null;
    Sound blockSound    = null;
    Sound gameOverSound = null;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        world = new World(gameEngine, new CollisionListener()
        {
            @Override
            public void collisionWall() {
                bounceSound.play(1);
            }

            @Override
            public void collisionPaddle() {
                bounceSound.play(1);
            }

            @Override
            public void collisionBlock() {
                blockSound.play(1);
            }
        } );
        worldRenderer = new WorldRenderer(gameEngine, world);

        //Bitmaps
        background = gameEngine.loadBitmap("breakout_assets/background.png");
        resume = gameEngine.loadBitmap("breakout_assets/resume.png");
        gameOver = gameEngine.loadBitmap("breakout_assets/gameover.png");

        //Init font
        font = gameEngine.loadFont("breakout_assets/font.ttf");

        //Sounds
        bounceSound   = gameEngine.loadSound("breakout_assets/bounce.wav");
        blockSound    = gameEngine.loadSound("breakout_assets/blocksplosion.wav");
        gameOverSound = gameEngine.loadSound("breakout_assets/gameover.wav");
    }

    @Override
    public void update(float deltaTime)
    {
        if(world.gameOver)
        {
            state = State.GameOver;
        }

        if(state == State.Paused && gameEngine.getTouchEvents().size() > 0)
        {
            state = State.Running;
            resume();
        }

        if(state == State.GameOver)
        {
            List<TouchEvent> events = gameEngine.getTouchEvents();
            for(int i = 0; i < events.size(); i++)
            {
                if (events.get(i).type == TouchEvent.TouchEventType.Up)
                {
                    gameEngine.setScreen(new MainMenuScreen(gameEngine));
                    return;
                }
            }
        }

        if(state == State.Running && gameEngine.getTouchY(0) < 38 && gameEngine.getTouchX(0) > 280)
        {
            state = State.Paused;
            pause();
            return;
        }

        gameEngine.drawBitmap(background, 0, 0);

        if(state == State.Running)
        {
            world.update(deltaTime, gameEngine.getAccelerometer()[0]);
        }
        worldRenderer.render();

        gameEngine.drawText(font, "POINTS " + Integer.toString(world.points), 24, 24, Color.GREEN, 15);

        if(state == State.Paused)
        {
            gameEngine.drawBitmap(resume, 160 - (resume.getWidth() / 2), 240 - (resume.getHeight() / 2));
        }

        if(state == State.GameOver)
        {
            gameEngine.drawBitmap(gameOver, 160 - (gameOver.getWidth() / 2), 240 - (gameOver.getHeight() / 2));
        }
    }

    @Override
    public void pause()
    {
        if(state == State.Running) state = State.Paused;
        gameEngine.music.pause();
    }

    @Override
    public void resume()
    {
        gameEngine.music.play();
    }

    @Override
    public void dispose()
    {

    }
}
