package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.sagar.digishopper.AllProduct;
import com.project.sagar.digishopper.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

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
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.productName.setText(products.get(position).getProduct_name());
        Picasso.with(context).load(products.get(position).getProduct_image()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName,productDesc,productPrice,productCate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=(ImageView) itemView.findViewById(R.id.imageProduct);
            productName=(TextView) itemView.findViewById(R.id.txtPrdName);
            productDesc=(TextView) itemView.findViewById(R.id.txtPrdDesc);
            productPrice=(TextView) itemView.findViewById(R.id.txtPrdPrice);
            productCate=(TextView) itemView.findViewById(R.id.txtPrdcate);
        }
    }
}
