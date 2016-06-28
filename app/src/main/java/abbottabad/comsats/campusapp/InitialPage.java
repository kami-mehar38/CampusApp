package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * @author Kamran Ramzan
 * This class is an activity class, having the layout of Login Page and methods
 * to start the animations
 */
public class InitialPage extends Activity implements View.OnClickListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        final String APPLICATION_STATUS = sharedPreferences.getString("APPLICATION_STATUS", "NULL");
        if (APPLICATION_STATUS.equals("STUDENT")){
            startActivity(new Intent(this, HomePageView.class));
        }

        if (APPLICATION_STATUS.equals("BLOOD_BANK")){
            startActivity(new Intent(this, LoginView.class));
        }
        setContentView(R.layout.initial_page);

        isPlayServicesAvailable();

        new InitialPageController(this).execute();

        ImageView iv_comsats_logo = (ImageView) findViewById(R.id.IV_comsats_logo);
        TextView tv_ciit = (TextView) findViewById(R.id.TV_ciit);
        TextView tv_forSignup = (TextView) findViewById(R.id.TV_forSignup);
        TextView tv_forLogin = (TextView) findViewById(R.id.TV_forLogin);

        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        if (btn_signup != null) {
            btn_signup.setOnClickListener(this);
        }

        Button btn_login = (Button) findViewById(R.id.btn_login);
        if (btn_login != null) {
            btn_login.setOnClickListener(this);
        }

        setAnimations(iv_comsats_logo, tv_ciit, tv_forSignup, tv_forLogin, btn_signup, btn_login);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isPlayServicesAvailable();
    }

    private void setAnimations(ImageView iv_comsats_logo, TextView tv_ciit,
                               TextView tv_forSignup, TextView tv_forLogin,
                               Button btn_signup, Button btn_login) {
        Animation zoomInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
        iv_comsats_logo.startAnimation(zoomInAnimation);
        tv_ciit.startAnimation(zoomInAnimation);

        Animation bounceAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.accelerate_decelerate_aimation);
        tv_forSignup.startAnimation(bounceAnimation);
        tv_forLogin.startAnimation(bounceAnimation);
        btn_signup.startAnimation(bounceAnimation);
        btn_login.startAnimation(bounceAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:{
                Intent intent = new Intent(InitialPage.this, SignUpView.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_login:{
                Intent intent = new Intent(InitialPage.this, LoginView.class);
                startActivity(intent);
                break;
            }
        }
    }
    private boolean isPlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("This device is not supported");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
            return false;
        }
        return true;
    }
}
