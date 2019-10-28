package fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.project.sagar.fcmnotifier.FCMNotifier;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductBillFragment extends Fragment {
    public static final String TAG=ProductBillFragment.class.getSimpleName();
    TextView basePrice,offerPrice,discountPrice,totalPrice,deliveryPrice;
    Button bookBtn;
    HashMap<String,ArrayList<String>> map=new HashMap<>();
    String address,name,mobile;
    DatabaseReference txnRef;
    int base,selling;
    ArrayList<String> prdidlist=new ArrayList<>();
    ArrayList<String> qtylist=new ArrayList<>();
    int txnid,nftid,nftcounter;
    String merchantkey="LmMMtotcU5TphTDMtLVxBbrJRfC2";
    int j;
    String token="",msg="";
    ProgressDialog progressDialog;
    Boolean isCompleted=false;

    public ProductBillFragment() {
    }

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



        FirebaseDatabase.getInstance().getReference().child("merchant_token_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    token=dataSnapshot.getValue().toString();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("Product booking processing...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                final DatabaseReference txtCounterRef=FirebaseDatabase.getInstance().getReference().child("txtCounter");
                txtCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String userKey= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final DatabaseReference cartref=FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Mycart");
                                txnid=Integer.parseInt(dataSnapshot.getValue().toString())+1;
                                txnRef=FirebaseDatabase.getInstance().getReference().child("Orders").child("TXN"+txnid);
                                txnRef.child("txn_id").setValue("TXN"+txnid);
                                txnRef.child("txt_amt").setValue(Integer.parseInt(String.valueOf(base)));
                                txnRef.child("order_address").setValue(address);
                                txnRef.child("order_status").setValue("ordered");
                                txnRef.child("buyer_name").setValue(name);
                                txnRef.child("buyer_phone").setValue(mobile);
                                txnRef.child("buyer_userkey").setValue(userKey);
                                txnRef.child("txn_timestamp").setValue(ServerValue.TIMESTAMP);

                                for(j=0;j<prdidlist.size();j++)
                                {

                                    final String id=prdidlist.get(j);
                                    final int qty=Integer.parseInt(qtylist.get(j));
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("ProductInfo").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            txnRef.child(id).child("product_price").setValue(dataSnapshot.child("product_selling_price").getValue().toString());
                                            txnRef.child(id).child("prd_id").setValue(id);
                                            if(msg.length()>0)
                                            {
                                                msg+=","+dataSnapshot.child("product_name").getValue().toString();
                                            }
                                            else
                                            {
                                                msg+=dataSnapshot.child("product_name").getValue().toString();
                                            }

                                            txnRef.child(id).child("product_qty").setValue(qty)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            txtCounterRef.setValue(txnid);
                                                            final DatabaseReference nftCounterRef=FirebaseDatabase.getInstance().getReference().child("NftCounter");
                                                            nftCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                   nftid =Integer.parseInt(dataSnapshot.getValue().toString());
                                                                   nftcounter=nftid+1;
                                                                    DatabaseReference nftRef=FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Notification").child("NFT"+nftcounter);
                                                                    DatabaseReference nftmerchantRef=FirebaseDatabase.getInstance().getReference().child("Users").child(merchantkey).child("Notification").child("NFT"+nftcounter);
                                                                    nftRef.child("order_status").setValue("ordered");
                                                                    nftRef.child("nft_timestamp").setValue(ServerValue.TIMESTAMP);
                                                                    nftRef.child("isViewed").setValue("false");
                                                                    nftmerchantRef.child("order_status").setValue("ordered");
                                                                    nftmerchantRef.child("nft_timestamp").setValue(ServerValue.TIMESTAMP);
                                                                    nftmerchantRef.child("nft_buyer_name").setValue(name);
                                                                    nftmerchantRef.child("isViewed").setValue("false");
                                                                    nftCounterRef.setValue(nftcounter);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        //    nftRef.child("nft_id").setValue("NFT"+nftid);
                                                            DatabaseReference odersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).child("Myorders").child("TXN"+txnid);
                                                            odersRef.child("order_status").setValue("ordered");
                                                            odersRef.child("txn_id").setValue("TXN"+txnid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    cartref.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                progressDialog.dismiss();
                                                                                Toast.makeText(getActivity(), "Booking Done !", Toast.LENGTH_SHORT).show();
                                                                                ((HomeDrawableActivity)getActivity()).removeAllFragment();
                                                                                if(!isCompleted)
                                                                                {
//                                                                                    new Notify().execute();
                                                                                    FCMNotifier notifier=new FCMNotifier("AIzaSyCvSWo7HKk0XFC69J9QQTtCovkWzHcKm0M",token);
                                                                                    notifier.setTitle("New Order");
                                                                                    notifier.setMsg("You have order of "+msg);
                                                                                    if(notifier.send())
                                                                                    {
                                                                                        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                                                                                    }else
                                                                                    {
                                                                                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                    //notifier.send();


                                                                                    isCompleted=true;
                                                                                }

                                                                        }
                                                                    });


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

    public class Notify extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            try {

                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization","key=AIzaSyCvSWo7HKk0XFC69J9QQTtCovkWzHcKm0M");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();


                json.put("to", token);


                JSONObject info = new JSONObject();
                info.put("title", "New Order");   // NotificationFragment title
                info.put("body", "You have order of "+msg); // NotificationFragment body
                json.put("notification_fragment", info);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.getInputStream();

            } catch (Exception e) {
                Log.d("Error", "" + e);
            }


            return null;
        }

    }
}
