package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andexert.library.RippleView;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 27-Nov-16.
 */

class PendingGroupRequestsAdapter extends RecyclerView.Adapter<PendingGroupRequestsAdapter.ViewHolder> {

    private static final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private List<PendingGroupRequestsInfo> pendingGroupRequestsInfoList = new ArrayList<>();
    private Context context;

    PendingGroupRequestsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_pending_requests, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PendingGroupRequestsInfo pendingGroupRequestsInfo = pendingGroupRequestsInfoList.get(position);
        holder.TV_requesteeName.setText(pendingGroupRequestsInfo.getName());
        holder.TV_regId.setText(pendingGroupRequestsInfo.getRegId());
    }

    @Override
    public int getItemCount() {
        return pendingGroupRequestsInfoList.size();
    }

    void addItem(PendingGroupRequestsInfo pendingGroupRequestsInfo, int position) {
        pendingGroupRequestsInfoList.add(position, pendingGroupRequestsInfo);
        notifyItemInserted(position);
        if (NotificationsView.TV_noRequests != null){
            if (getItemCount() > 0){
                NotificationsView.TV_noRequests.setVisibility(View.GONE);
            }
        }
    }

    void removeItem(int position) {
        pendingGroupRequestsInfoList.remove(position);
        notifyDataSetChanged();
        if (NotificationsView.TV_noRequests != null){
            if (getItemCount() == 0){
                NotificationsView.TV_noRequests.setVisibility(View.VISIBLE);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {

        private RippleView btnAccept;
        private RippleView btnReject;
        private TextView TV_requesteeName;
        private TextView TV_regId;
        private String groupName;

        ViewHolder(View itemView) {
            super(itemView);
            btnAccept = (RippleView) itemView.findViewById(R.id.btn_accept);
            btnReject = (RippleView) itemView.findViewById(R.id.btn_reject);
            TV_requesteeName = (TextView) itemView.findViewById(R.id.TV_requesteeName);
            TV_regId = (TextView) itemView.findViewById(R.id.TV_regId);

            btnAccept.setOnRippleCompleteListener(this);
            btnReject.setOnRippleCompleteListener(this);

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
            groupName = sharedPreferences.getString("NOTIFICATION_TYPE", null);

        }

        @Override
        public void onComplete(RippleView rippleView) {
            if (rippleView.getId() == R.id.btn_accept) {
                if (groupName != null) {
                    NotificationsUtills.setPosition(getAdapterPosition());
                    new NotificationsModal(context).sendGroupRequestResponse("Welcome to " + groupName + ". You can now chat here.",
                            TV_regId.getText().toString().trim(),
                            groupName,
                            "Accepted");
                    new NotificationsRequestsLocalModal(context).deleteGroupRequest(TV_regId.getText().toString().trim(),
                            groupName);
                }
            } else if (rippleView.getId() == R.id.btn_reject) {
                if (groupName != null) {
                    NotificationsUtills.setPosition(getAdapterPosition());
                    new NotificationsModal(context).sendGroupRequestResponse("Your request to join " + groupName + " is rejected.",
                            TV_regId.getText().toString().trim(),
                            groupName,
                            "Rejected");
                    new NotificationsRequestsLocalModal(context).deleteGroupRequest(TV_regId.getText().toString().trim(),
                            groupName);
                }
            }
        }
    }
}
