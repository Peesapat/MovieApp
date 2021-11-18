package com.example.my_imdb;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private CheckBox checkBoxVisibility;
    private Button signupbutton,verifybutton;
    private TextInputEditText nametext,emailtext,passwordtext,confirmpasswordtext;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        //notification bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(SignUp.this,R.color.black));
        }

        nametext = (TextInputEditText)findViewById(R.id.nametxt);
        emailtext = (TextInputEditText)findViewById(R.id.emailtxt);
        passwordtext = (TextInputEditText)findViewById(R.id.pwtxt);
        confirmpasswordtext = (TextInputEditText)findViewById(R.id.cpwtxt);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        TextView signintv1 = findViewById(R.id.signintv1);
        Button signupbtn = findViewById(R.id.signupbtn);
        signupbtn.setOnClickListener(this);
        signintv1.setOnClickListener(this);


        checkBoxVisibility = findViewById(R.id.checkBox1id);
        signupbutton = findViewById(R.id.signupbtn);

        boolean isCheked = checkBoxVisibility.isChecked();

        extracted(isCheked);

        checkBoxVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                extracted(isChecked);
            }
        });

    }

    private void extracted(boolean isCheked) {
        if(isCheked){
            signupbutton.setVisibility(View.VISIBLE);

        }
        else{
            signupbutton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.signupbtn:
                signupbtn();
                break;
            case R.id.signintv1:
                i = new Intent(this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
        }
    }

    private void signupbtn() {

        String name = nametext.getText().toString().trim();
        String email = emailtext.getText().toString().trim();
        String password = passwordtext.getText().toString().trim();
        String confirmpassword = confirmpasswordtext.getText().toString().trim();

        if(name.isEmpty()){
            nametext.setError("Full name is required!");
            nametext.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailtext.setError("E-mail is required!");
            emailtext.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtext.setError("Please provide valid E-mail!");
            emailtext.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordtext.setError("Password is required!");
            passwordtext.requestFocus();
            return;
        }

        if(password.length() < 6){
            passwordtext.setError("Min Password length should be 6 characters!");
            passwordtext.requestFocus();
            return;
        }

        if(confirmpassword.isEmpty()){
            confirmpasswordtext.setError("Password is required!");
            confirmpasswordtext.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    User user = new User(name,email);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> userF = new HashMap<>();
                                userF.put("name", name);
                                userF.put("email", email);
                                userF.put("rank_int", 0);
                                userF.put("profile", "https://i.pinimg.com/236x/53/26/7c/53267cf194e6435f6137a3e99c7cbcfc.jpg");

                                // Add a new document with a generated ID
                                db.collection("users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .set(userF)
                                        .addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
//                                                Log.d("dbug", "DocumentSnapshot added with ID: " + documentReference.getId());
//                                                Log.d("dbug", "DocumentSnapshot added with ID: " +  o);
                                                Toast.makeText(SignUp.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("dbug", "Error adding document", e);
                                            }
                                        });

                            } else {
                                Toast.makeText(SignUp.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignUp.this,"Failed to register! Try !",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });


    }
}