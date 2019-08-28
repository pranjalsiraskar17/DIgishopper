package fragment;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductHomePageFragment extends Fragment {
    public static final String TAG=ProductHomePageFragment.class.getSimpleName();
    private TextView textView_ProductName,textView_Desc,textView_ProductPrice,textView_ProductBasePrice,textView_ProductsavePrice,offerLabel;
    private ViewFlipper imageFlipper;
    private EditText editText_qty;
    private Button buy_button,addcart_button;
    private float startX;
    private float startY;
    private int CLICK_ACTION_THRESHOLD = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product_home_fragment_layout,container,false);
        textView_ProductName=view.findViewById(R.id.txtProductName);
        textView_ProductPrice=view.findViewById(R.id.txt_product_price);
        textView_ProductBasePrice=view.findViewById(R.id.txt_product_base_price);
        textView_ProductsavePrice=view.findViewById(R.id.txt_product_save_price);
        textView_Desc=view.findViewById(R.id.txt_description);
        imageFlipper=view.findViewById(R.id.imagesFlipper);
        offerLabel=view.findViewById(R.id.fragmentImgOffer);
        buy_button=view.findViewById(R.id.button_buy);
        editText_qty=view.findViewById(R.id.qtyedittxt);
        addcart_button=view.findViewById(R.id.button_addcart);
        Bundle bundle=getArguments();
        final String prdid=bundle.getString("productId");
        if(prdid!=null)
        {
            DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("ProductInfo").child(prdid);
            StorageReference productImagesRef= FirebaseStorage.getInstance().getReference().child("ProductImages").child(prdid);
            for(int i=1;i<5;i++)
            {
                productImagesRef.child(i+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(getActivity()!=null)
                        {
                            flipImage(uri.toString());
                        }

                    }
                });

            }

            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    textView_ProductName.setText(dataSnapshot.child("product_name").getValue().toString());
                    int selling=Integer.parseInt(dataSnapshot.child("product_selling_price").getValue().toString());
                    int base=Integer.parseInt(dataSnapshot.child("product_base_price").getValue().toString());
                    int save=base-selling;
                    textView_ProductPrice.setText(getResources().getString(R.string.Rs)+selling);
                    textView_ProductBasePrice.setText(getResources().getString(R.string.Rs)+base);
                    textView_ProductBasePrice.setPaintFlags(textView_ProductBasePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    if(save>0)
                    {
                        textView_ProductsavePrice.setText("You will save "+getResources().getString(R.string.Rs)+save);
                        int offerPer=(save*100)/base;
                        if(offerPer>1)
                        {
                            offerLabel.setBackground(getActivity().getDrawable(R.drawable.ic_local_offer_black_24dp));
                            offerLabel.setText(offerPer+"%\noff");
                        }
                    }

                    String desc="Category       :"+dataSnapshot.child("product_category").getValue().toString()+"\n"+
                            "Product Info :"+dataSnapshot.child("product_desc").getValue().toString()+"\n"+
                            "Product ID    :"+prdid;

                    textView_Desc.setText(desc);





                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                ((HomeDrawableActivity)getActivity()).showMapFragment(prdid,Integer.parseInt(editText_qty.getText().toString()));

            }
        });

        addcart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbr =FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Mycart").child(prdid);
                dbr.child("product_id").setValue(prdid);
                dbr.child("timestamp").setValue(ServerValue.TIMESTAMP);
                dbr.child("product_qty").setValue(Integer.parseInt(editText_qty.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getActivity(), "Added To Cart", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });



        return view;
    }



    private ArrayList<String> getAllDownloadUrl(StorageReference productImagesRef) {
        final ArrayList<String> list=new ArrayList<>();

        return list;
    }



    private void flipImage(final String image_url){

        ImageView view=new ImageView(getActivity());
        Picasso.with(getActivity()).load(image_url).into(view);
        imageFlipper.addView(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        float endY = event.getY();

                        if (isAClick(startX, endX, startY, endY)) {

                            Toast.makeText(getActivity(), image_url, Toast.LENGTH_SHORT).show();
                        }

                        //swipe right
                        if (startX < endX) {
                            imageFlipper.showNext();
                        }

                        //swipe left
                        if (startX > endX) {
                            imageFlipper.showPrevious();
                        }

                        break;

                }
                return true;
            }
        });

    }
    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }
}
