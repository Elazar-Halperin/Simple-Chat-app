package com.example.test.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;

import com.example.test.LogMainActivity;
import com.example.test.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SettingsFragment extends Fragment {
    Button btn_signOut;

    FirebaseAuth mAuth;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_signOut = view.findViewById(R.id.btn_signOut);

        mAuth = FirebaseAuth.getInstance();

        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                requireActivity().finish();
                Intent i = new Intent(getActivity(), LogMainActivity.class);
                startActivityForResult(i, 200);
            }
        });
    }
}