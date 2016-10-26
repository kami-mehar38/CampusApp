package abbottabad.comsats.campusapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This project CampusApp is created by Kamran Ramzan on 26-Oct-16.
 */

public class CreateNotificationsGroup extends AppCompatActivity implements View.OnClickListener {

    public final int Request_load_image = 19;
    private ImageView IV_groupPicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notifications_group_page);
        IV_groupPicture = (ImageView) findViewById(R.id.IV_groupPicture);
        if (IV_groupPicture != null) {
            IV_groupPicture.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_groupPicture: {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, Request_load_image);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_load_image && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String imaePath = getPathFromURI(imageUri);
            try {
                Bitmap bitmap = decodeBitmap(imaePath);
                if (bitmap != null) {
                    IV_groupPicture.setImageBitmap(bitmap);
                } else Toast.makeText(CreateNotificationsGroup.this, "Image not selected", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else Toast.makeText(CreateNotificationsGroup.this, "Image not selected", Toast.LENGTH_LONG).show();
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }

        return res;
    }

    private Bitmap decodeBitmap(String selectedImage) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImage);
        if (bitmap != null) {
            ExifInterface exif;
            try {
                exif = new ExifInterface(selectedImage);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
