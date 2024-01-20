package com.example.myanonchat.adapter;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.res.Configuration;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myanonchat.R;
import com.example.myanonchat.databinding.ChatRowLayoutBinding;
import com.example.myanonchat.model.ChatMessage;
import com.example.myanonchat.model.Util;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {


    private List<ChatMessage> chatMessageList;
    private Context context;

    public MessageAdapter(List<ChatMessage> chatMessageList, Context context) {
        this.chatMessageList = chatMessageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.chat_row_layout,parent,false);

        ChatRowLayoutBinding binding = DataBindingUtil.bind(view);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Util u_sender;
        Util u_reciver;

        switch (Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                u_sender = new Util(context.getColor(R.color.dark_bubble_sender),
                        context.getColor(R.color.dark_text_sender),
                        Gravity.END, context.getColor(R.color.text_dark));
                u_reciver = new Util(context.getColor(R.color.dark_bubble_reciever),
                        context.getColor(R.color.dark_text_reciever),
                        Gravity.START, context.getColor(R.color.text_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                u_sender = new Util(context.getColor(R.color.dark_bubble_sender),
                        context.getColor(R.color.dark_text_sender),
                        Gravity.END, context.getColor(R.color.text_dark));
                u_reciver = new Util(context.getColor(R.color.dark_bubble_reciever),
                        context.getColor(R.color.dark_text_reciever),
                        Gravity.START, context.getColor(R.color.text_dark));
                break;
            default:
                u_sender = new Util(context.getColor(R.color.dark_bubble_sender),
                        context.getColor(R.color.dark_text_sender),
                        Gravity.END, context.getColor(R.color.text_dark));
                u_reciver = new Util(context.getColor(R.color.dark_bubble_reciever),
                        context.getColor(R.color.dark_text_reciever),
                        Gravity.START, context.getColor(R.color.text_dark));
                break;
        }

        holder.getBinding().setMessage(chatMessageList.get(position));
        if(chatMessageList.get(position).isMine()){
            holder.binding.setUtil(u_sender);
        }else {
            holder.binding.setUtil(u_reciver);
        }
        holder.getBinding().executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ChatRowLayoutBinding binding;

        public MyViewHolder( ChatRowLayoutBinding binding) {
            super(binding.getRoot());
            setBinding(binding);

        }


        public ChatRowLayoutBinding getBinding() {
            return binding;
        }

        public void setBinding(ChatRowLayoutBinding binding) {
            this.binding = binding;
        }
    }
}
