package abbottabad.comsats.campusapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * This project CampusApp is created by Kamran Ramzan on 01-Jan-17.
 */

public class NotificationsListFragment extends Fragment implements View.OnClickListener {

    private LinearLayout LL_from;
    private FloatingActionButton FAB_sendNotification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FAB_sendNotification = (FloatingActionButton) view.findViewById(R.id.FAB_sendNotification);
        FAB_sendNotification.setOnClickListener(this);
        LL_from = (LinearLayout) view.findViewById(R.id.form);
        ImageView BTN_hideForm = (ImageView) view.findViewById(R.id.BTN_hideFrom);
        BTN_hideForm.setOnClickListener(this);
        //hideForm(LL_from);
    }

    void hideForm(final View view) {
        view.animate()
                .translationY(view.getHeight())
                .setDuration(300)
                .alpha(0.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                showFab(FAB_sendNotification);
            }
        });
    }

    void showFrom(final View view) {
        view.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    void hideFab(final View view) {
        view.animate()
                .translationY(view.getHeight())
                .setDuration(300)
                .alpha(0.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                showFrom(LL_from);
            }
        });
    }

    void showFab(final View view) {
        view.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }

                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FAB_sendNotification: {
                hideFab(FAB_sendNotification);
                break;
            }
            case R.id.BTN_hideFrom: {
                hideForm(LL_from);
                break;
            }
        }
    }
}
