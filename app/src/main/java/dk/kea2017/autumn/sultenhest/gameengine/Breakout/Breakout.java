package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;
import dk.kea2017.autumn.sultenhest.gameengine.Screen;

public class Breakout extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        return new MainMenuScreen(this);
    }
}
