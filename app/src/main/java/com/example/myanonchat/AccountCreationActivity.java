package com.example.myanonchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.myanonchat.databinding.ActivityAccountCreationBinding;
import com.example.myanonchat.viewmodel.MyViewModel;

public class AccountCreationActivity extends AppCompatActivity {

    ActivityAccountCreationBinding binding ;
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding = DataBindingUtil.setContentView(this
                ,R.layout.activity_account_creation);
        binding.setMviewmodel(myViewModel);

        binding.createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.createAccount(AccountCreationActivity.this,binding.createEmailEdt.getText().toString(),
                        binding.createPassEdt.getText().toString(),
                        binding.createNicknameEdt.getText().toString());
            }
        });

    }
}