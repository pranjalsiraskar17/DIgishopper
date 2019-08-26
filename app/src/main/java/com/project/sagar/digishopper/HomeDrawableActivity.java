package com.project.sagar.digishopper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import Notification.APIService;
import Notification.Client;
import Notification.Data;
import Notification.MyResponce;
import Notification.Sender;
import Notification.Token;
import adapter.ProductAdapter;
import fragment.LoginPageFragment;
import fragment.MapFragment;
import fragment.MyAccountFragment;
import fragment.MyOrderFragment;
import fragment.ProductBillFragment;
import fragment.ProductHomePageFragment;
import fragment.SearchProductFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDrawableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView product_recyclerView;
    private DatabaseReference productRef;
    private ProductAdapter adapter;
    private ViewFlipper flipper;
    private float startX;
    private float startY;
    private int CLICK_ACTION_THRESHOLD = 200;
    private ArrayList<AllProduct> productsList=new ArrayList<AllProduct>();
    private TextView username;
    APIService apiService;
    FirebaseUser user;
    EditText productSearchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<String> imageList=new ArrayList<>();
        user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbruser=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        dbruser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username=findViewById(R.id.txtusername);
                String fname=dataSnapshot.child("user_fname").getValue().toString();
                String lname=dataSnapshot.child("user_lname").getValue().toString();
                username.setText(fname+" "+lname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageList.add("https://image.shutterstock.com/image-vector/isometric-users-buying-online-tablets-260nw-1154447980.jpg");
        imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREBV9TIH6ri4eqvG5jloZirQBOZop7KQ4b-tmMOyB43DTzE58m");
        imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ547F5phL6-3-Rd4JHXOzz5VKt6csQAiTPQKr39cj6rdglMQ91");
        imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR31LIrJBmaN8D3lftG_pplvE-ItXBy46zhkQ2Q-y1jfVn0PcOR");
        flipper=findViewById(R.id.mainFlipper);
        flipper.setFlipInterval(2500);
        flipper.setAutoStart(true);

        Animation imgAnimationIn = AnimationUtils.
                loadAnimation(this, android.R.anim.slide_in_left);
        imgAnimationIn.setDuration(700);
        flipper.setInAnimation(imgAnimationIn);

        Animation imgAnimationOut = AnimationUtils.
                loadAnimation(this, android.R.anim.slide_out_right);
        imgAnimationOut.setDuration(700);
        flipper.setOutAnimation(imgAnimationOut);



        for(int i=0;i<imageList.size();i++)
        {
            flipImage(imageList.get(i));
        }

        apiService = Client.getClient("https://fcm.googlepis.com/").create(APIService.class);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        product_recyclerView=findViewById(R.id.recyclerView);
        productSearchBar=(EditText) findViewById(R.id.searchView);
        productSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchProductFragment searchProductFragment=new SearchProductFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("prdquery",productSearchBar.getText().toString());
                    searchProductFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.productHomeContainer,searchProductFragment,searchProductFragment.TAG)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return false;
            }
        });

//        searchViewproductSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                SearchProductFragment searchProductFragment=new SearchProductFragment();
//                Bundle bundle=new Bundle();
//                bundle.putString("prdquery",query);
//                searchProductFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.productHomeContainer,searchProductFragment,searchProductFragment.TAG)
//                        .addToBackStack(null)
//                        .commit();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        productRef= FirebaseDatabase.getInstance().getReference().child("ProductInfo");
        Query query=productRef.orderByChild("productToAll").equalTo(true);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productsList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    AllProduct p=dataSnapshot1.getValue(AllProduct.class);
                    productsList.add(p);
                }
                adapter=new ProductAdapter(HomeDrawableActivity.this,productsList);
                product_recyclerView.setLayoutManager(new LinearLayoutManager(HomeDrawableActivity.this));
                product_recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeDrawableActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_drawable, menu);
        return true;
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myorder) {
            // Handle the camera action
            MyOrderFragment myOrderFragment =new MyOrderFragment();
            Bundle bundle = new Bundle();
            bundle.putString("userid",user.getUid());
            myOrderFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.productHomeContainer, myOrderFragment, myOrderFragment.TAG)
                    .addToBackStack(null)
                    .commit();


        } else if (id == R.id.nav_mynotification) {
            sendNotification(user.getUid(),"","");

        } else if (id == R.id.nav_mywishlist) {

        } else if (id == R.id.nav_myaccount) {
            MyAccountFragment myAccountFragment=new MyAccountFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.productHomeContainer,myAccountFragment,myAccountFragment.TAG)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.sign_out_menu) {
            FirebaseAuth.getInstance().signOut();
            Intent loginIntent=new Intent(HomeDrawableActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void flipImage(final String image_url){
        ImageView view=new ImageView(this);
        Picasso.with(this).load(image_url).into(view);
        flipper.addView(view);

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

                            Toast.makeText(HomeDrawableActivity.this, image_url, Toast.LENGTH_SHORT).show();
                        }

                        //swipe right
                        if (startX < endX) {
                            HomeDrawableActivity.this.flipper.showNext();
                        }

                        //swipe left
                        if (startX > endX) {
                            HomeDrawableActivity.this.flipper.showPrevious();
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

    private void updateToken(String refreshToken){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);
    }

    private void sendNotification(String receiver,String username,String message)
    {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(),R.mipmap.ic_launcher,"msg","title",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponce>() {
                                @Override
                                public void onResponse(Call<MyResponce> call, Response<MyResponce> response) {
                                    if(response.body().success !=1){
                                        Toast.makeText(HomeDrawableActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponce> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showMapFragment(String prdid)
    {
        MapFragment mapFragment=new MapFragment();
        Bundle bundle=new Bundle();
        bundle.putString("prdidToAddress",prdid);
        mapFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.productHomeContainer,mapFragment,mapFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
    public void showProductBillFragment(String prdid,String address,String name,String mobile)
    {
        ProductBillFragment productBillFragment=new ProductBillFragment();
        Bundle bundle=new Bundle();
        bundle.putString("prdidToBookOrder",prdid);
        bundle.putString("address",address);
        bundle.putString("name",name);
        bundle.putString("mobile",mobile);
        productBillFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.productHomeContainer,productBillFragment,productBillFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    public void removeAllFragment(){
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

}



