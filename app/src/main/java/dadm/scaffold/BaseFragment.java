package dadm.scaffold;

import android.support.v4.app.Fragment;

import dadm.scaffold.engine.BaseCustomDialog;


public class BaseFragment extends Fragment {
    BaseCustomDialog mCurrentDialog;
    public void showDialog (BaseCustomDialog newDialog) {
        showDialog(newDialog, false);
    }
    public void showDialog (BaseCustomDialog newDialog,
                            boolean dismissOtherDialog) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            if (dismissOtherDialog) {
                mCurrentDialog.dismiss();
            }
            else {
                return;
            }
        }
        mCurrentDialog = newDialog;
        mCurrentDialog.show();
    }
    public boolean onBackPressed() {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            mCurrentDialog.dismiss();
            return true;
        }
        return false;
    }
    protected ScaffoldActivity getScaffoldActivity () {
        return (ScaffoldActivity) getActivity();
    }
}
