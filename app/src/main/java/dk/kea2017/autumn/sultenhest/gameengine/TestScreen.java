package dk.kea2017.autumn.sultenhest.gameengine;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class TestScreen extends Screen
{
    Bitmap bob = null;
    Bitmap sose = null;

    public TestScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        bob = gameEngine.loadBitmap("bob.png");
        sose = gameEngine.loadBitmap("sose.png");
    }

    @Override
    public void update(float deltaTime)
    {
        gameEngine.clearFrameBuffer(Color.BLUE);
        //gameEngine.drawBitmap(bob, 10, 10);
        //gameEngine.drawBitmap(bob, 100, 200, 0, 0, 64, 64);

        for( int pointer = 0; pointer < 5; pointer++ )
        {
            if( gameEngine.isTouchDown(pointer) )
            {
                gameEngine.drawBitmap(sose, gameEngine.getTouchX(pointer), gameEngine.getTouchY(pointer));
            }
        }

        float accX = gameEngine.getAccelerometer()[0];
        float accY = gameEngine.getAccelerometer()[1];
        //accX = 0; accY = 0;
        float x    = gameEngine.getFrameBufferWidth() / 2 + (accX/10) * gameEngine.getFrameBufferWidth();
        float y    = gameEngine.getFrameBufferHeight() / 2 + (accY/10) * gameEngine.getFrameBufferHeight();

        gameEngine.drawBitmap(sose, (int) (x - (sose.getWidth()/2)), (int) (y - (sose.getHeight()/2)));

    }

    @Override
    public void pause()
    {
        Log.d("TestScreen", "*** Pausing");
    }

    @Override
    public void resume()
    {
        Log.d("TestScreen", "*** Resuming");
    }

    @Override
    public void dispose()
    {
        Log.d("TestScreen", "*** Disposed");
    }
}
