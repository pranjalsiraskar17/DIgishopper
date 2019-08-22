package adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.project.sagar.digishopper.AllProduct;
import com.project.sagar.digishopper.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fragment.ProductHomePageFragment;


public class OfferListAdapter extends ArrayAdapter<AllProduct> {
    private Context context;
    private ArrayList<AllProduct> productList;
    private int resource;

    public OfferListAdapter(ArrayList<AllProduct> productList,@NonNull Context context, int resource) {
        super(context, resource);
        this.productList=productList;
        this.context=context;
        this.resource=resource;
    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView productImage;
        LinearLayout mainLayout;
        TextView productName,textView_ProductPrice,textView_ProductBasePrice,textView_ProductsavePrice,offerlabel;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(resource, parent, false);
            mainLayout=convertView.findViewById(R.id.main_layout);
            productImage=convertView.findViewById(R.id.imageProduct);
            productName=convertView.findViewById(R.id.txtPrdName);
            textView_ProductPrice=convertView.findViewById(R.id.txt_product_price);
            textView_ProductBasePrice=convertView.findViewById(R.id.txt_product_base_price);
            textView_ProductsavePrice=convertView.findViewById(R.id.txt_product_save_price);
            offerlabel=convertView.findViewById(R.id.imgOff);
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductHomePageFragment productHomePageFragment=new ProductHomePageFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("productId",productList.get(position).getProduct_id());
                    productHomePageFragment.setArguments(bundle);
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.productHomeContainer,productHomePageFragment,productHomePageFragment.TAG)
                            .addToBackStack(null)
                            .commit();

                }
            });
            productName.setText(productList.get(position).getProduct_name());
            Picasso.with(context).load(productList.get(position).getProduct_image()).into(productImage);
            int selling=Integer.parseInt(productList.get(position).getProduct_selling_price());
            int base=Integer.parseInt(productList.get(position).getProduct_base_price());
            int save=base-selling;
            textView_ProductPrice.setText(context.getResources().getString(R.string.Rs)+String.valueOf(selling));
            textView_ProductBasePrice.setText(context.getResources().getString(R.string.Rs)+String.valueOf(base));
            if(save!=0)
            {
                textView_ProductBasePrice.setPaintFlags(textView_ProductBasePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                if(save>0)
                {
                    textView_ProductsavePrice.setText("You will save "+context.getResources().getString(R.string.Rs)+save);
                    int offerPer=(save*100)/base;
                    if(offerPer>1)
                    {
                        offerlabel.setBackground(context.getDrawable(R.drawable.ic_local_offer_black_24dp));
                        offerlabel.setText(offerPer+"%\noff");
                    }
                }
            }
        }


        return convertView;
    }
}