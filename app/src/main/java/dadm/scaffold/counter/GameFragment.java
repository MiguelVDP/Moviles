package dadm.scaffold.counter;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.FramesPerSecondCounter;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameView;
import dadm.scaffold.input.GameOverDialogListener;
import dadm.scaffold.input.PauseDialog;
import dadm.scaffold.space.LivesCounter;
import dadm.scaffold.input.JoystickInputController;
import dadm.scaffold.space.GameController;
import dadm.scaffold.space.*;
import dadm.scaffold.space.SpaceShipPlayer;


public class GameFragment extends BaseFragment implements View.OnClickListener,
        PauseDialog.PauseDialogListener, GameOverDialogListener {
    private GameEngine theGameEngine;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_play_pause).setOnClickListener(this);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        final GameFragment gameFragment = this;
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                //Para evitar que sea llamado múltiples veces,
                //se elimina el listener en cuanto es llamado
                observer.removeOnGlobalLayoutListener(this);
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                theGameEngine = new GameEngine(getActivity(), gameView);
                theGameEngine.setSoundManager(getScaffoldActivity().getSoundManager());
                theGameEngine.setTheInputController(new JoystickInputController(getView()));
                theGameEngine.addGameObject(new ParalaxBackground(theGameEngine,300,R.drawable.space));
//                theGameEngine.addGameObject(new SpaceShipPlayer(theGameEngine));
                theGameEngine.addGameObject(new FramesPerSecondCounter(theGameEngine));
                theGameEngine.addGameObject(new GameController(theGameEngine, gameFragment));
                theGameEngine.addGameObject(new ScoreGameObject(view, R.id.score_value));
                theGameEngine.addGameObject(new LivesCounter(getView(), R.id.lives_value));
                theGameEngine.startGame();
            }
        });
        getScaffoldActivity().applyTypeface(view);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play_pause) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (theGameEngine.isRunning()){
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        theGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        if (theGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return super.onBackPressed();
    }

    private void pauseGameAndShowPauseDialog() {
        if(theGameEngine.isPaused()){ return; }
        theGameEngine.pauseGame();

        PauseDialog dialog = new PauseDialog(getScaffoldActivity());
        dialog.setListener(this);
        showDialog(dialog);
    }

    @Override
    public void exitGame() {
        theGameEngine.stopGame();
        getScaffoldActivity().navigateBack();
    }

    @Override
    public void startNewGame() {
        theGameEngine.stopGame();
        prepareAndStartGame();
    }

    private void prepareAndStartGame() {
        GameView gameView = (GameView)
                getView().findViewById(R.id.gameView);
        theGameEngine = new GameEngine(getActivity(), gameView);
        theGameEngine.setTheInputController(new JoystickInputController(getView()));
        theGameEngine.setSoundManager(getScaffoldActivity().getSoundManager());
        theGameEngine.addGameObject(new ParalaxBackground(theGameEngine,300,R.drawable.seamlessspace));
        theGameEngine.addGameObject(new FramesPerSecondCounter(theGameEngine));
        theGameEngine.addGameObject(new GameController(theGameEngine, this));
        theGameEngine.addGameObject(new ScoreGameObject(getView(), R.id.score_value));
        theGameEngine.addGameObject(new LivesCounter(getView(), R.id.lives_value));
        theGameEngine.startGame();
    }

    @Override
    public void resumeGame() {
        theGameEngine.resumeGame();
    }
}
