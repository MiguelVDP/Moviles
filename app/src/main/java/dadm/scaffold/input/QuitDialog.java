package dadm.scaffold.input;

import android.view.View;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.BaseCustomDialog;

public class QuitDialog extends BaseCustomDialog implements View.
        OnClickListener{
    private QuitDialogListener mListener;
    public QuitDialog(ScaffoldActivity activity) {
        super(activity);
        setContentView(R.layout.dialog_quit);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
    }
    public void setListener(QuitDialogListener listener) {
        mListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_exit) {
            dismiss();
            mListener.exit();
        }
        else if (v.getId() == R.id.btn_resume) {
            dismiss();
        }
    }
    public interface QuitDialogListener {
        void exit();
    }
}