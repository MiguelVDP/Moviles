package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;

public class SpaceShipPlayer extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 12;
    private static final long TIME_BETWEEN_BULLETS = 375;
    private static final long DOUBLE_SHOOT_TIMEOUT = 5000;
    List<Bullet> bullets = new ArrayList<Bullet>();
    private long timeSinceLastFire;
    private long timeSincePowerUp;

    private int maxX;
    private int maxY;
    private double speedFactor;
    private boolean doubleShoot;


    public SpaceShipPlayer(GameEngine gameEngine){
        super(gameEngine, R.drawable.xwing);
        speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;
        bodyType = BodyType.Circular;

        initBulletPool(gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    private void doubleShootOn() {
        doubleShoot = true;
        timeSincePowerUp = 0;
    }

    @Override
    public void startGame() {
        positionX = maxX / 2;
        positionY = maxY / 2;
        doubleShoot = false;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }

        if(doubleShoot){
            if(timeSincePowerUp > DOUBLE_SHOOT_TIMEOUT){
                doubleShoot = false;
            }else{
                timeSincePowerUp += elapsedMillis;
            }
        }
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            Bullet bullet = getBullet();
            Bullet bullet2= getBullet();
            if (bullet == null || bullet2==null) {
                return;
            }
            double angle = 0;
            if(gameEngine.theInputController.isFiring){
                angle = 3.14159;
            }
            bullet.init(this, positionX+7 , positionY, angle);
            bullet2.init(this, positionX + width -7 , positionY, -angle);
            if(doubleShoot){
                Bullet b1 = getBullet();
                Bullet b2 = getBullet();
                b1.init(this, positionX + 30, positionY, angle);
                b2.init(this, positionX + width - 30, positionY, angle);
                gameEngine.addGameObject(b1);
                gameEngine.addGameObject(b2);
            }
            gameEngine.addGameObject(bullet);
            gameEngine.addGameObject(bullet2);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            gameEngine.removeGameObject(this);
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }else if(otherObject instanceof EnemyShip){
            gameEngine.removeGameObject(this);
            EnemyShip e = (EnemyShip) otherObject;
            e.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }else if(otherObject instanceof EnemyBullet){
            gameEngine.removeGameObject(this);
            EnemyBullet b = (EnemyBullet) otherObject;
            b.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }else if(otherObject instanceof  DoubleShoot){
            DoubleShoot d = (DoubleShoot) otherObject;
            d.removeObject(gameEngine);
            doubleShootOn();
        }
    }


}
