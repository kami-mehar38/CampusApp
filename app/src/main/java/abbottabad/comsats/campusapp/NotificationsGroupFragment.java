package abbottabad.comsats.campusapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This project CampusApp is created by Kamran Ramzan on 01-Jan-17.
 */

public class NotificationsGroupFragment extends Fragment {

    public static NotificationsListAdapter notificationsListAdapter;
    private NotificationsModal notificationsModal;
    public static boolean isFirstTime = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications_home_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            notificationsListAdapter = new NotificationsListAdapter(getContext());
            recyclerView.setAdapter(notificationsListAdapter);
        }

        FloatingActionButton FAB_createGroup = (FloatingActionButton) view.findViewById(R.id.FAB_createGroup);
        if (FAB_createGroup != null) {
            FAB_createGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), CreateNotificationsGroup.class));
                }
            });
        }

        notificationsModal = new NotificationsModal(getContext());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstTime) {
            notificationsModal.getNotificationGroups();
            isFirstTime = false;
        }
    }
}

