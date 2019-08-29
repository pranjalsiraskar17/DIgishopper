package adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sagar.digishopper.MyOrderClass;
import com.project.sagar.digishopper.R;
import java.util.ArrayList;

import fragment.MyProductInOrderFragment;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewholder> {
    Context context;
    ArrayList<MyOrderClass> myOrderClasses;

    public MyOrderAdapter(Context context, ArrayList<MyOrderClass> myOrderClasses) {
        this.context = context;
        this.myOrderClasses = myOrderClasses;
    }

    @NonNull
    @Override
    public MyOrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_recycler_order, parent, false);
        MyOrderViewholder myOrderViewholder=new MyOrderViewholder(view);
        return myOrderViewholder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.MyOrderViewholder holder, final int position) {


        holder.order_id_value.setText(myOrderClasses.get(position).getTxn_id());
        holder.order_status_value.setText(myOrderClasses.get(position).getOrder_status());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProductInOrderFragment myProductInOrderFragment=new MyProductInOrderFragment();
                Bundle bundle=new Bundle();
                bundle.putString("txnId",myOrderClasses.get(position).getTxn_id());
                myProductInOrderFragment.setArguments(bundle);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.productHomeContainer,myProductInOrderFragment,myProductInOrderFragment.TAG)
                        .addToBackStack(null)
                        .commit();

            }
        });


    }

    @Override
    public int getItemCount() {
        return myOrderClasses.size();
    }

    public class MyOrderViewholder extends RecyclerView.ViewHolder {
        TextView order_id,order_id_value,order_status,order_status_value;
        LinearLayout layout;
        public MyOrderViewholder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.orders);
            order_id=itemView.findViewById(R.id.order_id);
            order_id_value=itemView.findViewById(R.id.order_id_value);
            order_status=itemView.findViewById(R.id.order_status);
            order_status_value=itemView.findViewById(R.id.order_status_value);
        }
    }
}
