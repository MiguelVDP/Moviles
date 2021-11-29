package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class EnemyShip extends Sprite {

    private final GameController gameController;
    private double speed;
    private double speedX;
    private double speedY;
    private double correctionFactor = 1;

    private int lives = 3;

    private int bounces;
    final int MAX_BOUNCES = 4;

    private boolean spawning;

    private List<EnemyBullet> bulletPool = new ArrayList<EnemyBullet>();
    private static final int INITIAL_BULLET_POOL_AMOUNT = 4;
    private static final long TIME_BETWEEN_BULLETS = 1000;
    private long timeSinceLastFire = 0;

    protected EnemyShip(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.tiefighter);
        this.speed = 200d * pixelFactor / 1000d;
        this.gameController = gameController;
        bodyType = BodyType.Circular;
        initialiceBulletPool(gameEngine);
    }

    private void initialiceBulletPool(GameEngine gameEngine) {
        for (int i = 0; i < INITIAL_BULLET_POOL_AMOUNT; i++) {
            bulletPool.add(new EnemyBullet(gameEngine));
        }
    }

    public void init(GameEngine gameEngine) {

        double angle = 0;
        if (gameEngine.random.nextInt(2) == 1) {
            angle = 0.785398;
        } else {
            angle = -0.785398;
        }
        speedX = speed * Math.sin(angle);
        speedY = speed * Math.cos(angle);
        // Asteroids initialize in the central 50% of the screen horizontally
        positionX = gameEngine.random.nextInt(gameEngine.width / 2) + gameEngine.width / 4;
        positionY = -height;
        spawning = true;
        bounces = 0;
        lives = 3;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;

        checkFiring(elapsedMillis, gameEngine);

        if (bounces < MAX_BOUNCES) {
            if (positionY + height > gameEngine.height) {
                speedY = -speedY;
                positionY -= correctionFactor;
                bounces++;
            }
            if (spawning) {
                if (positionY > 0.1) {
                    spawning = false;
                }
            } else if (positionY < 0.1) {
                speedY = -speedY;
                positionY += correctionFactor;
                bounces++;
            }
            if (positionX + width > gameEngine.width) {
                speedX = -speedX;
                positionX -= correctionFactor;
                bounces++;
            } else if (positionX < 0.1) {
                speedX = -speedX;
                positionX += correctionFactor;
                bounces++;
            }
        } else {
            if (positionY > gameEngine.height) {
                objectOutOfBounds(gameEngine);
            } else if (positionY + height < 0.1) {
                objectOutOfBounds(gameEngine);
            }
            if (positionX > gameEngine.width) {
                objectOutOfBounds(gameEngine);
            } else if (positionX + width < 0.1) {
                objectOutOfBounds(gameEngine);
            }
        }
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            EnemyBullet bullet = getBullet();
            if (bullet == null) {
                return;
            }

            bullet.init(this, positionX + width / 2, positionY + height);

            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.TieFighterFire);
        }else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    private EnemyBullet getBullet() {
        if (bulletPool.isEmpty()) {
            return null;
        }
        return bulletPool.remove(0);
    }

    private void objectOutOfBounds(GameEngine gameEngine) {
        gameEngine.onGameEvent(GameEvent.AsteroidMissed);
        // Return to the pool
        gameEngine.removeGameObject(this);
        //In case of enemy spaceships it is also recommended to return them into the pull back again
        gameController.returnToEnemyPool(this);

        gameEngine.onGameEvent(GameEvent.EnemyShipMissed);
    }

    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
        gameController.returnToEnemyPool(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Bullet) {
            lives--;
            if (lives <= 0) {
                removeObject(gameEngine);
                gameEngine.onGameEvent(GameEvent.EnemyShipKilled);
            }
            Bullet b = (Bullet) otherObject;
            b.removeObject(gameEngine);
        }
    }

    public void releaseBullet(EnemyBullet enemyBullet) {
        bulletPool.add(enemyBullet);
    }
}
