package abbottabad.comsats.campusapp;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Kamran Ramzan on 6/28/16.
 */
public class DonorsViewAdapter extends RecyclerView.Adapter <DonorsViewAdapter.ViewHolder> {

    private List<DonorsInfo> donorsInfoList;

    public DonorsViewAdapter(List<DonorsInfo> donorsInfoList) {
        this.donorsInfoList = donorsInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_donors, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DonorsInfo donorsInfo = donorsInfoList.get(position);
        holder.TV_donorName.append(donorsInfo.getName());
        holder.TV_donorBloodtype.append(donorsInfo.getBloodType());
        holder.TV_donorContact.append(donorsInfo.getContact());
    }

    @Override
    public int getItemCount() {
        return donorsInfoList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private TextView TV_donorName;
        private TextView TV_donorBloodtype;
        private TextView TV_donorContact;
        private Button btnCall;
        private Button btnMessage;
        public ViewHolder(View itemView) {
            super(itemView);
            TV_donorName = (TextView) itemView.findViewById(R.id.TV_donorName);
            TV_donorBloodtype = (TextView) itemView.findViewById(R.id.TV_donorBloodtype);
            TV_donorContact = (TextView) itemView.findViewById(R.id.TV_donorContact);
            btnCall = (Button) itemView.findViewById(R.id.btnCall);
            btnMessage = (Button) itemView.findViewById(R.id.btnMessage);
            final RelativeLayout relativeLayout = (RelativeLayout) itemView.findViewById(R.id.RL_cardView);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relativeLayout.setBackgroundColor(Color.WHITE);
                }
            });
        }
    }
}
