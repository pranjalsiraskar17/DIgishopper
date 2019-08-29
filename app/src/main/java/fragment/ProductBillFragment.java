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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductBillFragment extends Fragment {
    public static final String TAG=ProductBillFragment.class.getSimpleName();
    TextView basePrice,offerPrice,discountPrice,totalPrice,deliveryPrice;
    Button bookBtn;
    HashMap<String,ArrayList<String>> map=new HashMap<>();
    String address,name,mobile;
    int base,selling;
    ArrayList<String> prdidlist=new ArrayList<>();
    ArrayList<String> qtylist=new ArrayList<>();
    int txnid;
    int j;
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
        if(bundle!=null)
        {
            map= (HashMap<String, ArrayList<String>>) bundle.getSerializable("prdmap");
            address=bundle.getString("address");
            name=bundle.getString("name");
            mobile=bundle.getString("mobile");
            prdidlist=map.get("prd");
            qtylist=map.get("qty");
            for(int i=0;i<prdidlist.size();i++)
            {
                String id=map.get("prd").get(i);
                final int qty=Integer.parseInt(qtylist.get(i));
                DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("ProductInfo").child(id);
                dbr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        base+=Integer.parseInt(dataSnapshot.child("product_base_price").getValue().toString())*qty;
                        selling+=Integer.parseInt(dataSnapshot.child("product_selling_price").getValue().toString())*qty;
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
            }
        }


        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference txtCounterRef=FirebaseDatabase.getInstance().getReference().child("txtCounter");
                txtCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String userKey= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final DatabaseReference cartref=FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Mycart");
                                txnid=Integer.parseInt(dataSnapshot.getValue().toString())+1;
                                DatabaseReference txnRef=FirebaseDatabase.getInstance().getReference().child("Orders").child("TXN"+txnid);
                                txnRef.child("txn_id").setValue("TXN"+txnid);
                                txnRef.child("order_address").setValue(address);
                                txnRef.child("buyer_name").setValue(name);
                                txnRef.child("buyer_phone").setValue(mobile);
                                txnRef.child("buyer_userkey").setValue(userKey);
                                for(j=0;j<prdidlist.size();j++)
                                {

                                    final String id=prdidlist.get(j);
                                    int qty=Integer.parseInt(qtylist.get(j));

                                    txnRef.child(id).child("prd_id").setValue(id);
                                    txnRef.child(id).child("order_status").setValue("ordered");
                                    txnRef.child(id).child("product_qty").setValue(qty);
                                    txnRef.child(id).child("txn_timestamp").setValue(ServerValue.TIMESTAMP)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    txtCounterRef.setValue(txnid);
                                                    DatabaseReference odersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Myorders").child("TXN"+txnid);
                                                    odersRef.child("order_status").setValue("ordered");
                                                    odersRef.child("txn_id").setValue("TXN"+txnid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            cartref.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                        Toast.makeText(getActivity(), "Booking Done !", Toast.LENGTH_SHORT).show();
                                                                        ((HomeDrawableActivity)getActivity()).removeAllFragment();

                                                                }
                                                            });


                                                        }
                                                    });
                                                }
                                            });
                                }

                                txnRef.child("txn_id").setValue("TXN"+txnid);
                                txnRef.child("order_address").setValue(address);
                                txnRef.child("buyer_name").setValue(name);
                                txnRef.child("buyer_phone").setValue(mobile);
                                txnRef.child("buyer_userkey").setValue(userKey);



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                final DatabaseReference nftCounter=FirebaseDatabase.getInstance().getReference().child("nftCounter");
                nftCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String userKey= FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final int nftid=Integer.parseInt(dataSnapshot.getValue().toString())+1;
                        DatabaseReference nftRef=FirebaseDatabase.getInstance().getReference().child("Notifications").child("Merchant").child("NFT"+nftid);
                        nftRef.child("txn_id").setValue("TXN"+txnid);
                        nftRef.child("order_type").setValue("Ordered");
                        nftRef.child("txn_timestamp").setValue(ServerValue.TIMESTAMP);
                        nftRef.child("buyer_userkey").setValue(userKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                nftCounter.setValue(nftid);
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
