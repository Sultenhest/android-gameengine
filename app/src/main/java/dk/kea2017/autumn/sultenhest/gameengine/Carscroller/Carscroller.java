package dk.kea2017.autumn.sultenhest.gameengine.Carscroller;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Screen;

public class Carscroller extends GameEngine
{
    @Override
    public Screen createStartScreen() {
        music = this.loadMusic("music.ogg");
        return new MainMenuScreen(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        music.pause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        music.play();
    }

}
