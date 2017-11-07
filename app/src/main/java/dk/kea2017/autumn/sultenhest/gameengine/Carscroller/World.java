package dk.kea2017.autumn.sultenhest.gameengine.Carscroller;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dk.kea2017.autumn.sultenhest.gameengine.GameEngine;

public class World
{
    public static final float MIN_X = 0;
    public static final float MAX_X = 479;
    public static final float MIN_Y = 28;
    public static final float MAX_Y = 319 - 28;

    Car car = new Car();
    List<Monster> monsterList = new ArrayList<>();
    public int maxMonsters = 3;

    GameEngine gameEngine;
    CollisionListener collisionListener;

    boolean gameOver = false;
    int points = 0;
    int lives = 3;

    public World(GameEngine gameEngine, CollisionListener collisionListener)
    {
        this.gameEngine = gameEngine;
        this.collisionListener = collisionListener;
        initializeMonsters();
    }

    public void update(float deltatime, float accelY)
    {
        //Move the car based on the phone accelerometer in y-axis
        //car.y = (int)(car.y + accelY * deltatime * 50);
        car.y = (int)(car.y - accelY * deltatime * 50);

        //Car moves with the touch ONLY for testing purposes
        if(gameEngine.isTouchDown(0))
        {
            if(gameEngine.getTouchX(0) > 100)
            {
                car.y = gameEngine.getTouchY(0) - Car.HEIGHT;
            }
        }

        //Check if car touches top side of the road
        if (car.y < MIN_Y) car.y = (int)MIN_Y;
        //Check if car touches bottom size of the road
        if (car.y + Car.HEIGHT > MAX_Y) car.y = (int)(MAX_Y - Car.HEIGHT);

        //move the monsters
        Monster monster = null;
        for(int i = 0; i < maxMonsters; i++)
        {
            monster = monsterList.get(i);
            monster.x = (int)(monster.x - 100 * deltatime);
            if(monster.x < 0 - monster.WIDTH)
            {
                Random random = new Random();
                monster.x = 500 + random.nextInt(100);
                monster.y = 28  + random.nextInt(235);
            }
        }

        collideCarMonster(deltatime);
    }

    private void collideCarMonster(float deltatime)
    {
        Monster monster = null;
        for (int i = 0; i < maxMonsters; i++)
        {
            monster = monsterList.get(i);

            if (collideRects(car.x, car.y, Car.WIDTH, Car.HEIGHT, monster.x, monster.y, Monster.WIDTH, Monster.HEIGHT))
            {
                gameOver = true;
                collisionListener.gameOver();
            }
        }
    }

    // Refactor this piece of shit
    private boolean collideRects(float x1, float y1, float width1, float height1,
                                 float x2, float y2, float width2, float height2)
    {
        Rect rect1 = new Rect((int) x1, (int)y1, (int)x1+(int)width1, (int)y1+(int)height1);
        Rect rect2 = new Rect((int) x2, (int)y2, (int)x2+(int)width2, (int)y2+(int)height2);

        return rect1.intersect(rect2);

        /*
        // Teacher's implementation
        if (x < x2+width2 && x + width > x2 && y + height > y2 && y < y2 + height2)
        {
            return true;
        }
        return false;
        */
    }

    private void initializeMonsters()
    {
        Random random = new Random();

        for(int i = 0; i < maxMonsters; i++)
        {
            int randX = random.nextInt(50);
            int randY = random.nextInt(235);

            Monster monster = new Monster((500 + randX) + i * 100, 28 + randY);

            monsterList.add(monster);
        }
    }
}