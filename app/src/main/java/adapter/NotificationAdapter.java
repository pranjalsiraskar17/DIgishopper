package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sagar.digishopper.NotificationClass;
import com.project.sagar.digishopper.R;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewholder> {
    Context context;
    ArrayList<NotificationClass>notificationClasses;

    public NotificationAdapter(Context context,ArrayList<NotificationClass> notificationClasses ){
        this.context=context;
        this.notificationClasses=notificationClasses;
    }


    @NonNull
    @Override
    public NotificationViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_notification,parent,false);
        NotificationViewholder notificationViewholder=new NotificationViewholder(view);
        return notificationViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewholder holder, int position) {
        Date date=new Date(notificationClasses.get(position).getTimestamp());
        holder.nft_timestamp.setText(String.valueOf( new SimpleDateFormat("dd-MM-yyyy").format(date)));
        if(notificationClasses.get(position).getOrder_status().equals("ordered")){
            holder.notification.setText("Your order is waiting to accept by shopkeeper");
        }
        else  if (notificationClasses.get(position).getOrder_status().equals("Delivering")){
            holder.notification.setText("Your order is on the way");
        }
        else  if (notificationClasses.get(position).getOrder_status().equals("Delivered")){
            holder.notification.setText("Your order is delivered successfully");
        }
        else  if (notificationClasses.get(position).getOrder_status().equals("Rejected")){
            holder.notification.setText("Your order is rejected by shopkeeper");
        }

    }

    @Override
    public int getItemCount() {
        return notificationClasses.size();
    }

    public class NotificationViewholder extends RecyclerView.ViewHolder {
        TextView notification,nft_timestamp;
        LinearLayout layout;
        public NotificationViewholder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.nft);
            notification=itemView.findViewById(R.id.notification);
            nft_timestamp=itemView.findViewById(R.id.nft_time);
        }
    }
}
