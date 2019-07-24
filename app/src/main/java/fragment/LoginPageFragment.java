package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.R;

public class LoginPageFragment extends Fragment {
    public static final String TAG=LoginPageFragment.class.getSimpleName();
    private Button btn_login,btn_signup;
    private TextView txt_forgetpass;
    private EditText editText_username,editText_password;
    private CheckBox cbx_Remember;
    private FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.login_layout,container,false);
        btn_login=(Button)v.findViewById(R.id.login_btn);
        btn_signup=(Button)v.findViewById(R.id.signup_btn);
        txt_forgetpass=(TextView)v.findViewById(R.id.forgetpass_txt);
        editText_password=(EditText)v.findViewById(R.id.pass_edittext);
        editText_username=(EditText)v.findViewById(R.id.username_edittext);
        cbx_Remember=(CheckBox)v.findViewById(R.id.remember_cbx);
        firebaseAuth=FirebaseAuth.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=editText_username.getText().toString();
                String password=editText_password.getText().toString();
                boolean isValidate=true;
                if(TextUtils.isEmpty(username))
                {
                    isValidate=false;
                }
                if(TextUtils.isEmpty(password))
                {
                    isValidate=false;
                }

                if(isValidate)
                {
                    firebaseAuth.signInWithEmailAndPassword(username,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent loginIntent=new Intent(getActivity(), HomeDrawableActivity.class);
                                    startActivity(loginIntent);
                                    getActivity().finish();
                                }
                                }
                            });

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
}
