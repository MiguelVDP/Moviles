package dadm.scaffold.input;

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
        txt.setText("Your score: " + score);
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
