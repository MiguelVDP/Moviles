package dadm.scaffold.space;

import android.graphics.Canvas;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.counter.GameFragment;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;
import dadm.scaffold.input.GameOverDialog;
import dadm.scaffold.sound.GameControllerState;
import dadm.scaffold.sound.GameEvent;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ENEMIES = 1000; //ms
    private long currentMillis;
    private List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private int enemiesSpawned;
    private TextView scoreText;
    private long waitingTime;
    private static final int INITIAL_LIFES = 1;
    private static final long STOPPING_WAVE_WAITING_TIME = 2000;
    private GameEngine gE;
    private GameControllerState state;
    private int numLives = 0;
    private GameFragment parentFrag;

    private int score;

    public GameController(GameEngine gameEngine, GameFragment fragment) {
        this.gE = gameEngine;
        parentFrag = fragment;
        // We initialize the pool of items now
        for (int i = 0; i < 10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine));
        }
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        enemiesSpawned = 0;
        waitingTime = 0;


        for (int i = 0; i < INITIAL_LIFES; i++) {
            gE.onGameEvent(GameEvent.LifeAdded);
        }

        state = GameControllerState.PlacingSpaceship;

        //Score a 0 cuando comienza una partida
        score = 0;
        //scoreText=(TextView)findViewById(R.id);
    }

    //In this update, we can add enemy ships
    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (state == GameControllerState.SpawningEnemies) {
            currentMillis += elapsedMillis;

            long waveTimestamp = enemiesSpawned * TIME_BETWEEN_ENEMIES; //spawn time for enemies
            if (currentMillis > waveTimestamp) { //if current time is greater than spawn time, then spawn
                // Spawn a new enemy
                Asteroid a = asteroidPool.remove(0);
                a.init(gameEngine);
                gameEngine.addGameObject(a);
                enemiesSpawned++;
                return;
            }
        } else if (state == GameControllerState.StoppingWave) {
            waitingTime += elapsedMillis;
            if (waitingTime > STOPPING_WAVE_WAITING_TIME) {
                state = GameControllerState.PlacingSpaceship;
            }
        } else if (state == GameControllerState.PlacingSpaceship) {
            if (numLives == 0) {
                gameEngine.onGameEvent(GameEvent.GameOver);
            } else {
                numLives--;
                gameEngine.onGameEvent(GameEvent.LifeLost);
                SpaceShipPlayer newLife = new SpaceShipPlayer(gameEngine);
                gameEngine.addGameObject(newLife);
                newLife.startGame();
                // We wait to start spawning more enemies
                state = GameControllerState.Waiting;
                waitingTime = 0;
            }
        } else if (state == GameControllerState.Waiting) {
            waitingTime += elapsedMillis;
            if (waitingTime > STOPPING_WAVE_WAITING_TIME) {
                state = GameControllerState.SpawningEnemies;
            }
        }

    }

    private void showGameOverDialog() {
        parentFrag.getScaffoldActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameOverDialog quitDialog = new GameOverDialog(parentFrag.getScaffoldActivity());
                quitDialog.setListener(parentFrag);
                parentFrag.showDialog(quitDialog);
            }
        });
    }


    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(Asteroid asteroid) {
        asteroidPool.add(asteroid);
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        super.onGameEvent(gameEvent);
        if (gameEvent == GameEvent.SpaceshipHit) {
            state = GameControllerState.StoppingWave;
            waitingTime = 0;
        } else if (gameEvent == GameEvent.GameOver) {
            state = GameControllerState.GameOver;
            gE.pauseGame();
            showGameOverDialog();
        } else if (gameEvent == GameEvent.LifeAdded) {
            numLives++;
        }
    }
}

