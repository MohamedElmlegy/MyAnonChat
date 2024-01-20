package com.example.myanonchat.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myanonchat.model.ChatGroup;
import com.example.myanonchat.model.ChatMessage;
import com.example.myanonchat.model.UserAccount;
import com.example.myanonchat.repository.Repository;

import java.util.List;

public class MyViewModel extends AndroidViewModel {


    Repository repository;
    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    //sign in with email and password
    public void signInWithEmailPass(Context c,boolean rememberme ,String email , String pass){
        Context context = c;
        repository.sign_in_with_email_pass(rememberme ,context,email,pass);

    }

    //get uid
    public String getUid(){
        return repository.getCurrentUID();
    }

    //sign out
    public void signOut(Context c){
        Context context = c;
        repository.signOut(context);
    }


    //get group list
    public MutableLiveData<List<ChatGroup>> getGroups(){
        return repository.getChatGroups(getApplication().getApplicationContext());
    }

    //create a new group
    public void createGroup(ChatGroup GroupName){
        repository.createGroup(GroupName);
    }

    //create a new account
    public void createAccount(Context c,@NonNull String email,@NonNull String pass , @NonNull String nickname){
        Context context = c;
        repository.createAccount(context,email,pass,nickname);
    }

    public MutableLiveData<List<ChatMessage>> getMessage(String groupname,Context context){
        return repository.getGroupMessages(groupname,context);
    }

    public void sendMessage(String message , String groupname , Context context){
        repository.sendMessage(message,groupname,context);
    }


}
