package dadm.scaffold.space;

import android.graphics.Canvas;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.counter.GameFragment;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;
import dadm.scaffold.engine.PowerUp;
import dadm.scaffold.input.GameOverDialog;
import dadm.scaffold.sound.GameControllerState;
import dadm.scaffold.sound.GameEvent;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ENEMIES = 1000; //ms
    private long currentMillis;
    private List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private List<EnemyShip> enemyShipsPool = new ArrayList<EnemyShip>();
    private List<PowerUp> lifePwrUpPool = new ArrayList<PowerUp>();
    private List<PowerUp> dmgPwrUpPool = new ArrayList<PowerUp>();
    private List<PowerUp> cashPwrUpPool = new ArrayList<PowerUp>();
    private int enemiesSpawned;
    private TextView scoreText;
    private long waitingTime;
    private static final int INITIAL_LIFES = 4;
    private static final long STOPPING_WAVE_WAITING_TIME = 2000;
    private GameEngine gE;
    private GameControllerState state;
    private int numLives = 0;
    private GameFragment parentFrag;
    private int enemyCounter;

    private int score;

    public GameController(GameEngine gameEngine, GameFragment fragment) {
        this.gE = gameEngine;
        parentFrag = fragment;
        // We initialize the pool of items now
        for (int i = 0; i < 10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine));
        }
        for (int i = 0; i < 5; i++){
            enemyShipsPool.add(new EnemyShip(this, gameEngine));
        }

        for (int i = 0; i < 3; i++){
            lifePwrUpPool.add(new LifeUp(this, gameEngine));
            dmgPwrUpPool.add(new DoubleShoot(this, gameEngine));
            cashPwrUpPool.add(new CreditBoost(this, gameEngine));
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
                if(enemyCounter < 5) {
                    Asteroid a = asteroidPool.remove(0);
                    a.init(gameEngine);
                    gameEngine.addGameObject(a);
                    enemyCounter++;
                }else{
                    EnemyShip e = enemyShipsPool.remove(0);
                    e.init(gameEngine);
                    gameEngine.addGameObject(e);
                    enemyCounter = 0;
                }
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
                GameOverDialog quitDialog = new GameOverDialog(parentFrag.getScaffoldActivity(), score);
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

    public void returnToEnemyPool(EnemyShip enemyShip) {
        enemyShipsPool.add(enemyShip);
    }

    public void returnToPowerUpPool(PowerUp pwUp) {
        if(pwUp instanceof LifeUp){
            lifePwrUpPool.add(pwUp);
        }else if(pwUp instanceof DoubleShoot){
            dmgPwrUpPool.add(pwUp);
        }else if(pwUp instanceof CreditBoost){
            cashPwrUpPool.add(pwUp);
        }
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
        } else if(gameEvent == GameEvent.PowerUpDropped){
//            dropPowerUp();
        }
    }

    public void dropPowerUp(double posX, double posY){
        int num = gE.random.nextInt(3);
        switch (num){
            case 0:
                if(lifePwrUpPool.isEmpty()){break;}
                LifeUp a = (LifeUp) lifePwrUpPool.remove(0);
                a.init(posX, posY);
                gE.addGameObject(a);
                break;
            case 1:
                if(dmgPwrUpPool.isEmpty()){break;}
                DoubleShoot d = (DoubleShoot) dmgPwrUpPool.remove(0);
                d.init(posX, posY);
                gE.addGameObject(d);
                break;
            case 2:
                if(cashPwrUpPool.isEmpty()){break;}
                CreditBoost c = (CreditBoost) cashPwrUpPool.remove(0);
                c.init(posX, posY);
                gE.addGameObject(c);
                break;
        }
    }

    public void updateScore(int points) {
        score = points;
    }
}

