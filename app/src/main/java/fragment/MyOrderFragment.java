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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.MyOrderClass;
import com.project.sagar.digishopper.R;

import java.util.ArrayList;

import adapter.MyOrderAdapter;

public class MyOrderFragment extends Fragment {
    public static final String TAG=MapFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView heading;
    private ArrayList<MyOrderClass>myOrderClasses;
    private MyOrderAdapter myOrderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_order_fragment, container, false);
        heading=view.findViewById(R.id.heading);
        heading.setText("My Orders");
        recyclerView=view.findViewById(R.id.recycler_myorder);
        DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        Bundle bundle=getArguments();
        myOrderClasses=new ArrayList<>();
        final String userID=bundle.getString("userid");
        Query query=ordersRef.orderByChild("buyer_userkey").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MyOrderClass myOrderClass = postSnapshot.getValue(MyOrderClass.class);
                    myOrderClasses.add(myOrderClass);
                }
                myOrderAdapter=new MyOrderAdapter(getActivity(),myOrderClasses);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(myOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
