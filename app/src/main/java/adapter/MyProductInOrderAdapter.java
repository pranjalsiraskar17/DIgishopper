package adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.MyProductInOrderClass;
import com.project.sagar.digishopper.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyProductInOrderAdapter extends RecyclerView.Adapter<MyProductInOrderAdapter.MyProductInOrderViewHolder> {

    Context context;
    ArrayList<MyProductInOrderClass> myProductInOrderClassArrayList;

    public MyProductInOrderAdapter(Context context,ArrayList<MyProductInOrderClass> myProductInOrderClass) {
        this.context=context;
        this.myProductInOrderClassArrayList=myProductInOrderClass;
    }

    @NonNull
    @Override
    public MyProductInOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_recycler_product_in_order, parent, false);
        MyProductInOrderViewHolder myProductInOrderViewHolder=new MyProductInOrderViewHolder(view);
        return myProductInOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyProductInOrderViewHolder holder, int position) {
        holder.product_id.setText(myProductInOrderClassArrayList.get(position).getPrd_id());
        holder.product_qty.setText(String.valueOf(myProductInOrderClassArrayList.get(position).getProduct_qty()));
        holder.order_status.setText(myProductInOrderClassArrayList.get(position).getOrder_status()+" on");
        Date date=new Date(myProductInOrderClassArrayList.get(position).getTxn_timestamp());
        holder.order_time.setText(String.valueOf( new SimpleDateFormat("dd-MM-yyyy").format(date)));
        DatabaseReference productInfo= FirebaseDatabase.getInstance().getReference().child("ProductInfo").child(myProductInOrderClassArrayList.get(position).getPrd_id());
        productInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uri=dataSnapshot.child("product_image").getValue().toString();
                Picasso.with(context).load(uri).into(holder.imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return myProductInOrderClassArrayList.size();
    }

    public class MyProductInOrderViewHolder extends RecyclerView.ViewHolder {
        TextView product_id,product_qty,order_status,order_time;
        ImageView imageView;
        public MyProductInOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            product_id=itemView.findViewById(R.id.product_id_value);
            product_qty=itemView.findViewById(R.id.product_qty);
            order_status=itemView.findViewById(R.id.order_status_value);
            order_time=itemView.findViewById(R.id.order_time_value);
            imageView=itemView.findViewById(R.id.imageOrder);
        }
    }
}
