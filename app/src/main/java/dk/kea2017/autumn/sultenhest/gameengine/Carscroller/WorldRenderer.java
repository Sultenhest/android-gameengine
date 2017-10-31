package dk.kea2017.autumn.sultenhest.gameengine.Carscroller;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;

    Bitmap carImage;

    public WorldRenderer(GameEngine gameEngine, World world)
    {
        this.gameEngine = gameEngine;
        this.world = world;

        carImage = gameEngine.loadBitmap("carscroller_assets/xbluecar2.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(carImage, world.car.x, world.car.y);
    }
}
