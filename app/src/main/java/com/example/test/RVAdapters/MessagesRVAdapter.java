package com.example.test.RVAdapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Models.Message;
import com.example.test.R;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesRVAdapter extends RecyclerView.Adapter<MessagesRVAdapter.MessageHolder>  {
    List<Message> messageList;
    Context context;
    FirebaseUser firebaseUser;

    public MessagesRVAdapter(List<Message> list, Context context, FirebaseUser firebaseUser) {
        messageList = list;
        this.context = context;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_box, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        CardView cv_messageBox = holder.getCv_messageBox();
        LinearLayout.LayoutParams  params=(LinearLayout.LayoutParams)cv_messageBox.getLayoutParams();
        Log.d("database", "message:" + messageList.get(position).getSentByUserUid() + ",User Uid:" + firebaseUser.getUid());
        if(messageList.get(position).getSentByUserUid().equals(firebaseUser.getUid())) {
            params.gravity= Gravity.END;
            params.setMarginEnd(8);
            params.setMarginStart(64);
            cv_messageBox.setCardBackgroundColor(context.getResources().getColor(R.color.sent_message_color));
        } else {
            params.gravity= Gravity.START;
            params.setMarginEnd(64);
            params.setMarginStart(8);
            cv_messageBox.setCardBackgroundColor(context.getResources().getColor(R.color.blue_background));
        }
        cv_messageBox.setLayoutParams(params);



        holder.getTv_message().setText(messageList.get(position).getText());
        holder.getTv_date().setText("17:34");
        holder.getTv_username().setText(messageList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder{
        final CardView cv_messageBox;
        final TextView tv_username;
        final TextView tv_message;
        final TextView tv_date;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            cv_messageBox = itemView.findViewById(R.id.cv_messageBox);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_username = itemView.findViewById(R.id.tv_messageUsername);
        }

        public TextView getTv_date() {
            return tv_date;
        }

        public CardView getCv_messageBox() {
            return cv_messageBox;
        }

        public TextView getTv_message() {
            return tv_message;
        }

        public TextView getTv_username() {
            return tv_username;
        }
    }

    private String convertStampIntoDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String currentDate = dateFormat.format(date);
        return currentDate;
    }
}
