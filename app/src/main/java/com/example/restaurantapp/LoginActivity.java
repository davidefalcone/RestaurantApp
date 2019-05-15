package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout editMail;
    private TextInputLayout editPass;
    private MaterialButton signInButton;
    private MaterialButton signUpButton;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    public static final String EXTRA_IS_NEW = "isnew";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        mAuth = FirebaseAuth.getInstance();

        editMail = findViewById(R.id.editMail);
        editPass = findViewById(R.id.editPass);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        setSignInButton();

        setSignUpButton();

    }

    private void setSignInButton() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(editMail.getEditText().getText().toString(),
                        editPass.getEditText().getText().toString());
            }
        });
    }

    private void setSignUpButton() {
        signUpButton.setOnClickListener(view -> {
            createAccount(editMail.getEditText().getText().toString(),
                    editPass.getEditText().getText().toString());
        });
    }

    private void goToMainActivity(boolean isNew) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(EXTRA_IS_NEW, isNew);
        startActivity(i);
        finish();
    }

    private void signIn(String mail, String pass) {
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            goToMainActivity(false);
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(findViewById(R.id.mycoordinatorLayout), R.string.credentialswrong, Snackbar.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void createAccount(String mail, String pass) {
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            goToMainActivity(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(findViewById(R.id.mycoordinatorLayout), R.string.loginError, Snackbar.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null)
            goToMainActivity(false);
    }
}
