package com.encureit.firebasechatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.encureit.firebasechatapp.R;
import com.encureit.firebasechatapp.adapters.ChatAdapter;
import com.encureit.firebasechatapp.adapters.SearchAdapter;
import com.encureit.firebasechatapp.databinding.ActivityChatBinding;
import com.encureit.firebasechatapp.model.ChatMessageModel;
import com.encureit.firebasechatapp.model.ChatRoomModel;
import com.encureit.firebasechatapp.model.UserModel;
import com.encureit.firebasechatapp.utils.AndroidUtils;
import com.encureit.firebasechatapp.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private UserModel otherModel;
    private ChatRoomModel chatRoomModel;
    private String chatroomId;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        otherModel = AndroidUtils.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtils.getChatroomId(FirebaseUtils.currentUserId(),otherModel.getUserId());
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.userNameTxt.setText(otherModel.getUserName());
        binding.messageSendBtn.setOnClickListener(v -> {
            String message = binding.writeMessageTxt.getText().toString();
            if (message.isEmpty())
                return;
            setMessageToUser(message);
        });
        getOrCreateChatRoomModel();
        setUpChatRecyclerVIew();
    }

    private void setUpChatRecyclerVIew() {
        Query query = FirebaseUtils.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();
        chatAdapter = new ChatAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        binding.chatRv.setLayoutManager(manager);
        binding.chatRv.setAdapter(chatAdapter);
        chatAdapter.startListening();
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                binding.chatRv.smoothScrollToPosition(0);
            }
        });
    }

    private void setMessageToUser(String message) {
        chatRoomModel.setLastMessageSenderId(FirebaseUtils.currentUserId());
        chatRoomModel.setLastMessageTimeStamp(Timestamp.now());
        chatRoomModel.setLastMessage(message);
        FirebaseUtils.getChatRoomReference(chatroomId).set(chatRoomModel);
        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtils.currentUserId(),Timestamp.now());

        FirebaseUtils.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            binding.writeMessageTxt.setText("");
                        }
                    }
                });
    }

    private void getOrCreateChatRoomModel() {
        FirebaseUtils.getChatRoomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if (chatRoomModel == null){
                    chatRoomModel = new ChatRoomModel(
                            chatroomId,
                            Arrays.asList(FirebaseUtils.currentUserId(),otherModel.getUserId()),
                            Timestamp.now(),
                            "",
                            ""
                    );
                    FirebaseUtils.getChatRoomReference(chatroomId).set(chatRoomModel);
                }
            }
        });
    }
}