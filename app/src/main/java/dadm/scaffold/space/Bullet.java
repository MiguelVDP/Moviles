package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class Bullet extends Sprite {

    private double speedFactor;

    private double speedX, speedY;

    private SpaceShipPlayer parent;

    private GameController gc;

    private GameEngine ge;

    public Bullet(GameEngine gameEngine){
        super(gameEngine, R.drawable.xwingbullet);

        speedFactor = gameEngine.pixelFactor * -300d / 1000d;

        ge=gameEngine;

        bodyType = BodyType.Rectangular;
    }

    @Override
    public void startGame() {
        gc=(GameController) ge.getObject(GameController.class);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

            positionX += speedX * elapsedMillis;
            positionY += speedY * elapsedMillis;

        if ( positionY < 0.1 || positionY > gameEngine.height) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            parent.releaseBullet(this);
        }
    }


    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY,
                     double angle) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;

        speedX = speedFactor * Math.sin(angle);
        speedY = speedFactor * Math.cos(angle);

        parent = parentPlayer;
    }

    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {

            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);

            // Add some score

            //gc.addScore();
        }
    }
}
