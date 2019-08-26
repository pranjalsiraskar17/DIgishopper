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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.R;

public class ProductBillFragment extends Fragment {
    public static final String TAG=ProductBillFragment.class.getSimpleName();
    TextView basePrice,offerPrice,discountPrice,totalPrice,deliveryPrice;
    Button bookBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product_bill_fragment_layout,container,false);
        basePrice=(TextView)view.findViewById(R.id.baseTxt);
        offerPrice=(TextView)view.findViewById(R.id.discpriceTxt);
        discountPrice=(TextView)view.findViewById(R.id.discountTxt);
        totalPrice=(TextView)view.findViewById(R.id.totalTxt);
        deliveryPrice=(TextView)view.findViewById(R.id.deliveryTxt);
        bookBtn=(Button)view.findViewById(R.id.bookBtn);
        Bundle bundle=getArguments();
        final String prdid=bundle.getString("prdidToBookOrder");
        final String address=bundle.getString("address");
        final String name=bundle.getString("name");
        final String mobile=bundle.getString("mobile");
        DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("ProductInfo").child(prdid);
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int base=Integer.parseInt(dataSnapshot.child("product_base_price").getValue().toString());
                int selling=Integer.parseInt(dataSnapshot.child("product_selling_price").getValue().toString());
                basePrice.setText(getActivity().getResources().getString(R.string.Rs)+String.valueOf(base));
                offerPrice.setText(getActivity().getResources().getString(R.string.Rs)+String.valueOf(selling));
                discountPrice.setText(getActivity().getResources().getString(R.string.Rs)+String.valueOf(base-selling));
                deliveryPrice.setText(getActivity().getResources().getString(R.string.Rs)+"30");
                totalPrice.setText(getActivity().getResources().getString(R.string.Rs)+String.valueOf(selling+30));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference txtCounterRef=FirebaseDatabase.getInstance().getReference().child("txtCounter");
                txtCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String userKey= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final int txnid=Integer.parseInt(dataSnapshot.getValue().toString())+1;
                                DatabaseReference txnRef=FirebaseDatabase.getInstance().getReference().child("Orders").child("TXN"+txnid);
                                txnRef.child("txn_id").setValue("TXN"+txnid);
                                txnRef.child("prd_id").setValue(prdid);
                                txnRef.child("order_status").setValue("ordered");
                                txnRef.child("order_address").setValue(address);
                                txnRef.child("txn_timestamp").setValue(ServerValue.TIMESTAMP);
                                txnRef.child("buyer_name").setValue(name);
                                txnRef.child("buyer_phone").setValue(mobile);
                                txnRef.child("buyer_userkey").setValue(userKey)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                txtCounterRef.setValue(txnid);
                                                DatabaseReference odersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Myorders").child("TXN"+txnid);
                                                odersRef.child("order_status").setValue("ordered");
                                                odersRef.child("txn_id").setValue("TXN"+txnid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity(), "Booking Done !", Toast.LENGTH_SHORT).show();

                                                        ((HomeDrawableActivity)getActivity()).removeAllFragment();

                                                    }
                                                });
                                            }
                                        });


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
