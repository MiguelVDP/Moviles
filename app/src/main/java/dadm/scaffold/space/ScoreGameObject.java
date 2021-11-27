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
    private static final int POINTS_LOSS_PER_ASTEROID_MISSED = 1;
    private static final int POINTS_GAINED_PER_ASTEROID_HIT = 50;
    public ScoreGameObject(View view, int viewResId) {
        scoreText = (TextView) view.findViewById(viewResId);
    }
    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {}
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
        }
        else if (gameEvent == GameEvent.AsteroidMissed) {
            if (points > 0) {
                points -= POINTS_LOSS_PER_ASTEROID_MISSED;
            }
            pointsHaveChanged = true;
        }
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