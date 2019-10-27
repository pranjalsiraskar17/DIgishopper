package com.project.sagar.digishopper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.List;
import java.util.Locale;

import adapter.ProductAdapter;
import fragment.LoginPageFragment;
import fragment.MapFragment;
import fragment.MyAccountFragment;
import fragment.MyOrderFragment;
import fragment.NotificationFragment;
import fragment.ProductBillFragment;
import fragment.SearchProductFragment;
import fragment.ShoppingCartFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDrawableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private RecyclerView product_recyclerView;
    private DatabaseReference productRef;
    private ProductAdapter adapter;
    private ViewFlipper flipper;
    private float startX;
    private float startY;
    private int CLICK_ACTION_THRESHOLD = 200;
    private ArrayList<AllProduct> productsList=new ArrayList<AllProduct>();
    private TextView username;
    FirebaseUser user;
    EditText productSearchBar;
    TextView txtLocation;
    LocationManager locationManager;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        txtLocation=findViewById(R.id.txtLocation);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("We are fetching your records...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final ArrayList<String> imageList=new ArrayList<>();
        user=FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference dbruser=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        dbruser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username=findViewById(R.id.txtusername);
                String fname=dataSnapshot.child("user_fname").getValue().toString();
                String lname=dataSnapshot.child("user_lname").getValue().toString();
                username.setText(fname+" "+lname);
                String add="";
                if(dataSnapshot.child("user_address").child("add1").getValue()!=null)
                add+=dataSnapshot.child("user_address").child("add1").getValue().toString();

                if(dataSnapshot.child("user_address").child("add2").getValue()!=null)
                    add+=" "+dataSnapshot.child("user_address").child("add2").getValue().toString();

                if(dataSnapshot.child("user_address").child("street_locality").getValue()!=null)
                    add+=" "+dataSnapshot.child("user_address").child("street_locality").getValue().toString();

                if(dataSnapshot.child("user_address").child("landmark").getValue()!=null)
                    add+=" "+dataSnapshot.child("user_address").child("landmark").getValue().toString();

                if(dataSnapshot.child("user_address").child("district").getValue()!=null)
                    add+=" "+dataSnapshot.child("user_address").child("district").getValue().toString();

                if(dataSnapshot.child("user_address").child("pin").getValue()!=null)
                    add+=" "+dataSnapshot.child("user_address").child("pin").getValue().toString();

                if(add.length()!=0)
                {
                    txtLocation.setText(add);
                }else
                {
                    txtLocation.setText("Add Delivery Address");
                }
                progressDialog.dismiss();



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


        txtLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= txtLocation.getRight() - txtLocation.getTotalPaddingRight()) {
                        showMapFragment();

                        return true;
                    }
                }
                return true;
            }
        });



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_menu) {
            showShoppingCartFragment();
            return true;
        }
        else if (id == R.id.notification_menu) {
            showNotificationFagment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showShoppingCartFragment() {
        if(getSupportFragmentManager().findFragmentByTag(ShoppingCartFragment.TAG)==null )
        {
            ShoppingCartFragment shoppingCartFragment=new ShoppingCartFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.productHomeContainer,shoppingCartFragment,shoppingCartFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }else
        {
            ShoppingCartFragment shoppingCartFragment=(ShoppingCartFragment)getSupportFragmentManager().findFragmentByTag(ShoppingCartFragment.TAG);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.productHomeContainer,shoppingCartFragment,shoppingCartFragment.TAG)
                    .addToBackStack(null)
                    .commit();


        }
    }
    private void showNotificationFagment() {
        if(getSupportFragmentManager().findFragmentByTag(NotificationFragment.TAG)==null )
        {
            NotificationFragment notificationFragment=new NotificationFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.productHomeContainer,notificationFragment,notificationFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }else
        {
            NotificationFragment notificationFragment=(NotificationFragment) getSupportFragmentManager().findFragmentByTag(NotificationFragment.TAG);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.productHomeContainer,notificationFragment,notificationFragment.TAG)
                    .addToBackStack(null)
                    .commit();


        }
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


        } else if (id == R.id.nav_mywishlist) {

        } else if (id == R.id.nav_myaccount) {
            MyAccountFragment myAccountFragment=new MyAccountFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.productHomeContainer,myAccountFragment,myAccountFragment.TAG)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_setting) {

        }
        else if (id == R.id.nav_home) {
            removeAllFragment();

        }
        else if (id == R.id.sign_out_menu) {
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






    public void showMapFragment(){
        MapFragment mapFragment=new MapFragment();
        Bundle bundle=new Bundle();
        bundle.putString("parentFrag","prdhome1");
        mapFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.productHomeContainer,mapFragment,mapFragment.TAG)
                .addToBackStack(null)
                .commit();

    }

    public void showMapFragment(String prdid,int qty)
    {
        MapFragment mapFragment=new MapFragment();
        Bundle bundle=new Bundle();
        bundle.putString("prdidToAddress",prdid);
        bundle.putString("parentFrag","prdhome");
        bundle.putInt("qty",qty);
        mapFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.productHomeContainer,mapFragment,mapFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
    public void showMapFragment(HashMap<String,ArrayList<String>> map)
    {
        MapFragment mapFragment=new MapFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("cartmap",map);
        bundle.putString("parentFrag","cart");
        mapFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.productHomeContainer,mapFragment,mapFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
    public void showProductBillFragment(HashMap<String,ArrayList<String>> map,String address,String name,String mobile)
    {
        ProductBillFragment productBillFragment=new ProductBillFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("prdmap",map);
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

    @Override
    public void onLocationChanged(Location location) {

        //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Toast.makeText(this, addresses.get(0).getAddressLine(0)+", "+ addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2) , Toast.LENGTH_SHORT).show();
            txtLocation.setText(" Deliver To - "+addresses.get(0).getAddressLine(0));
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
}



