package com.example.myanonchat.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanonchat.ChatActivity;
import com.example.myanonchat.R;
import com.example.myanonchat.databinding.GroupItemBinding;
import com.example.myanonchat.model.ChatGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsHolder> {

    ArrayList<ChatGroup> chatGroupList ;

    public GroupsAdapter(ArrayList<ChatGroup> chatGroupList) {
        this.chatGroupList = chatGroupList;
    }

    @NonNull
    @Override
    public GroupsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        GroupItemBinding binding= DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.group_item,parent,false);

        return new GroupsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsHolder holder, int position) {

        ChatGroup current_group = chatGroupList.get(position);
        holder.itemBinding.setChatGroup(current_group);
    }

    @Override
    public int getItemCount() {
        return chatGroupList.size();
    }

    public class GroupsHolder extends RecyclerView.ViewHolder{

        private GroupItemBinding itemBinding;


        public GroupsHolder(GroupItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

            //clicking group item

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Dialog newChatgroupDialog = new Dialog(view.getContext());
                    newChatgroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    View v = LayoutInflater.from(view.getContext())
                            .inflate(R.layout.pass_dialog_layput,null);
                    newChatgroupDialog.setContentView(v);
                    ChatGroup current = chatGroupList.get(getAdapterPosition());
                    Button submit = v.findViewById(R.id.enter_btn);
                    EditText pass_edt = v.findViewById(R.id.chat_group_pass_input);
                    TextView t = v.findViewById(R.id.textView3);
                    String s = t.getText().toString();
                    s = s + "( " +current.getName() +" )";
                    t.setText(s);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(pass_edt.getText().toString().equals(current.getPassword())){
                                Intent i = new Intent(view.getContext(),ChatActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("GROUP_NAME", current.getName());
                                view.getContext().startActivity(i);
                                newChatgroupDialog.dismiss();
                            }
                            else {
                                Toast.makeText(view.getContext(), "wrong password"
                                        , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    if(current.isIs_locked()){
                        newChatgroupDialog.show();
                    }
                    else{

                        Intent i = new Intent(view.getContext(),ChatActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("GROUP_NAME", current.getName());
                        view.getContext().startActivity(i);

                    }



                }
            });

        }
    }
}
