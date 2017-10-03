package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Screen;

public class MainMenuScreen extends Screen
{
    Bitmap mainMenu = null;
    Bitmap insertCoin = null;
    float passedTime = 0;
    long startTime = System.nanoTime();

    public MainMenuScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        mainMenu = gameEngine.loadBitmap("breakout_assets/mainmenu.png");
        insertCoin = gameEngine.loadBitmap("breakout_assets/insertcoin.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if( gameEngine.isTouchDown(0) )
        {
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
