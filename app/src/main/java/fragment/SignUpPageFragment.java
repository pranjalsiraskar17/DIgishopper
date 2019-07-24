package fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.signup_layout,container,false);
        btnSignup=v.findViewById(R.id.signup_btn);
        btnLogin=v.findViewById(R.id.login_btn);
        firstName_editText=v.findViewById(R.id.firstname_edittext);
        lastName_editText=v.findViewById(R.id.lastname_edittext);
        email_editText=v.findViewById(R.id.email_edittext);
        mobile_editText=v.findViewById(R.id.mobile_edittext);
        pass_editText=v.findViewById(R.id.pass_edittext);
        confirm_editText=v.findViewById(R.id.repass_edittext);
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

                }else
                {
                    //error handling
                }
            }
        });

        return v;
    }
}
