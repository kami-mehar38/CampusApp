package abbottabad.comsats.campusapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * This project CampusApp is created by Kamran Ramzan on 9/1/16.
 */
class DonorsViewAdapter extends RecyclerView.Adapter <DonorsViewAdapter.ViewHolder> {

    private List<DonorsInfo> donorsInfoList = new ArrayList<>();

    DonorsViewAdapter(List<DonorsInfo> donorsInfoList) {
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
        holder.TV_donorName.setText(donorsInfo.getName());
        holder.TV_donorRegistration.setText(donorsInfo.getRegistration());
        holder.TV_donorBloodtype.setText(donorsInfo.getBloodType());
        holder.TV_donorContact.setText(donorsInfo.getContact());
    }

    @Override
    public int getItemCount() {
        return donorsInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView TV_donorName;
        private TextView TV_donorRegistration;
        private TextView TV_donorBloodtype;
        private TextView TV_donorContact;
        private Button btnCall;
        private Button btnMessage;
        private CheckBox CB_bleed;
        private AlertDialog alertDialog;
        ViewHolder(final View itemView) {
            super(itemView);

            TV_donorName = (TextView) itemView.findViewById(R.id.TV_donorName);
            TV_donorRegistration = (TextView) itemView.findViewById(R.id.TV_donorRegistration);
            TV_donorBloodtype = (TextView) itemView.findViewById(R.id.TV_donorBloodType);
            TV_donorContact = (TextView) itemView.findViewById(R.id.TV_donorContact);
            btnCall = (Button) itemView.findViewById(R.id.btnCall);
            btnMessage = (Button) itemView.findViewById(R.id.btnMessage);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:" + TV_donorContact.getText().toString().trim()));
                    itemView.getContext().startActivity(intent);
                }
            });
            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("sms:" + TV_donorContact.getText().toString().trim()));
                    itemView.getContext().startActivity(intent);
                }
            });
            CB_bleed = (CheckBox) itemView.findViewById(R.id.CB_bleed);
            CB_bleed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CB_bleed.isChecked()){
                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                        int curr_year = calendar.get(Calendar.YEAR);
                        int curr_month = calendar.get(Calendar.MONTH);
                        int curr_date = calendar.get(Calendar.DATE);
                        final String bleededDate = String.valueOf(curr_year) + "/" +
                                String.valueOf(curr_month + 1) + "/" +
                                String.valueOf(curr_date);
                        final String registration = TV_donorRegistration.getText().toString().trim();
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setCancelable(false);
                        builder.setTitle("Alert");
                        builder.setMessage("If you are sure about the bleeding of donor then press OK, otherwise press CANCEL.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new BloodBankModal(itemView.getContext())
                                        .updateBleedingDate(registration, bleededDate);
                                new BloodBankModal(itemView.getContext()).retrieveDonors("All");
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                                CB_bleed.setChecked(false);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });
        }
    }
}
