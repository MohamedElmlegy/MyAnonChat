package com.example.myanonchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myanonchat.databinding.ActivitySignInBinding;
import com.example.myanonchat.viewmodel.MyViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    MyViewModel myViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        binding.setMyviewmodel(myViewModel);

        binding.accountCreationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AccountCreationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.signInWithEmailPass(SignInActivity.this,binding.checkbox.isChecked(),binding.signEmailEdt.getText().toString(),
                        binding.signPassEdt.getText().toString());


            }
        });


    }
}