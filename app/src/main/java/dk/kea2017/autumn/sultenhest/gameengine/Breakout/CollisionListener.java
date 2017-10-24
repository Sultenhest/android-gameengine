package dk.kea2017.autumn.sultenhest.gameengine.Breakout;

public interface CollisionListener
{
    public void collisionWall();
    public void collisionPaddle();
    public void collisionBlock();
}
