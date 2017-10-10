package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Music;
import dk.kea2017.autumn.sultenhest.gameengine.Screen;
import dk.kea2017.autumn.sultenhest.gameengine.Sound;

public class MainMenuScreen extends Screen
{
    Bitmap mainMenu   = null;
    Bitmap insertCoin = null;
    float passedTime  = 0;
    long startTime    = System.nanoTime();

    Music music       = null;
    Sound sound       = null;

    public MainMenuScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        mainMenu = gameEngine.loadBitmap("breakout_assets/mainmenu.png");
        insertCoin = gameEngine.loadBitmap("breakout_assets/insertcoin.png");

        //Music and Sound
        sound = gameEngine.loadSound("breakout_assets/explosion.ogg");
        music = gameEngine.loadMusic("breakout_assets/music.ogg");
        music.setLooping(true);
        music.play();
    }

    @Override
    public void update(float deltaTime)
    {
        if( gameEngine.isTouchDown(0) )
        {
            sound.play(1);
            gameEngine.setScreen(new GameScreen(gameEngine));
            return;
        }

        gameEngine.drawBitmap(mainMenu, 0, 0);

        passedTime = passedTime + deltaTime;
        if( (passedTime - (int)passedTime) > 0.5f )
        {
            gameEngine.drawBitmap(insertCoin, 160 - (insertCoin.getWidth() / 2), 350);
        }
    }

    @Override
    public void pause()
    {

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
