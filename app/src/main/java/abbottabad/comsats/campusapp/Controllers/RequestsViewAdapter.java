package abbottabad.comsats.campusapp.Controllers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import abbottabad.comsats.campusapp.R;
import abbottabad.comsats.campusapp.Helper_Classes.RequestsInfo;

/**
 * Created by Kamran Ramzan on 7/6/16.
 */
public class RequestsViewAdapter extends RecyclerView.Adapter <RequestsViewAdapter.ViewHolder> {

    private List<RequestsInfo> requestsInfoList;

    public RequestsViewAdapter(List<RequestsInfo> requestsInfoList) {
        this.requestsInfoList = requestsInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_requests, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestsInfo requestsInfo = requestsInfoList.get(position);
        holder.TV_requesterName.setText(requestsInfo.getName());
        holder.TV_requesterReg.setText(requestsInfo.getRegistration());
        holder.TV_requesterBloodType.setText(requestsInfo.getBloodType());
        holder.TV_requesterContact.setText(requestsInfo.getContact());
    }


    @Override
    public int getItemCount() {
        return requestsInfoList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TV_requesterName;
        private TextView TV_requesterReg;
        private TextView TV_requesterBloodType;
        private TextView TV_requesterContact;
        private Button btnCall;
        private Button btnMessage;

        public ViewHolder(final View itemView) {
            super(itemView);
            TV_requesterName = (TextView) itemView.findViewById(R.id.TV_requesterName);
            TV_requesterReg = (TextView) itemView.findViewById(R.id.TV_requesterReg);
            TV_requesterBloodType = (TextView) itemView.findViewById(R.id.TV_requesterBloodType);
            TV_requesterContact = (TextView) itemView.findViewById(R.id.TV_requesterContact);
            btnCall = (Button) itemView.findViewById(R.id.btnCall);
            btnMessage = (Button) itemView.findViewById(R.id.btnMessage);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:" + TV_requesterContact.getText().toString().trim()));
                    itemView.getContext().startActivity(intent);
                }
            });

            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("sms:" + TV_requesterContact.getText().toString().trim()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
