package abbottabad.comsats.campusapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

/**
 * This project CampusApp is created by Kamran Ramzan on 05-Jan-17.
 */

public class ParkingHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.parking_toolbar);
        setSupportActionBar(toolbar);
        setStatusBarColor();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ParkingSlotsAdapter parkingSlotsAdapter = new ParkingSlotsAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        int padding = (int) getResources().getDimension(R.dimen.grid_layout_padding);
        recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(padding, 2));
        recyclerView.setAdapter(parkingSlotsAdapter);
        ParkingSlotsInfo parkingSlotsInfo = new ParkingSlotsInfo();
        parkingSlotsInfo.setFree(true);
        parkingSlotsInfo.setSlotNo("Slot 1");
        parkingSlotsAdapter.addItem(parkingSlotsInfo, 0);
        ParkingSlotsInfo parkingSlotsInfo1 = new ParkingSlotsInfo();
        parkingSlotsInfo1.setFree(true);
        parkingSlotsInfo1.setSlotNo("Slot 2");
        parkingSlotsAdapter.addItem(parkingSlotsInfo1, 0);
        ParkingSlotsInfo parkingSlotsInfo2 = new ParkingSlotsInfo();
        parkingSlotsInfo2.setFree(false);
        parkingSlotsInfo2.setSlotNo("Slot 3");
        parkingSlotsAdapter.addItem(parkingSlotsInfo2, 0);
        ParkingSlotsInfo parkingSlotsInfo3 = new ParkingSlotsInfo();
        parkingSlotsInfo3.setFree(true);
        parkingSlotsInfo3.setSlotNo("Slot 4");
        parkingSlotsAdapter.addItem(parkingSlotsInfo3, 0);
        ParkingSlotsInfo parkingSlotsInfo4 = new ParkingSlotsInfo();
        parkingSlotsInfo4.setFree(false);
        parkingSlotsInfo4.setSlotNo("Slot 5");
        parkingSlotsAdapter.addItem(parkingSlotsInfo4, 0);
        ParkingSlotsInfo parkingSlotsInfo5 = new ParkingSlotsInfo();
        parkingSlotsInfo5.setFree(true);
        parkingSlotsInfo5.setSlotNo("Slot 6");
        parkingSlotsAdapter.addItem(parkingSlotsInfo5, 0);
        ParkingSlotsInfo parkingSlotsInfo6 = new ParkingSlotsInfo();
        parkingSlotsInfo6.setFree(false);
        parkingSlotsInfo6.setSlotNo("Slot 7");
        parkingSlotsAdapter.addItem(parkingSlotsInfo6, 0);
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, R.color.parkingStatusBar);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
