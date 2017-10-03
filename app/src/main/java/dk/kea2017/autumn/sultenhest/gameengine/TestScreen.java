package dk.kea2017.autumn.sultenhest.gameengine;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.List;

public class TestScreen extends Screen
{
    Bitmap bob = null;
    Bitmap sose = null;
    float soseX = 0;
    int soseY = 50;
    TouchEvent event = null;
    Sound sound = null;
    Music music = null;
    boolean isPlaying = false;

    public TestScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        bob = gameEngine.loadBitmap("bob.png");
        sose = gameEngine.loadBitmap("sose.png");
        sound = gameEngine.loadSound("blocksplosion.wav");
        music = gameEngine.loadMusic("music.ogg");
        music.setLooping(true);
        music.play();
        isPlaying = true;
    }

    @Override
    public void update(float deltaTime)
    {
        gameEngine.clearFrameBuffer(Color.GREEN);

        soseX = soseX + 50 * deltaTime;
        if(soseX > gameEngine.getFrameBufferWidth()) soseX = 0 - sose.getWidth();

        gameEngine.drawBitmap(sose, (int)soseX, soseY);

        /*
        List<TouchEvent> touchEvents = gameEngine.getTouchEvents();
        int stop = touchEvents.size();

        if(stop == 0 && event != null)
        {
            gameEngine.drawBitmap(sose, gameEngine.getTouchX(event.pointer), gameEngine.getTouchY(event.pointer));
        }

        for(int i = 0; i < stop; i++)
        {
            event = touchEvents.get(i);
            //Log.d("TestScreen", "*** Event touch type: " + event.type + ", x: " + event.x + ", y: " + event.y);
            gameEngine.drawBitmap(sose, gameEngine.getTouchX(event.pointer), gameEngine.getTouchY(event.pointer));
            if(event.type == TouchEvent.TouchEventType.Down)
            {
                sound.play(1);
            }
        }

        if(gameEngine.isTouchDown(0))
        {
            if(music.isPlaying())
            {
                music.pause();
                isPlaying = false;
            }
            else
            {
                music.play();
                isPlaying = true;
            }
        }
        */
    }

    @Override
    public void pause()
    {
        Log.d("TestScreen", "*** Pausing");
        music.pause();
        isPlaying = false;
    }

    @Override
    public void resume()
    {
        Log.d("TestScreen", "*** Resuming");
        if(!isPlaying)
        {
            music.play();
            isPlaying = true;
        }
    }

    @Override
    public void dispose()
    {
        Log.d("TestScreen", "*** Disposed");
        music.stop();
        isPlaying = false;
    }
}
