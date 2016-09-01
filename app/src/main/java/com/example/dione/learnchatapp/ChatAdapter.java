package com.example.dione.learnchatapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dione on 01/09/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<Chat> chatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView message;


        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.textName);
            message = (TextView) view.findViewById(R.id.textMessage);
        }
    }


    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.username.setText(chat.getUser());
        holder.message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
