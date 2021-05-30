package com.deniszbecskei.personaldata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ActionsActivity extends AppCompatActivity {

    private static final String LOG_TAG = ActionsActivity.class.getName();
    private FirebaseAuth mAuth;
    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);

        mAuth = FirebaseAuth.getInstance();
        usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuth.getCurrentUser() != null) {
            Log.e(LOG_TAG, "User left without logging out! So we log out!");
            mAuth.signOut();
        }
    }

    public void search(View view) {
        Intent intent = new Intent(this, SearchPeopleActivity.class);
        startActivity(intent);
    }

    public void profile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void list(View view) {
        Intent intent = new Intent(this, ListPeopleActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();
        finish();
    }
}