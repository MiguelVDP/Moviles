package dadm.scaffold.input;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.BaseCustomDialog;

public class GameOverDialog extends BaseCustomDialog implements View.OnClickListener {
    private GameOverDialogListener mListener;
    public GameOverDialog(ScaffoldActivity activity, int score) {
        super(activity);
        setContentView(R.layout.game_over_dialog);
        findViewById(R.id.btn_replay).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        TextView txt = (TextView) findViewById(R.id.score_text);
        txt.setText("YOUR SCORE: " + score);
        TextView hsTxt = (TextView) findViewById(R.id.high_score_text);
        SharedPreferences preferences = activity.getSharedPreferences("score", Context.MODE_PRIVATE);
        int highScore = preferences.getInt("highScore", 0);
        if(score > highScore){
            highScore = score;
            SharedPreferences.Editor objEditor = preferences.edit();
            objEditor.putInt("highScore", score);
            objEditor.commit();
        }
        hsTxt.setText("HIGH SCORE: " + highScore);
    }
    public void setListener(GameOverDialogListener listener) {
        mListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_exit) {
            dismiss();
            mListener.exitGame();
        }
        else if (v.getId() == R.id.btn_replay) {
            mListener.startNewGame();
            dismiss();
        }
    }
}
