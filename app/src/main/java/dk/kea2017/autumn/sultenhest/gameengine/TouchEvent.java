package dk.kea2017.autumn.sultenhest.gameengine;

public class TouchEvent
{
    public enum TouchEventType
    {
        Down,
        Up,
        Dragged
    }

    public TouchEventType type; //the type of event
    public int x;               //the x-coordinate of the event
    public int y;               //the y-coordinate of the event
    public int pointer;         //the pointer id(from android system)
}
