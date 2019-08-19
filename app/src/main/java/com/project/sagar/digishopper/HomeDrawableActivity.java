package com.project.sagar.digishopper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.view.GestureDetector;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    APIService apiService;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ArrayList<String> imageList=new ArrayList<>();
        user=FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference flipperRef=FirebaseDatabase.getInstance().getReference().child("FlipperData").child("Flipper1");
//        Query quer=flipperRef.orderByChild("isShow").equalTo("true");
//        flipperRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                    if(dataSnapshot.child("image_url").getValue()!=null)
//                    {
//                        String imageUrl=dataSnapshot.child("image_url").getValue().toString();
//                        imageList.add(imageUrl);
//                    }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        imageList.add("https://image.shutterstock.com/image-vector/isometric-users-buying-online-tablets-260nw-1154447980.jpg");
        imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREBV9TIH6ri4eqvG5jloZirQBOZop7KQ4b-tmMOyB43DTzE58m");
        imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ547F5phL6-3-Rd4JHXOzz5VKt6csQAiTPQKr39cj6rdglMQ91");
        imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR31LIrJBmaN8D3lftG_pplvE-ItXBy46zhkQ2Q-y1jfVn0PcOR");
        flipper=findViewById(R.id.mainFlipper);
//        flipper.setInAnimation(this,android.R.anim.slide_in_left);
//        flipper.setOutAnimation(this,android.R.anim.slide_out_right);
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
        product_recyclerView=findViewById(R.id.recyclerview);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            sendNotification(user.getUid(),"","");

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

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
}
