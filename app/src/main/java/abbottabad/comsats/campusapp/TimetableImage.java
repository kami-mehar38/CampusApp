package abbottabad.comsats.campusapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TimetableImage extends Activity implements View.OnClickListener {

    private ImageView imageView;
    private ProgressBar isWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_image_page);
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
        new GetImage().execute(TrackFacultyUtills.getRegistration());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel: {
                finish();
                break;
            }
        }
    }

    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String reg_id = params[0];
            String stringUrl = "http://hostellocator.com/images/" + reg_id + ".JPG";
            URL url;
            try {
                url = new URL(stringUrl);
                InputStream is = new BufferedInputStream(url.openStream());
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, null);
                is.close();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            isWaiting.setVisibility(View.GONE);
            if (bitmap != null && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
