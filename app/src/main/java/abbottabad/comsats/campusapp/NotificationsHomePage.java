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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class NotificationsHomePage extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.notifications_home_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            NotificationsListAdapter notificationsListAdapter = new NotificationsListAdapter();
            recyclerView.setAdapter(notificationsListAdapter);
        }
    }

    /*public static void setBadgeCount() {
        int softSocietyBadgeCount = sharedPreferences.getInt("Soft Society_COUNT", 0);
        if (softSocietyBadgeCount > 0){
            badgeSoftSociety.setVisibility(View.VISIBLE);
            badgeSoftSociety.setText(String.valueOf(softSocietyBadgeCount));
            setpopupAnimation(badgeSoftSociety);
        } else badgeSoftSociety.setVisibility(View.GONE);

        int funkadaBadgeCount = sharedPreferences.getInt("Funkada_COUNT", 0);
        if (funkadaBadgeCount > 0){
            badgeFunkada.setVisibility(View.VISIBLE);
            badgeFunkada.setText(String.valueOf(funkadaBadgeCount));
            setpopupAnimation(badgeFunkada);
        } else badgeFunkada.setVisibility(View.GONE);

        int hertzSocietyBadgeCount = sharedPreferences.getInt("Hertz Society_COUNT", 0);
        if (hertzSocietyBadgeCount > 0){
            badgeHetzSociety.setVisibility(View.VISIBLE);
            badgeHetzSociety.setText(String.valueOf(hertzSocietyBadgeCount));
            setpopupAnimation(badgeHetzSociety);
        } else badgeHetzSociety.setVisibility(View.GONE);

        int aadrishBadgeCount = sharedPreferences.getInt("Aadrish_COUNT", 0);
        if (aadrishBadgeCount > 0){
            badgeAadrish.setVisibility(View.VISIBLE);
            badgeAadrish.setText(String.valueOf(aadrishBadgeCount));
            setpopupAnimation(badgeAadrish);
        } else badgeAadrish.setVisibility(View.GONE);

        int clsBadgeCount = sharedPreferences.getInt("Comsats Literary Society_COUNT", 0);
        if (clsBadgeCount > 0){
            badgeCLS.setVisibility(View.VISIBLE);
            badgeCLS.setText(String.valueOf(clsBadgeCount));
            setpopupAnimation(badgeCLS);
        } else badgeCLS.setVisibility(View.GONE);

        int ieeeSocietyBadgeCount = sharedPreferences.getInt("IEEE Society_COUNT", 0);
        if (ieeeSocietyBadgeCount > 0){
            badgeIEEESociety.setVisibility(View.VISIBLE);
            badgeIEEESociety.setText(String.valueOf(ieeeSocietyBadgeCount));
            setpopupAnimation(badgeIEEESociety);
        } else badgeIEEESociety.setVisibility(View.GONE);

        int othersBadgeCount = sharedPreferences.getInt("Others_COUNT", 0);
        if (othersBadgeCount > 0){
            badgeOthers.setVisibility(View.VISIBLE);
            badgeOthers.setText(String.valueOf(othersBadgeCount));
            setpopupAnimation(badgeOthers);
        } else badgeOthers.setVisibility(View.GONE);
    }*/

    private static void setpopupAnimation(TextView textView) {
        Animation popupAnimation = AnimationUtils.loadAnimation(textView.getContext(), R.anim.popup_animation);
        textView.startAnimation(popupAnimation);
    }


    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.notificationsStatusBar);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

}
