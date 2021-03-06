package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This project CampusApp is created by Kamran Ramzan on 26-Oct-16.
 */

public class CreateNotificationsGroup extends AppCompatActivity implements View.OnClickListener {

    public final int Request_load_image = 19;
    private ImageView IV_groupPicture;
    private SharedPreferences sharedPreferences;
    private NotificationsModal notificationsModal;
    private Bitmap bitmap = null;
    private EditText ET_groupName;
    private Spinner SP_groupPrivacy;
    private int spinnerOption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notifications_group_page);
        setStatusBarColor();
        String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
        sharedPreferences = getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        notificationsModal = new NotificationsModal(this);

        IV_groupPicture = (ImageView) findViewById(R.id.IV_groupPicture);

        FloatingActionButton FAB_groupCreation = (FloatingActionButton) findViewById(R.id.FAB_groupCreation);
        if (FAB_groupCreation != null) {
            FAB_groupCreation.setOnClickListener(this);
        }

        FloatingActionButton FAB_selectImage = (FloatingActionButton) findViewById(R.id.FAB_selectImage);
        FAB_selectImage.setOnClickListener(this);

        ET_groupName = (EditText) findViewById(R.id.ET_groupName);

        SP_groupPrivacy = (Spinner) findViewById(R.id.SP_groupPrivacy);
        populateSpinner(SP_groupPrivacy);
        SP_groupPrivacy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerOption = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.IV_openPrivacySpinner);
        if (imageView != null) {
            imageView.setOnClickListener(this);
        }
    }

    void populateSpinner(Spinner SPloginOptions) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.privacy_options,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to Spinner
        SPloginOptions.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FAB_selectImage: {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, Request_load_image);
                break;
            }
            case R.id.FAB_groupCreation: {
                String groupName = ET_groupName.getText().toString().trim();
                String reg_id = sharedPreferences.getString("REG_ID", null);
                DateFormat df = new SimpleDateFormat("d MMM yyyy 'AT' h:mm a", Locale.getDefault());
                String currentDateTimeString = df.format(Calendar.getInstance().getTime());
                String name = sharedPreferences.getString("NAME", null);
                if (!groupName.isEmpty()) {
                    if (bitmap != null) {
                        if (spinnerOption != 0) {
                            switch (spinnerOption){
                                case 1: {
                                    String imageString = getStringImage(bitmap);
                                    notificationsModal.createGroup(imageString, groupName, "Public", name, reg_id, currentDateTimeString);
                                    break;
                                }
                                case 2: {
                                    String imageString = getStringImage(bitmap);
                                    notificationsModal.createGroup(imageString, groupName, "Private", name, reg_id, currentDateTimeString);
                                    break;
                                }
                            }
                        } else
                            Toast.makeText(CreateNotificationsGroup.this, "Select group privacy", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(CreateNotificationsGroup.this, "Select group image", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(CreateNotificationsGroup.this, "Enter group name", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.IV_openPrivacySpinner: {
                SP_groupPrivacy.performClick();
                break;
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_load_image && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String imaePath = getPathFromURI(imageUri);
            try {
                bitmap = resizeImageForImageView(decodeBitmap(imaePath));
                if (bitmap != null) {
                    IV_groupPicture.setImageBitmap(bitmap);
                } else
                    Toast.makeText(CreateNotificationsGroup.this, "Image not selected", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(CreateNotificationsGroup.this, "Image not selected", Toast.LENGTH_LONG).show();
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
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private Bitmap resizeImageForImageView(Bitmap bitmap) {
        int scaleSize = 480;
        Bitmap resizedBitmap;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor;
        if (originalHeight > originalWidth) {
            newHeight = scaleSize;
            multFactor = (float) originalWidth / (float) originalHeight;
            newWidth = (int) (newHeight * multFactor);
        } else if (originalWidth > originalHeight) {
            newWidth = scaleSize;
            multFactor = (float) originalHeight / (float) originalWidth;
            newHeight = (int) (newWidth * multFactor);
        } else if (originalHeight == originalWidth) {
            newHeight = scaleSize;
            newWidth = scaleSize;
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.createGroupStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
