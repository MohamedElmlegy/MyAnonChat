package com.example.myanonchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.myanonchat.adapter.MessageAdapter;
import com.example.myanonchat.databinding.ActivityChatBinding;
import com.example.myanonchat.model.ChatMessage;
import com.example.myanonchat.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private MyViewModel myViewModel;
    private RecyclerView recyclerView;
    private MessageAdapter myAdapter;

    private List<ChatMessage> messagesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // RecyclerView with DataBinding
        recyclerView = binding.recycleMessages;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        messagesList = new ArrayList<>();
        binding.setVmodel(myViewModel);

        // Getting the Group Name from the Clicked Item in the GroupsActivity
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        binding.groupNameTxt.setText(groupName);


        myViewModel.getMessage(groupName,this).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                messagesList.clear();
                messagesList.addAll(chatMessages);

                myAdapter = new MessageAdapter(messagesList,getApplicationContext());

                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();

                // Scroll to the latest message added:
                int latestPosition = myAdapter.getItemCount() -1;
                if (latestPosition > 0) {
                    recyclerView.smoothScrollToPosition(latestPosition);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.sendBtn.setTooltipText("send text");
        }
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = binding.messageEdt.getText().toString();
                myViewModel.sendMessage(msg, groupName, ChatActivity.this);

                binding.messageEdt.getText().clear();
            }
        });
    }
}