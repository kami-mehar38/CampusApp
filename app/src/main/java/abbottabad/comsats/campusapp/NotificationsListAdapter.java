package abbottabad.comsats.campusapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 25-Oct-16.
 */

class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationsListViewHolder> {

    private List<NotificationsListInfo> notificationsListInfoList = new ArrayList<>();

    @Override
    public NotificationsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_notifications_chat, null);
        return new NotificationsListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return notificationsListInfoList.size();
    }

    @Override
    public void onBindViewHolder(NotificationsListViewHolder holder, int position) {

    }

    public void addItem(NotificationsListInfo notificationsListInfo, int position){
        notificationsListInfoList.add(position, notificationsListInfo);
        notifyItemInserted(position);
    }

    public void removeItem(int position){
        notificationsListInfoList.remove(position);
        notifyItemRemoved(position);
    }

    class NotificationsListViewHolder extends RecyclerView.ViewHolder{

        NotificationsListViewHolder(View itemView) {
            super(itemView);

        }
    }
}
