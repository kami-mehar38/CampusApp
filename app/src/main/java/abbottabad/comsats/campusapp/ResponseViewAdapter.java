package abbottabad.comsats.campusapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This project CampusApp is created by Kamran Ramzan on 03-Oct-16.
 */

class ResponseViewAdapter extends RecyclerView.Adapter <ResponseViewAdapter.ViewHolder> {

    private List<ResponseInfo> responseInfoList = new ArrayList<>();
    private final String PREFERENCE_FILE_KEY = "abbottabad.comsats.campusapp";
    private Context context;

    ResponseViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_response, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ResponseInfo responseInfo = responseInfoList.get(position);
        holder.TV_requesterName.setText(responseInfo.getName());
        holder.TV_requesterReg.setText(responseInfo.getRegistration());
        holder.TV_requesterBloodType.setText(responseInfo.getBloodType());
        holder.TV_requesterContact.setText(responseInfo.getContact());
        String distance = "Distance from you " + String.valueOf(responseInfo.getDistance()) + " meters";
        holder.TV_Distance.setText(distance);
        if (responseInfo.getIsAccepted() == 1 || responseInfo.getIsRejected() == 1){
            holder.btnAccept.setTextColor(Color.parseColor("#999999"));
            Drawable drawableTop = context.getResources().getDrawable(R.drawable.ic_action_accept_disabled);
            holder.btnAccept.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            holder.btnCancel.setTextColor(Color.parseColor("#999999"));
            Drawable drawableTopCancel = context.getResources().getDrawable(R.drawable.ic_action_cancel_disabled);
            holder.btnCancel.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopCancel, null, null);
        }
    }

    @Override
    public int getItemCount() {
        return responseInfoList.size();
    }

    public void add(ResponseInfo responseInfo, int position){
        responseInfoList.add(position,responseInfo);
        notifyItemInserted(position);
        if (BloodRequestResponseFragment.TV_noResponse != null){
            if (getItemCount() > 0){
                BloodRequestResponseFragment.TV_noResponse.setVisibility(View.GONE);
            }
        }
    }

    private void remove(int position) {
        responseInfoList.remove(position);
        notifyItemRemoved(position);
        if (BloodRequestResponseFragment.TV_noResponse != null){
            if (getItemCount() == 0){
                BloodRequestResponseFragment.TV_noResponse.setVisibility(View.VISIBLE);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TV_requesterName;
        private TextView TV_requesterReg;
        private TextView TV_requesterBloodType;
        private TextView TV_requesterContact;
        private TextView TV_Distance;
        private TextView btnAccept;
        private TextView btnCancel;
        private TextView btnDelete;
        private Button btnCall;
        private AlertDialog alertDialog;

        ViewHolder(final View itemView) {
            super(itemView);
            TV_requesterName = (TextView) itemView.findViewById(R.id.TV_requesterName);
            TV_requesterReg = (TextView) itemView.findViewById(R.id.TV_requesterReg);
            TV_requesterBloodType = (TextView) itemView.findViewById(R.id.TV_requesterBloodType);
            TV_requesterContact = (TextView) itemView.findViewById(R.id.TV_requesterContact);
            TV_Distance = (TextView) itemView.findViewById(R.id.TV_Distance);
            btnAccept = (TextView) itemView.findViewById(R.id.btnAccept);
            btnCancel = (TextView) itemView.findViewById(R.id.btnCancel);
            btnDelete = (TextView) itemView.findViewById(R.id.btnDelete);
            btnCall = (Button) itemView.findViewById(R.id.btnCall);
            final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure to delete?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                            new BloodBankResponseModal(itemView.getContext()).deleteResponse(TV_requesterReg.getText().toString());
                            remove(getAdapterPosition());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (new BloodBankResponseModal(itemView.getContext()).getIsRejected(TV_requesterReg.getText().toString()) == 0) {
                        if (new BloodBankResponseModal(itemView.getContext()).getIsAccepted(TV_requesterReg.getText().toString()) == 0) {
                            builder.setTitle("Accept");
                            builder.setMessage("Are you sure to accept blood donation from this donor?");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                    new BloodBankModal(itemView.getContext()).acceptResponse(TV_requesterReg.getText().toString());
                                    new BloodBankResponseModal(context).setIsAccepted(TV_requesterReg.getText().toString());
                                    btnAccept.setTextColor(Color.parseColor("#999999"));
                                    Drawable drawableTopAccept = context.getResources().getDrawable(R.drawable.ic_action_accept_disabled);
                                    btnAccept.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopAccept, null, null);
                                    btnCancel.setTextColor(Color.parseColor("#999999"));
                                    Drawable drawableTopReject = context.getResources().getDrawable(R.drawable.ic_action_cancel_disabled);
                                    btnCancel.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopReject, null, null);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setTitle("Accept");
                            builder1.setMessage("You have already accepted blood donation from this donor");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                }
                            });
                            alertDialog = builder1.create();
                            alertDialog.show();
                        }
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("Accept");
                        builder1.setMessage("You have already rejected blood donation from this donor");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                            }
                        });
                        alertDialog = builder1.create();
                        alertDialog.show();
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (new BloodBankResponseModal(itemView.getContext()).getIsAccepted(TV_requesterReg.getText().toString()) == 0) {
                        if (new BloodBankResponseModal(itemView.getContext()).getIsRejected(TV_requesterReg.getText().toString()) == 0) {
                            builder.setTitle("Reject");
                            builder.setMessage("Are you sure to reject blood donation from this donor?");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                    new BloodBankModal(itemView.getContext()).rejectResponse(TV_requesterReg.getText().toString());
                                    new BloodBankResponseModal(context).setIsRejected(TV_requesterReg.getText().toString());
                                    btnAccept.setTextColor(Color.parseColor("#999999"));
                                    Drawable drawableTopAccept = context.getResources().getDrawable(R.drawable.ic_action_accept_disabled);
                                    btnAccept.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopAccept, null, null);
                                    btnCancel.setTextColor(Color.parseColor("#999999"));
                                    Drawable drawableTopReject = context.getResources().getDrawable(R.drawable.ic_action_cancel_disabled);
                                    btnCancel.setCompoundDrawablesWithIntrinsicBounds(null, drawableTopReject, null, null);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setTitle("Reject");
                            builder1.setMessage("You have already rejected blood donation from this donor");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                }
                            });
                            alertDialog = builder1.create();
                            alertDialog.show();
                        }
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("Reject");
                        builder1.setMessage("You have already accepted blood donation from this donor");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                            }
                        });
                        alertDialog = builder1.create();
                        alertDialog.show();
                    }
                }
            });

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:" + TV_requesterContact.getText().toString().trim()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
