package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Music;
import dk.kea2017.autumn.sultenhest.gameengine.Screen;
import dk.kea2017.autumn.sultenhest.gameengine.Sound;

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

    Bitmap background = null;
    Bitmap resume     = null;
    Bitmap gameOver   = null;
    State  state      = State.Running;

    Music  music         = null;
    Sound  explosion     = null;
    Sound  gameOverSound = null;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        world = new World();
        worldRenderer = new WorldRenderer(gameEngine, world);

        //Bitmaps
        background = gameEngine.loadBitmap("breakout_assets/background.png");
        resume = gameEngine.loadBitmap("breakout_assets/resume.png");
        gameOver = gameEngine.loadBitmap("breakout_assets/gameover.png");

        //Music and Sounds
        music = gameEngine.loadMusic("breakout_assets/music.ogg");
        explosion = gameEngine.loadSound("breakout_assets/explosion.ogg");
        gameOverSound = gameEngine.loadSound("breakout_assets/gameover.wav");
        music.setLooping(true);
        music.play();
    }

    @Override
    public void update(float deltaTime)
    {
        if(state == State.Paused && gameEngine.getTouchEvents().size() > 0)
        {
            state = State.Running;
        }

        if(state == State.GameOver && gameEngine.getTouchEvents().size() > 0)
        {
            gameEngine.setScreen(new MainMenuScreen(gameEngine));
            return;
        }

        if(state == State.Running && gameEngine.getTouchY(0) < 38 && gameEngine.getTouchX(0) > 280)
        {
            state = State.Paused;
            return;
        }

        gameEngine.drawBitmap(background, 0, 0);

        if(state == State.Running)
        {
            world.update(deltaTime, gameEngine.getAccelerometer()[0]);
        }
        worldRenderer.render();

        if(state == State.Paused)
        {
            gameEngine.drawBitmap(resume, 160 - (resume.getWidth() / 2), 240 - (resume.getHeight() / 2));
        }

        if(state == State.GameOver)
        {
            gameOverSound.play(1);
            gameEngine.drawBitmap(gameOver, 160 - (gameOver.getWidth() / 2), 240 - (gameOver.getHeight() / 2));
        }
    }

    @Override
    public void pause()
    {
        if(state == State.Running) state = State.Paused;
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}
