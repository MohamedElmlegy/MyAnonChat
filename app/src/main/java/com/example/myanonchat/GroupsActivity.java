package com.example.myanonchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myanonchat.adapter.GroupsAdapter;
import com.example.myanonchat.databinding.ActivityGroupsBinding;
import com.example.myanonchat.model.ChatGroup;
import com.example.myanonchat.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

public class GroupsActivity extends AppCompatActivity {

    private ArrayList<ChatGroup> chatGroups ;

    private ArrayList<ChatGroup> filteredchatGroups ;
    private RecyclerView recyclerView;
    private MyViewModel myViewModel;
    private GroupsAdapter groupsAdapter;
    private ActivityGroupsBinding activityGroupsBinding;


    private Dialog newChatgroupDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        activityGroupsBinding = DataBindingUtil
                .setContentView(this,R.layout.activity_groups);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        recyclerView = activityGroupsBinding.recycleGroups;
        activityGroupsBinding.setMyviewmodel(myViewModel);
        filteredchatGroups = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        myViewModel.getGroups().observe(this, new Observer<List<ChatGroup>>() {
            @Override
            public void onChanged(List<ChatGroup> chatGroupslist) {
                chatGroups = new ArrayList<>();
                chatGroups.addAll(chatGroupslist);

                filter(activityGroupsBinding.searchText.getText().toString());

                groupsAdapter = new GroupsAdapter(filteredchatGroups);
                recyclerView.setAdapter(groupsAdapter);
                groupsAdapter.notifyDataSetChanged();

            }
        });



        activityGroupsBinding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.signOut(GroupsActivity.this);
                finish();
            }
        });



        activityGroupsBinding.fap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activityGroupsBinding.logOutBtn.setTooltipText("logout");
            activityGroupsBinding.fap.setTooltipText("Create new group");
            activityGroupsBinding.fapRand.setTooltipText("Enter a random chat group from your search results or from all groups if there is no search text");
        }

        activityGroupsBinding.fapRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filteredchatGroups.size()>0){
                    int i = getRandPosition(filteredchatGroups.size());
                    recyclerView.getChildAt(i).performClick();
                }else {
                    Toast.makeText(view.getContext(), "No groups to choose from "
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });


        activityGroupsBinding.searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
                groupsAdapter.notifyDataSetChanged();
            }
        });
    }


    public int getRandPosition(int size){
        return new Random().nextInt(size);
    }

    public void filter(String s){
        filteredchatGroups.clear();
        for(ChatGroup c : chatGroups){
            if (c.getName().toString().contains(s)){
                filteredchatGroups.add(c);
            }
        }
    }




    public void showDialog(){
        newChatgroupDialog = new Dialog(this);
        newChatgroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View v = LayoutInflater.from(this)
                .inflate(R.layout.dialog_layout,null);
        newChatgroupDialog.setContentView(v);

        Button submit = v.findViewById(R.id.submit_btn);
        EditText edt = v.findViewById(R.id.chat_group_edt);
        EditText pass_edt = v.findViewById(R.id.chat_group_pass_edt);
        CheckBox cb = v.findViewById(R.id.checkbox_islocked);
        pass_edt.setVisibility(View.GONE);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pass_edt.setVisibility(View.VISIBLE);
                }else {
                    pass_edt.setVisibility(View.GONE);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = edt.getText().toString();
                String sp;
                if(s.isEmpty()){
                    Toast.makeText(view.getContext(), "Enter group name "
                            , Toast.LENGTH_SHORT).show();
                }else {
                    if (cb.isChecked()) {
                        sp = pass_edt.getText().toString();
                        if (sp.length() >= 6) {
                            myViewModel.createGroup(new ChatGroup(s, sp));
                            newChatgroupDialog.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "Password must be longer than 6"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        myViewModel.createGroup(new ChatGroup(s, ""));
                        newChatgroupDialog.dismiss();

                    }
                }
            }
        });
        newChatgroupDialog.show();
    }
}