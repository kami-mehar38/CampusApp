package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/20/16.
 */
public class NotificationsView extends AppCompatActivity implements View.OnLongClickListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    public static ListView listView;
    public static ImageButton btnSend;
    public static EditText ET_message;
    public static ProgressBar progressBar;
    public static NotificationsAdapter notificationsAdapter;
    public static boolean IS_IN_ACTION_MODE = false;
    private String REG_ID;
    private Toolbar toolbar;
    private int counter = 0;
    private ArrayList<Integer> selectedItems;
    private ArrayList<NotificationInfo> notificationInfos;
    private TextView notificationTitle;
    private NotificationsLocalModal notificationsLocalModal;
    private CheckBox CB_selectAll;
    public static boolean IS_IN_SELECT_ALL_MODE = false;
    private SharedPreferences sharedPreferences;
    private boolean areRequestsOpen;
    private String notificationType;
    public static RecyclerView recyclerView;
    public static PendingGroupRequestsAdapter pendingGroupRequestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);
        toolbar = (Toolbar) findViewById(R.id.notifications_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        REG_ID = sharedPreferences.getString("REG_ID", null);
        notificationsLocalModal = new NotificationsLocalModal(this);
        IS_IN_ACTION_MODE = false;
        IS_IN_SELECT_ALL_MODE = false;

        notificationType = sharedPreferences.getString("NOTIFICATION_TYPE", null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(notificationType + "_COUNT", 0);
        editor.putBoolean("IS_CHAT_OPEN", true);
        editor.apply();

        String groupPrivacy = sharedPreferences.getString(notificationType + "_IS", null);
        boolean isCreatedByMe = sharedPreferences.getBoolean(notificationType + "_CREATED_BY_ME", false);

        ImageView imageView = (ImageView) findViewById(R.id.IV_groupPicture);
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        if (imageView != null) {
            ImageLoader.getInstance().displayImage("http://hostellocator.com/images/" + notificationType + ".JPG", imageView);
        }

        listView = (ListView) findViewById(R.id.LV_notifications);
        btnSend = (ImageButton) findViewById(R.id.sendNotification);
        ET_message = (EditText) findViewById(R.id.ET_notification);
        notificationTitle = (TextView) findViewById(R.id.notificationTitle);
        notificationTitle.setText(sharedPreferences.getString("NOTIFICATION_TYPE", ""));
        CB_selectAll = (CheckBox) findViewById(R.id.CB_selectAll);
        if (CB_selectAll != null) {
            CB_selectAll.setVisibility(View.GONE);
        }

        progressBar = (ProgressBar) findViewById(R.id.waiting);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        notificationsAdapter = new NotificationsAdapter(NotificationsView.this, R.layout.chat_right);
        listView.setAdapter(notificationsAdapter);
        notificationsLocalModal.retrieveNotifications(sharedPreferences.getString("NOTIFICATION_TYPE", null));
        selectedItems = new ArrayList<>();
        notificationInfos = new ArrayList<>();
        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ET_message.getText().toString().trim().equals("")) {
                    Toast.makeText(NotificationsView.this, "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    new NotificationsModal(NotificationsView.this).
                            sendEventNotification(REG_ID, ET_message.getText().toString().trim(),
                                    sharedPreferences.getString("NAME", null),
                                    sharedPreferences.getString("NOTIFICATION_TYPE", null));
                }
            }
        });

        CB_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    IS_IN_SELECT_ALL_MODE = true;
                    notificationsAdapter.notifyDataSetChanged();
                    notificationTitle.setText(notificationsAdapter.getCount() + " items selected");
                } else {
                    IS_IN_SELECT_ALL_MODE = false;
                    notificationsAdapter.notifyDataSetChanged();
                    notificationTitle.setText("0 item selected");
                }
            }
        });

        managePrivacyLayout(groupPrivacy, isCreatedByMe);

        final LinearLayout pendingRequestsLayout = (LinearLayout) findViewById(R.id.pendingRequestsLayout);
        if (pendingRequestsLayout != null) {
            pendingRequestsLayout.setVisibility(View.GONE);
        }
        final Animation bottomToTop = AnimationUtils.loadAnimation(this,
                R.anim.bottom_to_top);
        final Animation topToBottom = AnimationUtils.loadAnimation(this,
                R.anim.top_to_bottom);
        topToBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (pendingRequestsLayout != null) {
                    pendingRequestsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.RV_pendingRequests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pendingGroupRequestsAdapter = new PendingGroupRequestsAdapter(this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(pendingGroupRequestsAdapter);
        }

        new NotificationsRequestsLocalModal(this).retrieveGroupRequest(notificationType);

        TextView TV_collapse = (TextView) findViewById(R.id.TV_collapse);
        if (TV_collapse != null) {
            TV_collapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pendingRequestsLayout != null) {
                        pendingRequestsLayout.startAnimation(topToBottom);
                        areRequestsOpen = false;
                    }
                }
            });
        }

        RippleView pendingRequests = (RippleView) findViewById(R.id.pendingRequests);
        if (groupPrivacy != null && groupPrivacy.equals("Private") && isCreatedByMe) {
            if (pendingRequests != null) {
                pendingRequests.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        if (!areRequestsOpen) {
                            areRequestsOpen = true;
                            if (pendingRequestsLayout != null) {
                                pendingRequestsLayout.startAnimation(bottomToTop);
                            }
                            if (pendingRequestsLayout != null) {
                                pendingRequestsLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        } else if (pendingRequests != null) {
            pendingRequests.setVisibility(View.GONE);
        }
    }

    private void managePrivacyLayout(String groupPrivacy, boolean isCreatedByMe) {
        LinearLayout privacyLayout = (LinearLayout) findViewById(R.id.privacyLayout);
        RelativeLayout chatLayout = (RelativeLayout) findViewById(R.id.RL_chatLayout);
        if (privacyLayout != null) {
            privacyLayout.setVisibility(View.GONE);
        }
        if (chatLayout != null) {
            chatLayout.setVisibility(View.GONE);
        }
        if (groupPrivacy != null && groupPrivacy.equals("Public")){
            if (privacyLayout != null && chatLayout != null) {
                chatLayout.setVisibility(View.VISIBLE);
            }
        } else if (groupPrivacy != null && groupPrivacy.equals("Private")){
            if (chatLayout != null && privacyLayout != null && !isCreatedByMe){
                privacyLayout.setVisibility(View.VISIBLE);
            } else if (chatLayout != null) {
                chatLayout.setVisibility(View.VISIBLE);
            }
        }
        RippleView btn_sendRequest = (RippleView) findViewById(R.id.btn_sendRequest);
        if (btn_sendRequest != null) {
            btn_sendRequest.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    new NotificationsModal(NotificationsView.this).sendGroupRequest(notificationType, sharedPreferences.getString("NAME", null));
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                if (!CB_selectAll.isChecked()) {
                    if (selectedItems.size() > 0) {
                        for (int i = 0; i < selectedItems.size(); i++) {
                            notificationInfos.add(notificationsAdapter.getItem(selectedItems.get(i)));
                        }

                        for (int i = 0; i < selectedItems.size(); i++) {
                            notificationsAdapter.remove(notificationInfos.get(i));
                        }

                        clearActionMode();

                        String[] notifications = new String[notificationInfos.size()];
                        for (int i = 0; i < notificationInfos.size(); i++) {
                            notifications[i] = notificationInfos.get(i).getNotification();
                        }
                        notificationsLocalModal.deleteNotifications(notifications, sharedPreferences.getString("NOTIFICATION_TYPE", null));
                        notificationInfos.clear();
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No item is selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    notificationsLocalModal.deleteAll(sharedPreferences.getString("NOTIFICATION_TYPE", null));
                    notificationsAdapter.clear();
                    notificationInfos.clear();
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    clearActionMode();
                }
                break;
            }

            case android.R.id.home: {
                clearActionMode();
                break;
            }
        }
        return true;
    }

    private void clearActionMode() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        notificationTitle.setText(sharedPreferences.getString("NOTIFICATION_TYPE", ""));
        toolbar.getMenu().clear();
        CB_selectAll.setChecked(false);
        CB_selectAll.setVisibility(View.GONE);
        IS_IN_ACTION_MODE = false;
        IS_IN_SELECT_ALL_MODE = false;
        notificationsAdapter.notifyDataSetChanged();
        selectedItems.clear();
        counter = 0;
    }

    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        notificationTitle.setText("0 item selected");
        toolbar.inflateMenu(R.menu.notifications_menu);
        CB_selectAll.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IS_IN_ACTION_MODE = true;
        IS_IN_SELECT_ALL_MODE = false;
        notificationsAdapter.notifyDataSetChanged();
        return true;
    }

    public void updateSelection(View v, int position) {
        if (((CheckBox) v).isChecked()) {
            Log.i("TAG", "updateSelection: " + position);
            counter += 1;
            updateCounter(counter);
            selectedItems.add(position);
        } else {
            counter -= 1;
            updateCounter(counter);
            if (selectedItems.size() > 0) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i) == position) {
                        selectedItems.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void updateCounter(int counter) {
        if (counter == 0) {
            notificationTitle.setText("0 item selected");
        } else {
            notificationTitle.setText(counter + " items selected");
        }
    }

    @Override
    public void onBackPressed() {
        if (IS_IN_ACTION_MODE) {
            clearActionMode();
        } else {
            super.onBackPressed();
        }
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.notificationsStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String notificationType = sharedPreferences.getString("NOTIFICATION_TYPE", null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(notificationType + "_COUNT", 0);
        editor.putBoolean("IS_CHAT_OPEN", false);
        editor.apply();
    }
}
