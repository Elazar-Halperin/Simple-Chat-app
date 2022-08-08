package com.example.test.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.test.HomePageActivity;
import com.example.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignInFragment extends Fragment {
    TextInputEditText et_email, et_password;
    Button btn_signIn;
    FirebaseAuth mAuth;
    ProgressBar pb_log;

    public SignInFragment() {
        super(R.layout.fragment_sign_in);
        // Required empty public constructor
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_email = view.findViewById(R.id.et_emailSignIn);
        et_password = view.findViewById(R.id.et_passwordSignIn);
        btn_signIn = view.findViewById(R.id.btn_signIn);
        pb_log = getActivity().findViewById(R.id.pb_log);
        mAuth = FirebaseAuth.getInstance();

        et_email.requestFocus();

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        if(!isValidInput()) {
            return;
        }
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        pb_log.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Signed in successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), HomePageActivity.class);
                    startActivityForResult(i, 200);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Failed to sign in!", Toast.LENGTH_SHORT).show();
                }
                pb_log.setVisibility(View.GONE);
            }
        });
    }

    public boolean isValidInput() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(email.isEmpty()) {
            et_email.setError("Email is required!");
            et_email.requestFocus();
            return false;
        }
        if(password.isEmpty()) {
            et_password.setError("Password is required!");
            et_password.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Please provide valid email");
            et_email.requestFocus();
            return false;
        }
        if(password.length() < 6) {
            et_password.setError("Min password length 6");
            et_password.requestFocus();
            return false;
        }
        return true;
    }
}