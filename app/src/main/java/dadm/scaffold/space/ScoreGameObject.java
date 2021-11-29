package dadm.scaffold.space;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;
import dadm.scaffold.sound.GameEvent;

public class ScoreGameObject extends GameObject {
    private TextView scoreText;
    private int points;
    private boolean pointsHaveChanged;
    private static final int POINTS_LOSS_PER_ASTEROID_MISSED = 5;
    private static final int POINTS_LOSS_PER_SHIP_MISSED = 50;
    private static final int POINTS_GAINED_PER_ASTEROID_HIT = 30;
    private static final int POINTS_GAINED_PER_SHIP_DESTROYED = 100;
    private GameController gameController;

    public ScoreGameObject(View view, int viewResId, GameController gameController) {
        scoreText = (TextView) view.findViewById(viewResId);
        this.gameController = gameController;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
    }

    @Override
    public void startGame() {
        points = 0;
        scoreText.post(mUpdateTextRunnable);
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.AsteroidHit) {
            points += POINTS_GAINED_PER_ASTEROID_HIT;
            pointsHaveChanged = true;
        } else if (gameEvent == GameEvent.AsteroidMissed) {
            points -= POINTS_LOSS_PER_ASTEROID_MISSED;
            if(points < 0){ points = 0; }

            pointsHaveChanged = true;
        } else if (gameEvent == GameEvent.EnemyShipKilled) {
            points += POINTS_GAINED_PER_SHIP_DESTROYED;
            pointsHaveChanged = true;
        } else if (gameEvent == GameEvent.EnemyShipMissed) {
            points -= POINTS_LOSS_PER_SHIP_MISSED;
            if(points < 0){ points = 0; }

            pointsHaveChanged = true;
        }

        gameController.updateScore(points);
    }

    private Runnable mUpdateTextRunnable = new Runnable() {
        @Override
        public void run() {
            String text = String.format("%06d", points);
            scoreText.setText(text);
        }
    };


    @Override
    public void onDraw(Canvas canvas) {
        if (pointsHaveChanged) {
            scoreText.post(mUpdateTextRunnable);
            pointsHaveChanged = false;
        }
    }
}