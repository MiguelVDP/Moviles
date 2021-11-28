package dadm.scaffold.counter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.input.QuitDialog;
import dadm.scaffold.sound.SoundManager;


public class MainMenuFragment extends BaseFragment implements View.OnClickListener, QuitDialog.QuitDialogListener {
    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
        view.findViewById(R.id.btn_music).setOnClickListener(this);
        view.findViewById(R.id.btn_sound).setOnClickListener(this);
        updateSoundAndMusicButtons();
        getScaffoldActivity().applyTypeface(view);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_start) {
            ((ScaffoldActivity) getActivity()).startGame();
        }else if(v.getId() == R.id.btn_music){
            SoundManager soundManager = getScaffoldActivity().getSoundManager();
            soundManager.playPauseMusic();
            updateSoundAndMusicButtons();
        }else if(v.getId() == R.id.btn_sound){
            SoundManager soundManager = getScaffoldActivity().getSoundManager();
            soundManager.playPauseSfx();
            updateSoundAndMusicButtons();
        }
    }

    private void updateSoundAndMusicButtons() {
        SoundManager soundManager = getScaffoldActivity().getSoundManager();

        ImageView btnMusic = (ImageView) getView().findViewById(R.id.btn_music);
        if(soundManager.musicOn){
            btnMusic.setImageResource(R.drawable.music);
        }else{
            btnMusic.setImageResource(R.drawable.mute_music);
        }

        ImageView btnSfx = (ImageView) getView().findViewById(R.id.btn_sound);
        if(soundManager.sfxOn){
            btnSfx.setImageResource(R.drawable.sfx);
        }else{
            btnSfx.setImageResource(R.drawable.mute_sfx);
        }
    }

    @Override
    public boolean onBackPressed() {
        boolean consumed = super.onBackPressed();
        if (!consumed){
            QuitDialog quitDialog = new QuitDialog(getScaffoldActivity());
            quitDialog.setListener(this);
            showDialog(quitDialog);
        }
        return true;
    }

    @Override
    public void exit() {
        getScaffoldActivity().finish();
    }
}
