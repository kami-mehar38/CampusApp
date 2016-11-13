package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class TimetableImage extends Activity implements View.OnClickListener {

    private ImageView imageView;
    private ProgressBar isWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_image_page);

        // Create default options which will be used for every
        // displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        ImageView btnCancel = (ImageView) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        isWaiting = (ProgressBar) findViewById(R.id.isWaiting);
        imageView = (ImageView) findViewById(R.id.IV_picture);

        String stringUrl = "http://hostellocator.com/images/" + TrackFacultyUtills.getRegistration() + ".JPG";
        ImageLoader.getInstance().displayImage(stringUrl, imageView, defaultOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Toast.makeText(TimetableImage.this, "Error loading timetable", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                isWaiting.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel: {
                ImageLoader.getInstance().cancelDisplayTask(imageView);
                finish();
                break;
            }
        }
    }
}
