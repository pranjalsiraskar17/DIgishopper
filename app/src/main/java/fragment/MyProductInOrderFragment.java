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
import com.project.sagar.digishopper.MyProductInOrderClass;
import com.project.sagar.digishopper.R;

import java.util.ArrayList;

import adapter.MyOrderAdapter;
import adapter.MyProductInOrderAdapter;

public class MyProductInOrderFragment extends Fragment {
    public static final String TAG= MyProductInOrderFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<MyProductInOrderClass> myProductInOrderClassArrayList;
    private MyProductInOrderAdapter myProductInOrderAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_product_in_order_fragment,container,false);
        recyclerView=view.findViewById(R.id.recycler_my_product_in_order);
        Bundle bundle=getArguments();
        final String txn=bundle.getString("txnId");
        DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(txn);
        Query query=ordersRef.orderByKey().startAt("DG").endAt("DG"+"\uF8FF");
        myProductInOrderClassArrayList=new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MyProductInOrderClass myProductInOrderClass=postSnapshot.getValue(MyProductInOrderClass.class);
                    myProductInOrderClassArrayList.add(myProductInOrderClass);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                myProductInOrderAdapter=new MyProductInOrderAdapter(getActivity(),myProductInOrderClassArrayList);
                recyclerView.setAdapter(myProductInOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return  view;
    }
}
