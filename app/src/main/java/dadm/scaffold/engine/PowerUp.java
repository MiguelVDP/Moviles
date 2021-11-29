package dadm.scaffold.engine;

import dadm.scaffold.space.GameController;

public abstract class PowerUp extends Sprite{

    protected double speed;

    protected GameController gameController;

    protected PowerUp(GameEngine gameEngine, int drawableRes, GameController gc) {
        super(gameEngine, drawableRes);
        speed = 100 * pixelFactor/1000d;
        gameController = gc;
    }

    public void init(double posX, double posY){
        positionX = posX;
        positionY = posY;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY += speed * elapsedMillis;

        if(positionY > gameEngine.height){
            removeObject(gameEngine);
        }
    }

    public void removeObject(GameEngine gameEngine){
        gameEngine.removeGameObject(this);
        gameController.returnToPowerUpPool(this);
    }
}
