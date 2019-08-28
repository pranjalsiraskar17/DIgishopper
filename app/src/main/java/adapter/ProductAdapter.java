package adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sagar.digishopper.AllProduct;
import com.project.sagar.digishopper.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fragment.ProductHomePageFragment;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    ArrayList<AllProduct> products;

    public ProductAdapter(Context context, ArrayList<AllProduct> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_recycler_layout, parent, false);
        ProductViewHolder productViewHolder=new ProductViewHolder(view);
        return productViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductHomePageFragment productHomePageFragment=new ProductHomePageFragment();
                Bundle bundle=new Bundle();
                bundle.putString("productId",products.get(position).getProduct_id());
                productHomePageFragment.setArguments(bundle);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.productHomeContainer,productHomePageFragment,productHomePageFragment.TAG)
                        .addToBackStack(null)
                        .commit();

            }
        });
        holder.productName.setText(products.get(position).getProduct_name());
        Picasso.with(context).load(products.get(position).getProduct_image()).into(holder.productImage);
        int selling=Integer.parseInt(products.get(position).getProduct_selling_price());
        int base=Integer.parseInt(products.get(position).getProduct_base_price());
        int save=base-selling;
        holder.textView_ProductPrice.setText(context.getResources().getString(R.string.Rs)+selling);

        if(save!=0)
        {
           if(save>0)
            {
                holder.textView_ProductBasePrice.setText(context.getResources().getString(R.string.Rs)+base);
                holder.textView_ProductBasePrice.setPaintFlags(holder.textView_ProductBasePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.textView_ProductsavePrice.setText("You will save "+context.getResources().getString(R.string.Rs)+save);
                int offerPer=(save*100)/base;
                if(offerPer>1)
                {
                    holder.offerlabel.setBackground(context.getDrawable(R.drawable.ic_local_offer_black_24dp));
                    holder.offerlabel.setText(offerPer+"%\noff");
                }
            }
        }



    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        LinearLayout mainLayout;
        TextView productName,textView_ProductPrice,textView_ProductBasePrice,textView_ProductsavePrice,offerlabel;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout=itemView.findViewById(R.id.main_layout);
            productImage=itemView.findViewById(R.id.imageProduct);
            productName=itemView.findViewById(R.id.txtPrdName);
            textView_ProductPrice=itemView.findViewById(R.id.txt_product_price);
            textView_ProductBasePrice=itemView.findViewById(R.id.txt_product_base_price);
            textView_ProductsavePrice=itemView.findViewById(R.id.txt_product_save_price);
            offerlabel=itemView.findViewById(R.id.imgOff);
        }
    }
}
