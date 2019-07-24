package com.project.sagar.digishopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(firebaseUser==null)
                    {
                        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent=new Intent(SplashActivity.this,HomeDrawableActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
