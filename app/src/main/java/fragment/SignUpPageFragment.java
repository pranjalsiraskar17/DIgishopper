package fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.project.sagar.digishopper.EmailUtil;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.LoginActivity;
import com.project.sagar.digishopper.R;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

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
    private CountryCodePicker mCodePicker;
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
        mCodePicker=v.findViewById(R.id.countryCodePicker);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginFragment();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    new SendMail().execute();

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


                    Bundle args = new Bundle();
                    args.putString("fnameText", fname);
                    args.putString("lnameText", lname);
                    args.putString("countyCode", mCodePicker.getSelectedCountryCodeWithPlus());
                    args.putString("emailText", email);
                    args.putString("mobileText", mobile);
                    args.putString("passText", pass);

                    VerifyMobileFragment verifyMobileFragment=new VerifyMobileFragment();
                    verifyMobileFragment.setArguments(args);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.loginContainer,verifyMobileFragment,VerifyMobileFragment.TAG)
                            .commit();


//                    mAuth.createUserWithEmailAndPassword(email,pass)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if(task.isSuccessful())
//                                    {
//                                        final FirebaseUser user = mAuth.getCurrentUser();
//                                        String uid=user.getUid();
//                                        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//
//                                        dbref.child("fistname").setValue(fname);
//                                        dbref.child("lastname").setValue(lname);
//                                        dbref.child("email").setValue(email);
//                                        dbref.child("mobile").setValue(mobile);
//                                        dbref.child("password").setValue(pass)
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        //data set successfully
//                                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                if(task.isSuccessful())
//                                                                {
//                                                                    Toast.makeText(getActivity(), "Account Created Successfully Verify your email to login", Toast.LENGTH_SHORT).show();
//                                                                    Intent loginIntent=new Intent(getActivity(), LoginActivity.class);
//                                                                    startActivity(loginIntent);
//                                                                    getActivity().finish();
//                                                                }
//                                                            }
//                                                        });
//
//                                                    }
//                                                });
//                                    }else {
//                                        Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
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



    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(String... params) {

            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("digishopperonline@gmail.com", "zgvmmbpiedgxtlta");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, "cyberclub149@gmail.com","TLSEmail Testing Subject", "TLSEmail Testing Body");
            return null;
        }
    }

}
