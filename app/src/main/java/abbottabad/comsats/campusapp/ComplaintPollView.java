package abbottabad.comsats.campusapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/30/16.
 */
public class ComplaintPollView extends AppCompatActivity implements View.OnClickListener {

    private final int CAMERA_REQUEST = 98;
    private Button btn_addImage;
    private ImageView IV_visualProof;
    private EditText ET_description;
    private TextView TV_descriptionCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaintpoll_page);
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
                if (s.length() > 0 && s.length() <= 320){
                    TV_descriptionCounter.setText(String.valueOf(s.length()));
                } else if (s.length() == 0) TV_descriptionCounter.setText("0");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addImage: {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            byte[] byte_arr = getImageBytes(photo);

           // Toast.makeText(ComplaintPollView.this, byte_arr.toString(), Toast.LENGTH_SHORT).show();

           // Bitmap bitmap = BitmapFactory.decodeByteArray(byte_arr, 0, byte_arr.length);
            IV_visualProof.setImageBitmap(photo);
        }
    }

    private byte[] getImageBytes(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
