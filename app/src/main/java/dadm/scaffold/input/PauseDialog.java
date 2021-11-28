package dadm.scaffold.input;

import android.view.View;
import android.widget.ImageView;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.BaseCustomDialog;
import dadm.scaffold.sound.SoundManager;

public class PauseDialog extends BaseCustomDialog
        implements View.OnClickListener {
    private PauseDialogListener mListener;
    public PauseDialog(ScaffoldActivity activity) {
        super(activity);
        setContentView(R.layout.pause_dialog);
        findViewById(R.id.btn_music).setOnClickListener(this);
        findViewById(R.id.btn_sound).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
        updateSoundAndMusicButtons();
    }
    public void setListener(PauseDialogListener listener) {
        mListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sound) {
            parent.getSoundManager().playPauseSfx();
            updateSoundAndMusicButtons();
        }
        else if (v.getId() == R.id.btn_music) {
            parent.getSoundManager().playPauseMusic();
            updateSoundAndMusicButtons();
        }
        else if (v.getId() == R.id.btn_exit) {
            super.dismiss();
            mListener.exitGame();
        }
        else if (v.getId() == R.id.btn_resume) {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mListener.resumeGame();
    }
    public void updateSoundAndMusicButtons() {
        SoundManager soundManager = parent.getSoundManager();

        ImageView btnMusic = (ImageView) rootView.findViewById(R.id.btn_music);

        if(soundManager.musicOn){
            btnMusic.setImageResource(R.drawable.music);
        }else{
            btnMusic.setImageResource(R.drawable.mute_music);
        }

        ImageView btnSfx = (ImageView) rootView.findViewById(R.id.btn_sound);
        if(soundManager.sfxOn){
            btnSfx.setImageResource(R.drawable.sfx);
        }else{
            btnSfx.setImageResource(R.drawable.mute_sfx);
        }
    }
    public interface PauseDialogListener {
        void exitGame();
        void resumeGame();
    }
}