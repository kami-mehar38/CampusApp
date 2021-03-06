package abbottabad.comsats.campusapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * This project CampusApp is created by Kamran Ramzan on 01-Jan-17.
 */

public class NotificationsListFragment extends Fragment implements View.OnClickListener {

    private LinearLayout LL_from;
    private FloatingActionButton FAB_sendNotification;
    public static RecyclerView RV_notifications;
    public static EventNotificationsAdapter eventNotificationsAdapter;
    public static EditText ET_notification;
    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private SharedPreferences sharedPreferences;
    public static ImageButton sendNotification;
    public static ProgressBar isWaiting;
    private Paint p = new Paint();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RV_notifications = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_notifications.setLayoutManager(layoutManager);
        RV_notifications.addItemDecoration(new RecyclerViewDivider(getContext()));
        RV_notifications.setItemAnimator(new SlideInLeftAnimator());
        eventNotificationsAdapter = new EventNotificationsAdapter(getContext());
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(eventNotificationsAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        RV_notifications.setAdapter(scaleInAnimationAdapter);
        new EventsLocalModal(getContext()).retrieveNotifications();

        FAB_sendNotification = (FloatingActionButton) view.findViewById(R.id.FAB_sendNotification);
        FAB_sendNotification.setOnClickListener(this);
        LL_from = (LinearLayout) view.findViewById(R.id.form);
        ImageView BTN_hideForm = (ImageView) view.findViewById(R.id.BTN_hideFrom);
        BTN_hideForm.setOnClickListener(this);
        ET_notification = (EditText) view.findViewById(R.id.ET_notification);
        sendNotification = (ImageButton) view.findViewById(R.id.sendNotification);
        sendNotification.setOnClickListener(this);
        isWaiting = (ProgressBar) view.findViewById(R.id.waiting);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                eventNotificationsAdapter.onItemRemove(viewHolder, RV_notifications);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#FF0000"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_discard_notification);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(RV_notifications);
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
                ET_notification.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                hideForm(LL_from);
                break;
            }
            case R.id.sendNotification: {
                String message = ET_notification.getText().toString().trim();
                String regId = sharedPreferences.getString("REG_ID", "");
                String name = sharedPreferences.getString("NAME", "");
                if (!message.isEmpty()) {
                    new NotificationsModal(getContext()).sendEventNotification(regId, name, message);
                    ET_notification.setText("");
                } else Toast.makeText(getContext(), "Enter message", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

}
