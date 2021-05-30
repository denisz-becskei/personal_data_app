package com.deniszbecskei.personaldata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getName();
    EditText newPassEditText;
    EditText confirmPassEditText;
    EditText newUsernameEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Log.d(LOG_TAG, "Profile loaded"); debug

        newPassEditText = findViewById(R.id.passwordChangeEditText);
        confirmPassEditText = findViewById(R.id.passwordConfirmEditText);
        newUsernameEditText = findViewById(R.id.usernameChangeEditText);
        mAuth = FirebaseAuth.getInstance();
    }

    public void changePass(View view) {
        // Log.d(LOG_TAG, "Changepass entered"); debug
        if (newPassEditText.getText().toString().equals(confirmPassEditText.getText().toString())) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.updatePassword(String.valueOf(newPassEditText.getText())).
                        addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(LOG_TAG, "User password updated.");
                                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e(LOG_TAG, "User password update failed.");
                                Toast.makeText(this, "Password update failed. Contact developers!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
            finish();
        } else {
            Toast.makeText(this, "The two given password aren't equal", Toast.LENGTH_LONG).show();
            // Log.e(LOG_TAG, newPassEditText.getText() + " " + confirmPassEditText.getText());  debug
        }
    }

    public void changeUname(View view) {
        if (newUsernameEditText.getText().toString().length() > 0) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                UserProfileChangeRequest profileChanges = new UserProfileChangeRequest.Builder().setDisplayName(newUsernameEditText.getText().toString()).build();
                user.updateProfile(profileChanges);
                Toast.makeText(this, "Username was updated successfully, hi " + newUsernameEditText.getText().toString() + ", nice to meet you!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Your username cannot be empty", Toast.LENGTH_LONG).show();
            }
        }
    }
}