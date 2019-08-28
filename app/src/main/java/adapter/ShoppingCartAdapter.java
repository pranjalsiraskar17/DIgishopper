package adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.CartProduct;
import com.project.sagar.digishopper.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import fragment.ShoppingCartFragment;

import static com.project.sagar.digishopper.LastSeenTime.getTimeAgo;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.CartViewHolder> {
    private Context context;
    private String uid;
    private ArrayList<CartProduct> cartProducts;
    private int total;
    public ShoppingCartAdapter(Context context, ArrayList<CartProduct> cartProducts) {
        this.context = context;
        this.cartProducts = cartProducts;
    }


    @NonNull
    @Override
    public ShoppingCartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_recyclerview_layout,parent,false);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        CartViewHolder holder=new CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShoppingCartAdapter.CartViewHolder holder, final int position) {
        final String prdid=cartProducts.get(position).getProduct_id();
        DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("ProductInfo").child(prdid);

        holder.imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbr =FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Mycart").child(prdid);
                dbr.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Product Removed From Cart", Toast.LENGTH_SHORT).show();
                       // cartProducts.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final DatabaseReference dbr=FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                        .child("Mycart").child(cartProducts.get(position).getProduct_id()).child("product_qty");
                Picasso.with(context).load(dataSnapshot.child("product_image").getValue().toString()).into(holder.imageView_product);
                holder.textView_name.setText(dataSnapshot.child("product_name").getValue().toString());
                holder.textView_timestamp.setText(getTimeAgo(cartProducts.get(position).getTimestamp(),context));
                holder.textView_qty.setText(String.valueOf(cartProducts.get(position).getProduct_qty()));

                int selling=Integer.parseInt(dataSnapshot.child("product_selling_price").getValue().toString());
                int base=Integer.parseInt(dataSnapshot.child("product_base_price").getValue().toString());
                int save=base-selling;
                total+=selling*cartProducts.get(position).getProduct_qty();
                ShoppingCartFragment.cart_amount_txt.setText(context.getResources().getString(R.string.Rs)+total);
                holder.textView_price.setText(context.getResources().getString(R.string.Rs)+selling);
                if(save>0)
                {
                    holder.textView_baseprice.setPaintFlags(holder.textView_baseprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.textView_baseprice.setText(context.getResources().getString(R.string.Rs)+base);
                }

                holder.img_button_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int no=Integer.parseInt( holder.textView_qty.getText().toString());
                        dbr.setValue(no+1);
                        notifyDataSetChanged();
                    }
                });

                holder.img_button_rm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            int n=Integer.parseInt( holder.textView_qty.getText().toString());
                            if(n>1)
                            {
                                dbr.setValue(n-1);
                                notifyDataSetChanged();
                            }


                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_product,imageView_close;
        private TextView textView_name,textView_timestamp,textView_price,textView_baseprice;
        private ImageView img_button_add,img_button_rm;
        private TextView textView_qty;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_product=itemView.findViewById(R.id.imageView_product);
            imageView_close=itemView.findViewById(R.id.closeImgView);
            textView_name=itemView.findViewById(R.id.product_name_txt);
            textView_price=itemView.findViewById(R.id.product_price_txt);
            textView_baseprice=itemView.findViewById(R.id.product_baseprice_txt);
            textView_qty=itemView.findViewById(R.id.product_qty_txt);
            img_button_add=itemView.findViewById(R.id.img_button_add);
            img_button_rm=itemView.findViewById(R.id.img_button_rm);
            textView_timestamp=itemView.findViewById(R.id.product_timstamp_txt);


        }
    }
}
