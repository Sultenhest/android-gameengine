package dk.kea2017.autumn.sultenhest.gameengine;

import android.graphics.Color;
import android.util.Log;

public class TestScreen extends Screen
{
    public TestScreen(GameEngine gameEngine)
    {
        super(gameEngine);
    }

    @Override
    public void update(float deltaTime)
    {
        gameEngine.clearFrameBuffer(Color.GREEN);
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
