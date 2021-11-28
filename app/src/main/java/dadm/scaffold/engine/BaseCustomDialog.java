package dadm.scaffold.engine;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;

public class BaseCustomDialog implements View.OnTouchListener {
    private boolean isShowing;
    protected final ScaffoldActivity parent;
    private ViewGroup rootLayout;
    protected View rootView;

    public BaseCustomDialog(ScaffoldActivity activity) {
        parent = activity;
    }

    protected void onViewClicked() {
// Ignore clicks on this view
    }

    protected void setContentView(int dialogResId) {
        ViewGroup activityRoot = (ViewGroup)
                parent.findViewById(android.R.id.content);
        rootView = LayoutInflater.from(parent).inflate(dialogResId,
                activityRoot, false);
        parent.applyTypeface(rootView);
    }

    public void show() {
        if (isShowing) {
            return;
        }
        isShowing = true;
        ViewGroup activityRoot = (ViewGroup)
                parent.findViewById(android.R.id.content);
        rootLayout = (ViewGroup)
                LayoutInflater.from(parent).inflate(
                        R.layout.my_overlay_dialog, activityRoot, false);
        activityRoot.addView(rootLayout);
        rootLayout.setOnTouchListener(this);
        rootLayout.addView(rootView);
    }

    public void dismiss() {
        if (!isShowing) {
            return;
        }
        isShowing = false;
        hideViews();
    }

    private void hideViews() {
        rootLayout.removeView(rootView);
        ViewGroup activityRoot = (ViewGroup)
                parent.findViewById(android.R.id.content);
        activityRoot.removeView(rootLayout);
    }

    protected View findViewById(int id) {
        return rootView.findViewById(id);
    }



    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
// Ignoring touch events on the gray outside
        return true;
    }
}