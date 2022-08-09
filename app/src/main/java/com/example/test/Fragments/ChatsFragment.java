package com.example.test.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Models.Message;
import com.example.test.Models.User;
import com.example.test.R;
import com.example.test.RVAdapters.MessagesRVAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    RecyclerView rv_messages;
    EditText et_message;
    FloatingActionButton fab_addMessage;
    RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    List<Message> messageList;
    View v_focus;
    ChildEventListener childEventListener;
    Bundle mBundleRVstate;
    String RV_STATE_KEY = "rv_state";

    public ChatsFragment() {
        // Required empty public constructor
        super(R.layout.fragment_chats);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        rv_messages = view.findViewById(R.id.rv_messages);
        et_message = view.findViewById(R.id.et_message);
        fab_addMessage = view.findViewById(R.id.fab_addMessage);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        fab_addMessage.setVisibility(View.GONE);
        messageList = new ArrayList<>();

        // Get all the messages from the database
        myRef = database.getReference("messages");
        // attach listeners

        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidMessage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fab_addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidMessage())
                    return;
                String key = myRef.push().getKey();
                String text = et_message.getText().toString().trim();
                et_message.setText("");
                database.getReference("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        String name = user.getName();
                        Message message = new Message(name, firebaseUser.getUid(), text, "18:23");
                        myRef.child(key).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("database", text + " Elazar the king");
                                    // adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                    //    @Override
                                    //    public void onItemRangeInserted(int positionStart, int itemCount) {
                                    //        layoutManager.smoothScrollToPosition(rv_messages, null, adapter.getItemCount());
                                    //    }
                                    //});
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }


        });


        // Add adapter to rv_chat.
        layoutManager = new LinearLayoutManager(getActivity());

        // making the RecyclerView to add from the bottom
        // with that when we start the fragment it will start from the bottom
        // where are the newest messages instead from the top
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        rv_messages.setLayoutManager(layoutManager);
        adapter = new MessagesRVAdapter(messageList, getActivity(), firebaseUser);
        rv_messages.setAdapter(adapter);


        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                layoutManager.scrollToPosition(adapter.getItemCount());
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        // Restore RecyclerView state

        if (mBundleRVstate != null) {
            Parcelable listState = mBundleRVstate.getParcelable(RV_STATE_KEY);
            rv_messages.getLayoutManager().onRestoreInstanceState(listState);
        }

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                messageList.add(message);
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
                if (!message.getSentByUserUid().trim().equals(firebaseUser.getUid().trim())) {
                    return;
                }
                // scroll down if the user currently at the bottom of the recyclerview
                // currently doesn't working
                rv_messages.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                            if(!recyclerView.canScrollVertically(1) && dy > 0) {
                                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                    @Override
                                    public void onItemRangeInserted(int positionStart, int itemCount) {
                                        layoutManager.smoothScrollToPosition(rv_messages, null, adapter.getItemCount());
                                    }
                                });
                            }
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addChildEventListener(childEventListener);

    }

    @Override
    public void onPause() {
        super.onPause();

        // remove the event listener,
        // to ensure that there is no 2 event listener at the same time.
        myRef.removeEventListener(childEventListener);

        // save RecyclerView instance
        mBundleRVstate = new Bundle();
        Parcelable listState = rv_messages.getLayoutManager().onSaveInstanceState();
        mBundleRVstate.putParcelable(RV_STATE_KEY, listState);
    }

    private boolean isValidMessage() {
        String text = et_message.getText().toString().trim();
        if (text.isEmpty()) {
            fab_addMessage.setVisibility(View.GONE);
            return false;
        }

        fab_addMessage.setVisibility(View.VISIBLE);
        return true;
    }
}