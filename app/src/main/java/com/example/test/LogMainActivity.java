package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogMainActivity extends AppCompatActivity {
    BottomNavigationView bnv_signInUp;
    NavController navController;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_main_activity);

        mAuth = FirebaseAuth.getInstance();
        bnv_signInUp = findViewById(R.id.bnv_signInUp);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fcv_signInUpHolder);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bnv_signInUp, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            Intent i = new Intent(LogMainActivity.this, HomePageActivity.class);
            startActivityForResult(i, 200);
            finish();
        }
    }
}