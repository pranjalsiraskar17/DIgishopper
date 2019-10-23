package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.NotificationClass;
import com.project.sagar.digishopper.R;
import java.util.ArrayList;

import adapter.NotificationAdapter;

public class NotificationFragment extends Fragment {
    public static final String TAG= NotificationFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView heading;
    private ArrayList<NotificationClass> notificationClasses;
    private NotificationFragment notificationFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.notification_fragment, container, false);
        heading=view.findViewById(R.id.heading);
        heading.setText("Notification");
        recyclerView=view.findViewById(R.id.recycler_notification);
        final String user_key= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference().child(user_key).child("Notification");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    NotificationClass notificationClass = postSnapshot.getValue(NotificationClass.class);
                    notificationClasses.add(notificationClass);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                NotificationAdapter notificationAdapter=new NotificationAdapter(getActivity(),notificationClasses);
                recyclerView.setAdapter(notificationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}
