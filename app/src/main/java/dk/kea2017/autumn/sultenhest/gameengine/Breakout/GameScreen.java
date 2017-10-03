package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Music;
import dk.kea2017.autumn.sultenhest.gameengine.Screen;
import dk.kea2017.autumn.sultenhest.gameengine.Sound;

public class GameScreen extends Screen
{
    Bitmap background = null;
    Music music = null;
    Sound explosion = null;

    public GameScreen(GameEngine gameEngine) {
        super(gameEngine);
        background = gameEngine.loadBitmap("breakout_assets/background.png");
        music = gameEngine.loadMusic("breakout_assets/music.ogg");
        explosion = gameEngine.loadSound("breakout_assets/explosion.ogg");
        music.setLooping(true);
        music.play();
    }

    @Override
    public void update(float deltaTime)
    {
        gameEngine.drawBitmap(background, 0, 0);

        if(gameEngine.isTouchDown(0))
        {
            explosion.play(1);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
