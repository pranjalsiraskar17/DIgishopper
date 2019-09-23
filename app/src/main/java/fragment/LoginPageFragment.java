package fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.R;

public class LoginPageFragment extends Fragment {
    public static final String TAG=LoginPageFragment.class.getSimpleName();
    private Button btn_login,btn_signup;
    private TextView txt_forgetpass,txtVerifyEmail;
    private EditText editText_mobile,editText_password;
    private CountryCodePicker mCodePicker;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.login_layout,container,false);
        btn_login=(Button)v.findViewById(R.id.login_btn);
        btn_signup=(Button)v.findViewById(R.id.signup_btn);
        editText_password=(EditText)v.findViewById(R.id.pass_edittext);
        editText_mobile=(EditText)v.findViewById(R.id.mobile_edittext);
        txtVerifyEmail=v.findViewById(R.id.verifyEmail);
        mCodePicker=v.findViewById(R.id.codePicker);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Log In");
        progressDialog.setMessage("Please wait, we are fetching your credentials");
        txtVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerifyEmailFragment();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String mobile=editText_mobile.getText().toString();
                final String password=editText_password.getText().toString();
                boolean isValidate=true;
                if(TextUtils.isEmpty(mobile))
                {
                    isValidate=false;
                }
                if(TextUtils.isEmpty(password))
                {
                    isValidate=false;
                }

                if(isValidate)
                {

                    DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("Users");
                    Query query=dbr.orderByChild("user_phone_number").equalTo(mCodePicker.getSelectedCountryCodeWithPlus().concat(mobile));
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String pass="";
                            String email="";
                            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                            {

                                if(!TextUtils.isEmpty(String.valueOf(dataSnapshot1.child("user_pass").getValue())))
                                {
                                    pass=String.valueOf(dataSnapshot1.child("user_pass").getValue());
                                }
                                if(!TextUtils.isEmpty(String.valueOf(dataSnapshot1.child("user_email").getValue())))
                                {
                                    email=String.valueOf(dataSnapshot1.child("user_email").getValue());
                                }

                            }
                                    if(pass.equals(password))
                                    {

                                        firebaseAuth.signInWithEmailAndPassword(email,pass).
                                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                                        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                                                        {
                                                            progressDialog.dismiss();

                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .child("user_device_token").setValue(FirebaseInstanceId.getInstance().getToken());
                                                            Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                            Intent loginIntent=new Intent(getActivity(), HomeDrawableActivity.class);
                                                            startActivity(loginIntent);
                                                            getActivity().finish();


                                                        }else
                                                        {
                                                            progressDialog.dismiss();
                                                            FirebaseAuth.getInstance().signOut();
                                                            Toast.makeText(getActivity(), "Email is Not Verified", Toast.LENGTH_SHORT).show();
                                                        }


                                                    }
                                                });

                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Wrong Password", Toast.LENGTH_SHORT).show();

                                    }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
//                    firebaseAuth.signInWithEmailAndPassword(username,password)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful())
//                                {
//                                    FirebaseUser user=firebaseAuth.getCurrentUser();
//                                    if(user.isEmailVerified())
//                                    {
//                                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
//                                        Intent loginIntent=new Intent(getActivity(), HomeDrawableActivity.class);
//                                        startActivity(loginIntent);
//                                        getActivity().finish();
//                                    }
//                                    else
//                                    {
//                                        Toast.makeText(getActivity(), "Email Not verified please verify your email", Toast.LENGTH_SHORT).show();
//
//                                    }
//
//                                }else {
//                                    Toast.makeText(getActivity(), "Invalid Username & password", Toast.LENGTH_SHORT).show();
//
//                                }
//                                }
//                            });

                }
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpFragment();
            }
        });
        return v;
    }

    private void showSignUpFragment() {
        SignUpPageFragment signUpPageFragment=new SignUpPageFragment();
        if(signUpPageFragment!=null)
        {
            getFragmentManager().beginTransaction()
                    .replace(R.id.loginContainer,signUpPageFragment,SignUpPageFragment.TAG)
                    .commit();
        }

    }
    private void showVerifyEmailFragment() {
        VerifyEmailFragment verifyEmailFragment=new VerifyEmailFragment();
        if(verifyEmailFragment!=null)
        {
            getFragmentManager().beginTransaction()
                    .replace(R.id.loginContainer,verifyEmailFragment,verifyEmailFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }

    }
}
