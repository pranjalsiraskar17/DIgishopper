package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.sagar.digishopper.R;

public class LoginPageFragment extends Fragment {
    private Button btn_login,btn_signup;
    private TextView txt_forgetpass;
    private EditText editText_username,editText_password;
    private CheckBox cbx_Remember;
    public static final String TAG=LoginPageFragment.class.getSimpleName();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.login_layout,container,false);
        btn_login=(Button)v.findViewById(R.id.login_btn);
        btn_signup=(Button)v.findViewById(R.id.signup_btn);
        txt_forgetpass=(TextView)v.findViewById(R.id.forgetpass_txt);
        editText_password=(EditText)v.findViewById(R.id.pass_edittext);
        editText_username=(EditText)v.findViewById(R.id.username_edittext);
        cbx_Remember=(CheckBox)v.findViewById(R.id.remember_cbx);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpClick();
            }
        });
        return v;
    }

    private void onSignUpClick() {
        SignUpPageFragment loginPageFragment=new SignUpPageFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.loginContainer,loginPageFragment,LoginPageFragment.TAG)
                .commit();
    }
}
