package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.R;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.ShoppingCartAdapter;

public class ShoppingCartFragment extends Fragment {
    public static final String TAG=ShoppingCartFragment.class.getSimpleName();
    private RecyclerView recyclerView_cart;
    public static TextView cart_amount_txt;
    public Button btn_checkout;
    private ArrayList<CartProduct> cartProducts =new ArrayList<>();
    private ShoppingCartAdapter adapter;
    public static int cart_amt;
    DatabaseReference dbr;
    HashMap<String, ArrayList<String>> prdmap=new HashMap<>();
    ArrayList<String> prdlist=new ArrayList<>();
    ArrayList<String> qtylist=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.shopping_cart_layout,container,false);
        recyclerView_cart=view.findViewById(R.id.cart_recyclerview);
        btn_checkout=view.findViewById(R.id.button_checkout);
        cart_amount_txt=view.findViewById(R.id.cartamount_txt);

        dbr= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
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

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dbr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        cartProducts.clear();

                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            CartProduct cp=dataSnapshot1.getValue(CartProduct.class);
                            cartProducts.add(cp);

                        }
                        for(int i=0;i<cartProducts.size();i++)
                        {
                            prdlist.add(cartProducts.get(i).getProduct_id());
                            qtylist.add(String.valueOf(cartProducts.get(i).getProduct_qty()));
                        }
                        prdmap.put("prd",prdlist);
                        prdmap.put("qty",qtylist);
                        ((HomeDrawableActivity)getActivity()).showMapFragment(prdmap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });





        return view;
    }
}
