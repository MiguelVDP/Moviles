package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.PowerUp;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.sound.GameEvent;

public class LifeUp extends PowerUp {
    protected LifeUp(GameController gc, GameEngine gameEngine) {
        super(gameEngine, R.drawable.repair, gc);
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if(otherObject instanceof SpaceShipPlayer){
            removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.LifeAdded);
        }
    }
}
