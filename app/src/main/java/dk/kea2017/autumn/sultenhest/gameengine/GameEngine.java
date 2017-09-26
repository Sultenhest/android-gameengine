package dk.kea2017.autumn.sultenhest.gameengine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class GameEngine extends Activity implements Runnable, SensorEventListener
{
    private Screen screen;
    private Canvas canvas;
    private Bitmap virtualScreen;
    Rect src = new Rect();
    Rect dst = new Rect();

    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();
    private List<TouchEvent> touchEventCopied = new ArrayList<>();

    private float[] accelerometer = new float[3];

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();

        screen = createStartScreen();

        if(surfaceView.getWidth() > surfaceView.getHeight())
        {
            setVirtualScreen(480, 320);
        }
        else
        {
            setVirtualScreen(320, 480);
        }
        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer, touchEventPool);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
        {
            Sensor accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values, 0, accelerometer, 0, 3);
        accelerometer[0] = -1.0f * accelerometer[0];
    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    private void fillEvents()
    {
        synchronized(touchEventBuffer)
        {
            int stop = touchEventBuffer.size();
            for(int i = 0; i < stop; i++)
            {
                touchEventCopied.add(touchEventBuffer.get(i)); //copy all objects from one list to the other
            }
            touchEventBuffer.clear();
        }
    }

    private void freeEvents()
    {
        synchronized(touchEventCopied)
        {
            int stop = touchEventCopied.size();
            for(int i = 0; i < stop; i++)
            {
                touchEventPool.free(touchEventCopied.get(i)); //return all used objects to the free pool
            }
            touchEventCopied.clear();
        }
    }

    public List<TouchEvent> getTouchEvents()
    {
        return touchEventCopied;
    }

    public void setVirtualScreen(int width, int height)
    {
        if(virtualScreen != null) virtualScreen.recycle();
        virtualScreen = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(virtualScreen);
    }

    public int getFrameBufferWidth()
    {
        return virtualScreen.getWidth();
    }
    public int getFrameBufferHeight()
    {
        return virtualScreen.getHeight();
    }

    public abstract Screen createStartScreen();

    public void setScreen(Screen screen)
    {
        if(this.screen != null)
        {
            this.screen.dispose();
        }
        this.screen = screen;
    }

    public Bitmap loadBitmap(String filename)
    {
        InputStream in = null;
        Bitmap bitmap;

        try
        {
            in = getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(in);
            if(bitmap == null)
            {
                throw new RuntimeException("*** Could not find graphics:" + filename);
            }
            return bitmap;
        }
        catch(IOException e)
        {
            throw new RuntimeException("*** Could not open graphics: " + filename);
        }
        finally
        {
            if(in != null)
            {
                try
                {
                    in.close();
                }
                catch(IOException e){}
            }
        }
    }
    //public Music loadMusic(String filename) { return null; }
    //public Sound loadSound(String filename) { return null; }

    public void clearFrameBuffer(int color)
    {
        canvas.drawColor(color);
    }
    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if(canvas != null) canvas.drawBitmap(bitmap, x, y, null);
    }

    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        Rect src = new Rect();
        Rect dst = new Rect();
        if(canvas == null) return;

        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }
    public int getTouchX(int pointer)
    {
        int virtualX = 0;
        virtualX = (int) ((float) touchHandler.getTouchX(pointer) / (float) surfaceView.getWidth() * virtualScreen.getWidth());
        return virtualX;
    }
    public int getTouchY(int pointer)
    {
        int virtualY = 0;
        virtualY = (int) ((float) touchHandler.getTouchY(pointer) / (float) surfaceView.getHeight() * virtualScreen.getHeight());
        return virtualY;
    }

    //public List<TouchEvent> getTouchEvents() { return null; }

    public void onPause()
    {
        super.onPause();
        synchronized(stateChanges)
        {
            if(isFinishing())
            {
                stateChanges.add(State.Disposed);
            }
            else
            {
                stateChanges.add(State.Paused);
            }
        }
        try
        {
            mainLoopThread.join();
        }
        catch(Exception e)
        {
            Log.d("GameEngine", "something went wrong");
        }

        if(isFinishing())
        {
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
        }
    }

    public void onResume()
    {
        super.onResume();
        synchronized(stateChanges)
        {
            stateChanges.add(State.Resumed);
        }
        mainLoopThread = new Thread(this);
        mainLoopThread.start();
    }

    @Override
    public void run()
    {
        while(true)
        {
            synchronized(stateChanges)
            {
                int stopValue = stateChanges.size();
                for(int i = 0; i < stopValue; i++)
                {
                    state = stateChanges.get(i);
                    switch(state)
                    {
                        case Disposed:
                            if(screen != null)
                            {
                                screen.dispose();
                            }
                            Log.d("GameEngine" , "Main Loop thread is disposed");
                            stateChanges.clear();
                            return;
                        case Paused:
                            if(screen != null)
                            {
                                screen.pause();
                            }
                            Log.d("GameEngine", "Main Loop thread is paused");
                            stateChanges.clear();
                            return;
                        case Resumed:
                            if(screen != null)
                            {
                                screen.resume();
                            }
                            Log.d("GameEngine", "Main Loop thread is resumed");
                            state = State.Running;
                            break;
                        default: break;
                    }
                }
                stateChanges.clear();
            }
            //After the synchronized state check we can do the actual work of the thread
            if(state == State.Running)
            {
                if(!surfaceHolder.getSurface().isValid())
                {
                    continue;
                }
                Canvas canvas = surfaceHolder.lockCanvas();
                //now we can do all the drawing stuff
                fillEvents();
                if(screen != null ) screen.update(0);
                freeEvents();
                //after the screen has made all game objects to the virtualScreen we need to copy
                //and resize the virtualScreen to the actual physical surfaceView
                src.left = 0;
                src.top = 0;
                src.right = virtualScreen.getWidth() - 1;
                src.bottom = virtualScreen.getHeight() - 1;

                dst.left = 0;
                dst.top = 0;
                dst.right = surfaceView.getWidth();
                dst.bottom = surfaceView.getHeight();

                canvas.drawBitmap(virtualScreen, src, dst, null);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
