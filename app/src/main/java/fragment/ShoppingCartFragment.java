package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.CartProduct;
import com.project.sagar.digishopper.R;

import java.util.ArrayList;

import adapter.ShoppingCartAdapter;

public class ShoppingCartFragment extends Fragment {
    public static final String TAG=ShoppingCartFragment.class.getSimpleName();
    private RecyclerView recyclerView_cart;
    private ArrayList<CartProduct> cartProducts =new ArrayList<>();
    private ShoppingCartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.shopping_cart_layout,container,false);
        recyclerView_cart=view.findViewById(R.id.cart_recyclerview);
        DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Mycart");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cartProducts.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    CartProduct cp=dataSnapshot1.getValue(CartProduct.class);
                    cartProducts.add(cp);
                }
                adapter=new ShoppingCartAdapter(getActivity(),cartProducts);
                recyclerView_cart.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView_cart.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}
