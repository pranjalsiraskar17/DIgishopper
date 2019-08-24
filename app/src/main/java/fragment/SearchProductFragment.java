package fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.AllProduct;
import com.project.sagar.digishopper.R;

import java.util.ArrayList;

import adapter.ProductAdapter;

public class SearchProductFragment extends Fragment {
    public static final String TAG=SignUpPageFragment.class.getSimpleName();
    private ImageView back;
    private EditText searchBarInFragment;
    private RecyclerView recyclerViewFragment;
    private ArrayList<AllProduct> productsList=new ArrayList<AllProduct>();
    private ProductAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.search_fragment_layout,container,false);
        back=(ImageView)view.findViewById(R.id.backOnCloseFragment);
        searchBarInFragment=(EditText)view.findViewById(R.id.searchViewFragment);
        recyclerViewFragment=(RecyclerView)view.findViewById(R.id.recyclerViewFragment);
        if(getArguments()!=null)
        {
            Bundle bundle=getArguments();
            searchProduct(bundle.getString("prdquery"));
            searchBarInFragment.setText(bundle.getString("prdquery"));
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchBarInFragment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchProduct(searchBarInFragment.getText().toString());
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private void searchProduct(String query) {
        Query dbquery= FirebaseDatabase.getInstance().getReference().child("ProductInfo").orderByChild("product_name").startAt(query).endAt(query+"\uf8ff");

        dbquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productsList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    AllProduct p=dataSnapshot1.getValue(AllProduct.class);
                    productsList.add(p);
                }
                adapter=new ProductAdapter(getActivity(),productsList);
                recyclerViewFragment.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewFragment.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
