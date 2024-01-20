package com.example.myanonchat.repository;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myanonchat.GroupsActivity;
import com.example.myanonchat.MainActivity;
import com.example.myanonchat.SignInActivity;
import com.example.myanonchat.model.ChatGroup;
import com.example.myanonchat.model.ChatMessage;
import com.example.myanonchat.model.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository {


    private UserAccount userAccount;
    private MutableLiveData<List<ChatGroup>> chatgroups;
    private MutableLiveData<List<ChatMessage>> chatmessages;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database ;
    private DatabaseReference reference_acc;
    private DatabaseReference reference_grp;
    private DatabaseReference reference_msg;


    public Repository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.reference_acc = database.getReference("Users");
        this.reference_grp = database.getReference("groups");
        this.reference_msg = database.getReference("chat_messages");
        this.userAccount = new UserAccount();
        this.chatgroups = new MutableLiveData<List<ChatGroup>>();
        this.chatmessages = new MutableLiveData<List<ChatMessage>>();
    }


    // get Uid
    public String getCurrentUID(){
        return mAuth.getUid();
    }

    //sign out

    public void signOut(Context context){
        mAuth.signOut();


        //delete sign in tokens
        File file = new File(context.getFilesDir(), "sign in token.txt");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        try {
            osw.write("");
            osw.flush();
            osw.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //delete user tokens

        file = new File(context.getFilesDir(), "User token.txt");

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        osw = new OutputStreamWriter(fos);
        try {
            osw.write("");
            osw.flush();
            osw.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Intent i = new Intent(context, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        ((Activity)context).finish();
    }

    //get the group list
    public MutableLiveData<List<ChatGroup>> getChatGroups(Context context) {
        List<ChatGroup> groupList = new ArrayList<>();

        reference_grp.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    ChatGroup newchatgroup = new ChatGroup(snapshot1.child("name").getValue().toString()
                            ,snapshot1.child("password").getValue().toString());
                    groupList.add(newchatgroup);
                }
                chatgroups.postValue(groupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context,
                        "something went wrong fetching the data!",
                        Toast.LENGTH_LONG).show();


            }
        });

        return chatgroups;

    }


    //get group messages
    public MutableLiveData<List<ChatMessage>> getGroupMessages(String groupname,Context context) {
        List<ChatMessage> msgList = new ArrayList<>();

        reference_msg.child(groupname).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    ChatMessage newchatmsg = snapshot1.getValue(ChatMessage.class);
                    msgList.add(newchatmsg);
                }
                chatmessages.postValue(msgList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context,
                        "something went wrong fetching the data!",
                        Toast.LENGTH_LONG).show();


            }
        });

        return chatmessages;

    }


    //send a message

    public void sendMessage(String message ,String groupname, Context context){

        DatabaseReference ref = reference_msg.child(groupname).getRef();
        if(!message.trim().equals("")){
            File file = new File(context.getFilesDir(), "User token.txt");

            FileInputStream fis = null;
            try {
                file.createNewFile();
                fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String email;
                String nickname;
                boolean priv;
                String pass;


                email = br.readLine();
                nickname = br.readLine();
                priv = br.readLine()=="true";
                pass = br.readLine();

                br.close();
                isr.close();
                fis.close();
                if(nickname.isEmpty()){
                    Toast.makeText(context,
                            "error: user tokens!",
                            Toast.LENGTH_LONG).show();
                }else {
                    ChatMessage chatMessage = new ChatMessage(nickname,
                            getCurrentUID(),message,groupname,System.currentTimeMillis()
                    );
                    String randomKey = ref.push().getKey();

                    ref.child(randomKey).setValue(chatMessage);
                }


            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
    //create a group
    public void createGroup(ChatGroup GroupName){
        reference_grp.child(GroupName.getName()).setValue(GroupName);
        ChatMessage chatMessage = new ChatMessage("admin",
                "non of your business"
                ,"test message here saying hello",
                GroupName.getName(),System.currentTimeMillis());

        //no longer needed
        /*DatabaseReference ref = reference_msg.child(GroupName.getName()).getRef();
        String randomKey = ref.push().getKey();
        ref.child(randomKey).setValue(chatMessage);

         */
    }

    //sign in with email and password
    public void sign_in_with_email_pass(boolean rememberme ,Context context, @NonNull String email, @NonNull String pass){
        if(!email.isEmpty()&& !pass.isEmpty()){
            mAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(context, GroupsActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);

                        ///////////////////////
                        //useraccount instance
                        reference_acc.child(email.toString().replace(".com","")).get()
                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                DataSnapshot snapshot1 = task.getResult();
                                if (snapshot1.child("password").getValue().toString().equals(pass)
                                && snapshot1.child("email").getValue().toString().equals(email) ){

                                    //save user token
                                    String user = snapshot1.child("email").getValue().toString()
                                            + "\n" + snapshot1.child("nickname").getValue().toString()
                                    + "\n" +snapshot1.child("privileged").getValue()
                                            + "\n"+snapshot1.child("password").getValue().toString();
                                    File file = new File(context.getFilesDir(), "User token.txt");

                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    FileOutputStream fos = null;
                                    try {
                                        fos = new FileOutputStream(file);
                                    } catch (FileNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                                    try {
                                        osw.write("");
                                        osw.write(user);
                                        osw.flush();
                                        osw.close();
                                        fos.close();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }


                                }
                            }
                        });
                        if(rememberme){
                            String text = email+"\n"+pass;
                            File file = new File(context.getFilesDir(), "sign in token.txt");

                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            OutputStreamWriter osw = new OutputStreamWriter(fos);
                            try {
                                osw.write("");
                                osw.write(text);
                                osw.flush();
                                osw.close();
                                fos.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        context.startActivity(i);
                        ((Activity)context).finish();
                    }
                    else{
                        Toast.makeText(context,
                                "error while signing in!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(context,
                    "Error: empty fields!",
                    Toast.LENGTH_LONG).show();
        }
    }


    //create a new account
    public void createAccount(Context context,@NonNull String email,@NonNull String pass , @NonNull String nickname){

        if(!email.isEmpty() && !pass.isEmpty() && !nickname.isEmpty() && pass.length()>=6){
            UserAccount tempuserAccount = new UserAccount(email,pass,nickname,true);
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    reference_acc.child(tempuserAccount.getEmail().toString().replace(".com","")).setValue(tempuserAccount)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent i = new Intent(context, SignInActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context,
                                            "error while creating account",
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                }
            });


        }
        else if(pass.length()<6){
            Toast.makeText(context,
                    "Error: password must be longer than 6 characters!",
                    Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context,
                    "Error: empty fields!",
                    Toast.LENGTH_LONG).show();
        }


    }


}