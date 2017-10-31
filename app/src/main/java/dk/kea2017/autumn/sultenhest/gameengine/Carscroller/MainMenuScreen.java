package dk.kea2017.autumn.sultenhest.gameengine.Carscroller;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Screen;

public class MainMenuScreen extends Screen
{
    Bitmap background = null;
    Bitmap startGame = null;
    float passedTime  = 0;
    long startTime    = System.nanoTime();

    public MainMenuScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        background = gameEngine.loadBitmap("carscroller_assets/xcarbackground.png");
        startGame = gameEngine.loadBitmap("carscroller_assets/xstartgame.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if( gameEngine.isTouchDown(0) )
        {
            gameEngine.setScreen(new GameScreen(gameEngine));
            return;
        }

        gameEngine.drawBitmap(background, 0, 0);

        passedTime += deltaTime;
        if( passedTime - (int)passedTime > 0.5f )
        {
            gameEngine.drawBitmap(startGame, 240 - (startGame.getWidth() / 2), 150);
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
