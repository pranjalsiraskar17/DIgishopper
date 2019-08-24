package fragment;

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

public class VerifyEmailFragment extends Fragment {
    public static final String TAG=SignUpPageFragment.class.getSimpleName();
    private EditText editTextEmail;
    private Button verifyBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.verify_email_layout,container,false);
        editTextEmail=v.findViewById(R.id.txtEmail);
        verifyBtn=v.findViewById(R.id.btnVerify);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users");
                Query query=FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("user_email").
                        equalTo(editTextEmail.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pass="";
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            UserInfo user=dataSnapshot1.getValue(UserInfo.class);
                            pass=user.getUser_pass();
                            break;

                        }
                        if(!TextUtils.isEmpty(pass))
                        {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextEmail.getText().toString(),pass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                            if(user.isEmailVerified())
                                            {
                                                Toast.makeText(getActivity(), "Email Alrady Verified", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                user.sendEmailVerification().
                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(getActivity(), "Email Sent SuccessFully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Email not send", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }

                                            FirebaseAuth.getInstance().signOut();
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Email Not Found", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        return v;
    }
}
