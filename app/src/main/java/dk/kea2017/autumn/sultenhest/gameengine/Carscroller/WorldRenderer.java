package dk.kea2017.autumn.sultenhest.gameengine.Carscroller;

import android.graphics.Bitmap;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;

    Bitmap carImage;
    Bitmap monsterImage;

    public WorldRenderer(GameEngine gameEngine, World world)
    {
        this.gameEngine = gameEngine;
        this.world = world;

        carImage = gameEngine.loadBitmap("carscroller_assets/xbluecar2.png");
        monsterImage = gameEngine.loadBitmap("carscroller_assets/xyellowmonster2.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(carImage, world.car.x, world.car.y);

        for(int i = 0; i < world.maxMonsters; i++)
        {
            gameEngine.drawBitmap(monsterImage, world.monsterList.get(i).x, world.monsterList.get(i).y);
        }
    }
}
