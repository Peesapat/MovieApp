package com.example.my_imdb;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText emailtext,passwordtext;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        //notification bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(SignIn.this,R.color.black));
        }

        Button signinbtn2 = findViewById(R.id.signinbtn2);
        TextView signuptv = findViewById(R.id.signuptv);
        TextView fptxt = findViewById(R.id.fptxt);

        signinbtn2.setOnClickListener(this);
        signuptv.setOnClickListener(this);
        fptxt.setOnClickListener(this);

        emailtext = (TextInputEditText)findViewById(R.id.emailtxt);
        passwordtext = (TextInputEditText)findViewById(R.id.pwtxt);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){

            case R.id.signinbtn2: userLogin();break;
//            case R.id.signuptv: i = new Intent(this, com.example.canprojectapp.SignUp.class);startActivity(i);break;
//            case R.id.fptxt: i = new Intent(this,ForgotPassword.class);startActivity(i);break;

        }
    }

    private void userLogin() {
        String email = emailtext.getText().toString().trim();
        String password = passwordtext.getText().toString().trim();

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

        progressBar.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        //redirect userF to home
                        Intent i = new Intent(SignIn.this, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
//                        startActivity(new Intent(SignIn.this,HomeActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(SignIn.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SignIn.this,"Failed to Sign In! Please check your credentials",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}
