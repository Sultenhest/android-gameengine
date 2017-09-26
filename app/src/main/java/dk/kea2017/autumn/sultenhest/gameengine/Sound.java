package dk.kea2017.autumn.sultenhest.gameengine;

import android.media.SoundPool;

public class Sound
{
    int soundId;
    SoundPool soundPool;

    public Sound(int soundId, SoundPool soundPool)
    {
        this.soundId   = soundId;
        this.soundPool = soundPool;
    }

    public void play(float volume)
    {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    public void dispose()
    {
        soundPool.unload(soundId);
    }
}
