package fragment;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.AllProduct;
import com.project.sagar.digishopper.R;
import com.project.sagar.digishopper.UserInfo;


public class ForgatePassFragment extends Fragment {
        public static final String TAG=ForgatePassFragment.class.getSimpleName();
        private EditText Email;
        private Button resetPassword;
        private FirebaseAuth firebaseAuth;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v=inflater.inflate(R.layout.forget_password,container,false);

            Email = (EditText)v.findViewById(R.id.Email);
            resetPassword = (Button)v.findViewById(R.id.btnPasswordReset);
            firebaseAuth = FirebaseAuth.getInstance();

            resetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String useremail = Email.getText().toString().trim();

                    if(useremail.equals("")){
                        Toast.makeText(getActivity(), "Please enter your registered email ID", Toast.LENGTH_SHORT).show();
                    }else{
                        firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Password reset email sent!", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(getActivity(), "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });return v;

        }

    }

