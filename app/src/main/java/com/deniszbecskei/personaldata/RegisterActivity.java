package com.deniszbecskei.personaldata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;
    EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.registerEmailTextInput);
        passwordEditText = findViewById(R.id.registerPassword);
        passwordConfirmEditText = findViewById(R.id.registerPasswordConfirm);
        usernameEditText = findViewById(R.id.username);
    }

    public void login(View view) {
        finish();
    }

    public void register(View view) {
        if (passwordEditText.getText().toString().equals(passwordConfirmEditText.getText().toString())) {
            if (passwordEditText.getText().toString().length() >= 6) {
                if (emailEditText.getText().toString().contains("@") && emailEditText.getText().toString().contains(".")) {
                    if(usernameEditText.getText().toString().length() != 0) {
                        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(),
                                passwordEditText.getText().toString()).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Successfully registered with the e-mail " +
                                        "address: " + emailEditText.getText().toString(), Toast.LENGTH_LONG).
                                        show();
                                mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString());
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(usernameEditText.getText().toString()).build();
                                    user.updateProfile(profileUpdates);
                                    mAuth.signOut();
                                }
                                finish();
                            } else {
                                Toast.makeText(this, "Error registering, contact developers!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, "Username cannot be blank", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid E-mail address", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "The password needs to be at least 6 characters long", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "The two given password aren't equal.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}