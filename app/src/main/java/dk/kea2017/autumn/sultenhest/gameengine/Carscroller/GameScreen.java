package dk.kea2017.autumn.sultenhest.gameengine.Carscroller;

import android.graphics.Bitmap;
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

    World world;
    WorldRenderer worldRenderer;
    State state = State.Running;

    Bitmap background = null;
    float backgroundX = 0;
    Bitmap resume = null;
    Bitmap gameOver = null;

    //Typeface font = null;

    Sound bounceSound = null;
    Sound blockSound = null;
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
            public void collisionMonster() {
                bounceSound.play(1);
            }

            @Override
            public void gameOver() {
                gameOverSound.play(1);
            }
        } );
        worldRenderer = new WorldRenderer(gameEngine, world);

        //Bitmaps
        background = gameEngine.loadBitmap("carscroller_assets/xcarbackground.png");
        resume = gameEngine.loadBitmap("carscroller_assets/resume.png");
        gameOver = gameEngine.loadBitmap("carscroller_assets/gameover.png");

        //Init font
        //font = gameEngine.loadFont("carscroller_assets/font.ttf");

        //Sounds
        bounceSound   = gameEngine.loadSound("carscroller_assets/bounce.wav");
        blockSound    = gameEngine.loadSound("carscroller_assets/blocksplosion.wav");
        gameOverSound = gameEngine.loadSound("carscroller_assets/gameover.wav");
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

        //Scroll the background image
        backgroundX = backgroundX + 100 * deltaTime;

        if(backgroundX > 2700 - 480)
        {
            backgroundX = 0;
        }

        gameEngine.drawBitmap(background, 0, 0, (int) backgroundX, 0, 480, 320);

        if(state == State.Running)
        {
            world.update(deltaTime, gameEngine.getAccelerometer()[1]);
        }
        worldRenderer.render();

        //gameEngine.drawText(font, ("LIVES " + Integer.toString(world.lives) + " | POINTS" + Integer.toString(world.points)), 24, 24, Color.GREEN, 12);

        if(state == State.Paused)
        {
            gameEngine.drawBitmap(resume, 240 - (resume.getWidth() / 2), 160 - (resume.getHeight() / 2));
        }

        if(state == State.GameOver)
        {
            gameEngine.drawBitmap(gameOver, 240 - (gameOver.getWidth() / 2), 160 - (gameOver.getHeight() / 2));
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
