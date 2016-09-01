package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/30/16.
 */
public class ComplaintPollView extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private final int CAMERA_REQUEST = 98;
    private Button btn_addImage;
    private Button btn_sendComplaint;
    private ImageView IV_visualProof;
    private EditText ET_description;
    private EditText ET_name;
    private EditText ET_regID;
    private EditText ET_contact;
    private TextView TV_descriptionCounter;
    public static RecyclerView RV_complaints;
    private Validation validation;
    private Bitmap photo;
    private byte[] byte_arr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences applicationStatus = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String APPLICATION_STATUS = applicationStatus.getString("APPLICATION_STATUS", null);
        if (APPLICATION_STATUS != null) {
            if (APPLICATION_STATUS.equals("FOOD")){
                setContentView(R.layout.complaintpoll_page_admin);
                RV_complaints = (RecyclerView) findViewById(R.id.RV_complaints);
                if (RV_complaints != null) {
                    RV_complaints.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    RV_complaints.setLayoutManager(layoutManager);
                    RV_complaints.addItemDecoration(new RecyclerViewDivider(this));
                    new ComplaintPollLocalModal(this).retrieveComplaints();
                }
              /*  List<ComplaintsInfo> complaintsInfos = new ArrayList<>();
                ComplaintsInfo complaintsInfo = new ComplaintsInfo();
                complaintsInfo.setName("Kamran Ramzan");
                complaintsInfo.setRegistration("sp13-bse-098");
                complaintsInfo.setContact("03450578052");
                //complaintsInfo.setDescription("Descriptiojiuguivfuibfunyiyfsrwci7 8 68ec64w6uujfbhjjkyrb4ctn of complaint that you are intended to process.");
                complaintsInfos.add(complaintsInfo);
                ComplaintPollVIewAdapter complaintPollVIewAdapter = new ComplaintPollVIewAdapter(complaintsInfos);
                RV_complaints.setAdapter(complaintPollVIewAdapter);*/
            } else {
                setContentView(R.layout.complaintpoll_page_others);
                btn_addImage = (Button) findViewById(R.id.btn_addImage);
                if (btn_addImage != null) {
                    btn_addImage.setOnClickListener(this);
                }
                IV_visualProof = (ImageView) findViewById(R.id.IV_visualProof);
                TV_descriptionCounter = (TextView) findViewById(R.id.TV_descriptionCounter);
                ET_description = (EditText) findViewById(R.id.ET_desciption);
                ET_description.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() > 0 && s.length() <= 320) {
                            TV_descriptionCounter.setText(String.valueOf(s.length()));
                        } else if (s.length() == 0) TV_descriptionCounter.setText("0");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                ET_name = (EditText) findViewById(R.id.ET_name);
                ET_regID = (EditText) findViewById(R.id.ET_regID);
                ET_contact =(EditText) findViewById(R.id.ET_contact);
                btn_sendComplaint = (Button) findViewById(R.id.btn_sendComplaint);
                if (btn_sendComplaint != null) {
                    btn_sendComplaint.setOnClickListener(this);
                }
                validation = new Validation();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addImage: {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
            }
            case R.id.btn_sendComplaint: {
                String name = ET_name.getText().toString();
                String contact = ET_contact.getText().toString();
                String regID = ET_regID.getText().toString();
                String description = ET_description.getText().toString();
                if (validation.validateName(name)){
                    if (validation.validateReg(regID)){
                        if (validation.validatePhoneNumber(contact)){
                            new ComplaintPollModal(ComplaintPollView.this).sendComplaint(name, regID, contact, description);
                        } else Toast.makeText(ComplaintPollView.this, "Invalid contact #", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(ComplaintPollView.this, "Invalid registration id", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(ComplaintPollView.this, "Invalid name", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            byte_arr = getImageBytes(photo);

/*
            Toast.makeText(ComplaintPollView.this, byte_arr.toString(), Toast.LENGTH_SHORT).show();
*/

            Bitmap bitmap = BitmapFactory.decodeByteArray(byte_arr, 0, byte_arr.length);
            IV_visualProof.setImageBitmap(bitmap);
        }
    }

    private byte[] getImageBytes(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
