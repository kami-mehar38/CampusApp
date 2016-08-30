package abbottabad.comsats.campusapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/30/16.
 */
public class ComplaintPollView extends AppCompatActivity implements View.OnClickListener {

    private final int CAMERA_REQUEST = 98;
    private Button btn_addImage;
    private ImageView IV_visualProof;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaintpoll_page);
        btn_addImage = (Button) findViewById(R.id.btn_addImage);
        if (btn_addImage != null) {
            btn_addImage.setOnClickListener(this);
        }
        IV_visualProof = (ImageView) findViewById(R.id.IV_visualProof);
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
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte [] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            //IV_visualProof.setImageBitmap(photo);
            Toast.makeText(ComplaintPollView.this, image_str, Toast.LENGTH_SHORT).show();
        }
    }
}
