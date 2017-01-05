package abbottabad.comsats.campusapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 06-Jan-17.
 */

class ParkingSlotsAdapter extends RecyclerView.Adapter<ParkingSlotsAdapter.ViewHolder> {

    private Context context;
    private List<ParkingSlotsInfo> parkingSlotsInfos = new ArrayList<>();

    ParkingSlotsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_parking_slot, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParkingSlotsInfo parkingSlotsInfo = parkingSlotsInfos.get(position);
        holder.slotNo.setText(parkingSlotsInfo.getSlotNo());
        if (parkingSlotsInfo.isFree()) {
            holder.TV_slotStatus.setText("Free");
        } else holder.TV_slotStatus.setText("Occupied");
    }

    @Override
    public int getItemCount() {
        return parkingSlotsInfos.size();
    }

    void addItem(ParkingSlotsInfo parkingSlotsInfo, int position) {
        parkingSlotsInfos.add(position, parkingSlotsInfo);
        notifyItemInserted(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TV_slotStatus;
        private TextView slotNo;
        private LinearLayout parkingSlot;
        public ViewHolder(View itemView) {
            super(itemView);
            TV_slotStatus = (TextView) itemView.findViewById(R.id.TV_slotStatus);
            slotNo = (TextView) itemView.findViewById(R.id.slotNo);
            parkingSlot = (LinearLayout) itemView.findViewById(R.id.parkingSlot);
            parkingSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
