package abbottabad.comsats.campusapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

class PendingGroupRequestsAdapter extends RecyclerView.Adapter<PendingGroupRequestsAdapter.ViewHolder>{

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
        Log.i("TAG", "onBindViewHolder: " + pendingGroupRequestsInfo.getName());

    }

    @Override
    public int getItemCount() {
        return pendingGroupRequestsInfoList.size();
    }

    void addItem(PendingGroupRequestsInfo pendingGroupRequestsInfo, int position){
        pendingGroupRequestsInfoList.add(position, pendingGroupRequestsInfo);
        notifyItemInserted(position);
    }

    void removeItem(int position){
        pendingGroupRequestsInfoList.remove(position);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RippleView btnAccept;
        private RippleView btnReject;
        private TextView TV_requesteeName;
        ViewHolder(View itemView) {
            super(itemView);
            btnAccept = (RippleView) itemView.findViewById(R.id.btn_accept);
            btnReject = (RippleView) itemView.findViewById(R.id.btn_reject);
            TV_requesteeName = (TextView) itemView.findViewById(R.id.TV_requesteeName);

        }
    }
}
