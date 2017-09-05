package dk.kea2017.autumn.sultenhest.gameengine;

public class TestGame extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        return new TestScreen(this);
    }
}
