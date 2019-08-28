package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sagar.digishopper.MyOrderClass;
import com.project.sagar.digishopper.R;
import java.util.ArrayList;

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

        holder.txtPrdId.setText("Product ID");
        holder.txtPrdIdValue.setText(myOrderClasses.get(position).getPrd_id());
        holder.order_id.setText("Order_ID");
        holder.order_id_value.setText(myOrderClasses.get(position).getTxn_id());
        holder.order_status.setText("Order Status");
        holder.order_status_value.setText(myOrderClasses.get(position).getOrder_status());


    }

    @Override
    public int getItemCount() {
        return myOrderClasses.size();
    }

    public class MyOrderViewholder extends RecyclerView.ViewHolder {
        TextView txtPrdId,txtPrdIdValue,order_id,order_id_value,order_address,order_address_value,order_status,order_status_value;

        public MyOrderViewholder(@NonNull View itemView) {
            super(itemView);
            txtPrdId=itemView.findViewById(R.id.txtPrdId);
            txtPrdIdValue=itemView.findViewById(R.id.txtPrdIdValue);
            order_id=itemView.findViewById(R.id.order_id);
            order_id_value=itemView.findViewById(R.id.order_id_value);
            order_address=itemView.findViewById(R.id.order_address);
            order_address_value=itemView.findViewById(R.id.order_address_value);
            order_status=itemView.findViewById(R.id.order_status);
            order_status_value=itemView.findViewById(R.id.order_status_value);
        }
    }
}
