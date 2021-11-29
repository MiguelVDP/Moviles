package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class EnemyBullet extends Sprite {

    private double speedFactor;

    private EnemyShip parent;

    private GameController gc;

    private GameEngine ge;

    public EnemyBullet(GameEngine gameEngine) {
        super(gameEngine, R.drawable.balaenemigos);

        speedFactor = gameEngine.pixelFactor * 300d / 1000d;

        ge=gameEngine;

        bodyType = BodyType.Rectangular;
    }

    @Override
    public void startGame() {
        gc=(GameController) ge.getObject(GameController.class);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

        positionY += speedFactor * elapsedMillis;

        if (positionY > gameEngine.height) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            parent.releaseBullet(this);
        }
    }


    public void init(EnemyShip enemyShip, double initPositionX, double initPositionY) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = enemyShip;
    }

    public void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }
}