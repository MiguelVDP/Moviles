package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.PowerUp;
import dadm.scaffold.engine.ScreenGameObject;

public class DoubleShoot extends PowerUp {
    protected DoubleShoot(GameController gc, GameEngine gameEngine) {
        super(gameEngine, R.drawable.berserkerpowerup, gc);
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }
}
