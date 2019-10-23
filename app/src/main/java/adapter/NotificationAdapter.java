package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sagar.digishopper.NotificationClass;

import java.util.ArrayList;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notificationClasses.size();
    }

    public class NotificationViewholder extends RecyclerView.ViewHolder {
        public NotificationViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
