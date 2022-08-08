package com.example.test.RVAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Models.User;
import com.example.test.R;

import java.util.List;

public class UsersRVAdapter extends RecyclerView.Adapter<UsersRVAdapter.UsersViewHolder> {
    List<User> userList;
    Context context;
    static final String CHAT_UID = "CHAT_UID";

    public UsersRVAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_box, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.getTv_username().setText(userList.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_username;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_displayUserName);
        }

        public TextView getTv_username() {
            return tv_username;
        }

    }
}
