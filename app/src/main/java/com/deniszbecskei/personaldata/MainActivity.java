package com.deniszbecskei.personaldata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = Objects.requireNonNull(MainActivity.class.getPackage()).toString();

    EditText emailTextArea;
    EditText passwordTextArea;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailTextArea = findViewById(R.id.loginEmailTextInput);
        passwordTextArea = findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String emailAddr = preferences.getString("emailAddr", "");
        emailTextArea.setText(emailAddr, TextView.BufferType.EDITABLE);

        Log.i(LOG_TAG, "On Create called");
    }

    public void login(View view) {
        String email = emailTextArea.getText().toString();
        String password = passwordTextArea.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            return;
        }

        passwordTextArea.setText("", TextView.BufferType.EDITABLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(LOG_TAG, "User was logged in successfully!");
                startAppUse();
            } else {
                Log.e(LOG_TAG, "User wasn't logged in successfully... goddammit!");
                Toast.makeText(MainActivity.this, "User login fail: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void startAppUse() {
        Intent intent = new Intent(this, ActionsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("emailAddr", emailTextArea.getText().toString());
        editor.apply();

        Log.d(LOG_TAG, "On Pause called");
    }
}