package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.LoginActivity;
import com.project.sagar.digishopper.R;

public class SignUpPageFragment extends Fragment {
    public static final String TAG=SignUpPageFragment.class.getSimpleName();
    private Button btnSignup;
    private Button btnLogin;
    private EditText firstName_editText;
    private EditText lastName_editText;
    private EditText email_editText;
    private EditText mobile_editText;
    private EditText pass_editText;
    private EditText confirm_editText;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.signup_layout,container,false);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        btnSignup=v.findViewById(R.id.signup_btn);
        btnLogin=v.findViewById(R.id.login_btn);
        firstName_editText=v.findViewById(R.id.firstname_edittext);
        lastName_editText=v.findViewById(R.id.lastname_edittext);
        email_editText=v.findViewById(R.id.email_edittext);
        mobile_editText=v.findViewById(R.id.mobile_edittext);
        pass_editText=v.findViewById(R.id.pass_edittext);
        confirm_editText=v.findViewById(R.id.repass_edittext);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginFragment();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidate=true;
                if(TextUtils.isEmpty(firstName_editText.getText()))
                {
                    isValidate=false;
                }
                if(TextUtils.isEmpty(lastName_editText.getText()))
                {
                    isValidate=false;
                }
                if(TextUtils.isEmpty(email_editText.getText()))
                {
                    isValidate=false;
                }
                if(TextUtils.isEmpty(mobile_editText.getText()))
                {
                    isValidate=false;
                }
                if(TextUtils.isEmpty(pass_editText.getText()))
                {
                    isValidate=false;
                }
                if(TextUtils.isEmpty(confirm_editText.getText()))
                {
                    isValidate=false;
                }
                if(isValidate)
                {
                    final String fname=firstName_editText.getText().toString();
                    final String lname=lastName_editText.getText().toString();
                    final String email=email_editText.getText().toString();
                    final String mobile=mobile_editText.getText().toString();
                    final String pass=pass_editText.getText().toString();




                    mAuth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid=user.getUid();
                                        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                        dbref.child("fistname").setValue(fname);
                                        dbref.child("lastname").setValue(lname);
                                        dbref.child("email").setValue(email);
                                        dbref.child("mobile").setValue(mobile);
                                        dbref.child("password").setValue(pass)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //data set successfully
                                                        Toast.makeText(getActivity(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent loginIntent=new Intent(getActivity(), HomeDrawableActivity.class);
                                                        startActivity(loginIntent);
                                                        getActivity().finish();
                                                    }
                                                });


                                    }else {
                                        Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else
                {
                    //error handling
                }
            }
        });

        return v;
    }
    private void showLoginFragment() {
        LoginPageFragment loginPageFragment=new LoginPageFragment();
        if(loginPageFragment!=null)
        {
            getFragmentManager().beginTransaction()
                    .replace(R.id.loginContainer,loginPageFragment,LoginPageFragment.TAG)
                    .commit();
        }

    }
}
