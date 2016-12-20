package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/30/16.
 */
public class ComplaintPollView extends AppCompatActivity implements View.OnClickListener, RippleView.OnRippleCompleteListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private EditText ET_description;
    private EditText ET_name;
    private EditText ET_regID;
    private EditText ET_contact;
    private TextView TV_descriptionCounter;
    private final int descriptionLength = 320;
    public static RecyclerView RV_complaints;
    public static ComplaintPollVIewAdapter complaintPollVIewAdapter;
    private Validation validation;
    private Spinner SPadminOptions;
    private int adminOption;
    private AlertDialog alertDialog;
    private ImageView IV_complaintPicture;
    private int REQUEST_SELECT_IMAGE = 25;
    private Bitmap bitmap;
    private AlertDialog alertDialogOptions;
    private int REQUEST_TAKE_PICTURE = 26;
    public static TextView TV_noComplaints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        SharedPreferences applicationStatus = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String APPLICATION_STATUS = applicationStatus.getString("APPLICATION_STATUS", null);
        if (APPLICATION_STATUS != null) {
            if (APPLICATION_STATUS.equals("FOOD")) {
                setContentView(R.layout.complaintpoll_page_admin);
                TV_noComplaints = (TextView) findViewById(R.id.TV_noComplaints);
                RV_complaints = (RecyclerView) findViewById(R.id.RV_complaints);
                if (RV_complaints != null) {
                    RV_complaints.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    RV_complaints.setLayoutManager(layoutManager);
                    RV_complaints.addItemDecoration(new RecyclerViewDivider(this));
                    complaintPollVIewAdapter = new ComplaintPollVIewAdapter(this);
                    ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(complaintPollVIewAdapter);
                    scaleInAnimationAdapter.setFirstOnly(false);
                    RV_complaints.setAdapter(scaleInAnimationAdapter);
                    new ComplaintPollLocalModal(this).retrieveComplaints();
                }
            } else {
                setContentView(R.layout.complaintpoll_page_others);
                ImageView IV_openAdminSpinner = (ImageView) findViewById(R.id.IV_openAdminSpinner);
                if (IV_openAdminSpinner != null) {
                    IV_openAdminSpinner.setOnClickListener(this);
                }

                SPadminOptions = (Spinner) findViewById(R.id.SPadmin);
                populateSpinner();
                if (SPadminOptions != null) {
                    SPadminOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            adminOption = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                TV_descriptionCounter = (TextView) findViewById(R.id.TV_descriptionCounter);
                ET_description = (EditText) findViewById(R.id.ET_desciption);
                ET_description.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        TV_descriptionCounter.setText(String.valueOf(descriptionLength - s.length()));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                ET_name = (EditText) findViewById(R.id.ET_name);
                ET_regID = (EditText) findViewById(R.id.ET_regID);
                ET_contact = (EditText) findViewById(R.id.ET_contact);
                Button btn_sendComplaint = (Button) findViewById(R.id.btn_sendComplaint);
                if (btn_sendComplaint != null) {
                    btn_sendComplaint.setOnClickListener(this);
                }
                IV_complaintPicture = (ImageView) findViewById(R.id.IV_complaintPicture);
                IV_complaintPicture.setOnClickListener(this);

                View view = LayoutInflater.from(this).inflate(R.layout.picture_option_view, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(view);
                alertDialogOptions = builder.create();

                RippleView BTN_choosePicture = (RippleView) view.findViewById(R.id.BTN_choosePicture);
                BTN_choosePicture.setOnRippleCompleteListener(this);
                RippleView BTN_takePicture = (RippleView) view.findViewById(R.id.BTN_takePicture);
                BTN_takePicture.setOnRippleCompleteListener(this);

                validation = new Validation();
            }
        }
    }

    void populateSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(ComplaintPollView.this,
                R.array.admin_options,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to Spinner
        SPadminOptions.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendComplaint: {
                String name = ET_name.getText().toString();
                String contact = ET_contact.getText().toString();
                String regID = ET_regID.getText().toString();
                String description = ET_description.getText().toString();
                String imageString = getStringImage(bitmap);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault());
                String currentDateTimeString = df.format(Calendar.getInstance().getTime());
                if (validation.validateName(name)) {
                    if (validation.validateReg(regID)) {
                        if (validation.validatePhoneNumber(contact)) {
                            if (imageString != null) {
                                if (adminOption != 0) {
                                    switch (adminOption) {
                                        case 1: {
                                            new ComplaintPollModal(ComplaintPollView.this).sendComplaint(name,
                                                    regID,
                                                    contact,
                                                    description,
                                                    currentDateTimeString,
                                                    imageString);
                                        }
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setTitle("Error");
                                    builder.setMessage("Please select complaint admin");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            if (alertDialog != null) {
                                                alertDialog.cancel();
                                            }
                                        }
                                    });
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            } else
                                Toast.makeText(ComplaintPollView.this, "Select image", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ComplaintPollView.this, "Invalid contact #", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(ComplaintPollView.this, "Invalid registration id", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ComplaintPollView.this, "Invalid name", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.IV_openAdminSpinner: {
                SPadminOptions.performClick();
                break;
            }
            case R.id.IV_complaintPicture: {
                alertDialogOptions.show();
                break;
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        if (bmp == null){
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String imaePath = getPathFromURI(imageUri);
            try {
                bitmap = resizeImageForImageView(decodeBitmap(imaePath));
                if (bitmap != null) {
                    IV_complaintPicture.setImageBitmap(bitmap);
                } else
                    Toast.makeText(ComplaintPollView.this, "Image not selected", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK && data != null) {
            try {
                IV_complaintPicture.setImageBitmap(resizeImageForImageView(decodeBitmap(getPathFromURI(data.getData()))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
            int color = ContextCompat.getColor(this, R.color.complaintPollStatusBar);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()) {
            case R.id.BTN_choosePicture: {
                alertDialogOptions.cancel();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_SELECT_IMAGE);
                break;
            }
            case R.id.BTN_takePicture: {
                alertDialogOptions.cancel();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,REQUEST_TAKE_PICTURE );
                break;
            }
        }
    }
}
