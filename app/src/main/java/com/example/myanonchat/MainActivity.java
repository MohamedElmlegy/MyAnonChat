package com.example.myanonchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.example.myanonchat.databinding.ActivityMainBinding;
import com.example.myanonchat.viewmodel.MyViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private MyViewModel myViewModel;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        Animation fadeout = new AlphaAnimation(1.f, 0.f);
        fadeout.setDuration(1500);
        binding.imageView.startAnimation(fadeout);
        binding.imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.VISIBLE);

                checkTokens();
                binding.progressBar.setVisibility(View.GONE);
            }
        }, 1500);






    }

    public void checkTokens(){
        File file = new File(getFilesDir(), "sign in token.txt");

        FileInputStream fis = null;
        try {
            file.createNewFile();
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String email;
            String pass;
            email = br.readLine();
            pass = br.readLine();
            br.close();
            isr.close();
            fis.close();
            if(email == null || pass== null){
                Intent i = new Intent(this, SignInActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                this.finish();
                binding.progressBar.setVisibility(View.GONE);
            }else {
                myViewModel.signInWithEmailPass(MainActivity.this,true, email, pass);
                binding.progressBar.setVisibility(View.GONE);
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}