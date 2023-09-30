package com.encureit.firebasechatapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.encureit.firebasechatapp.R;
import com.encureit.firebasechatapp.adapters.RecentChatAdapter;
import com.encureit.firebasechatapp.adapters.SearchAdapter;
import com.encureit.firebasechatapp.databinding.FragmentChatBinding;
import com.encureit.firebasechatapp.model.ChatRoomModel;
import com.encureit.firebasechatapp.model.UserModel;
import com.encureit.firebasechatapp.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private RecentChatAdapter adapter;


    public ChatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        setupSearchRV();
        return binding.getRoot();
    }

    private void setupSearchRV() {
        Query query = FirebaseUtils.allChatroomCollectionReference()
                .whereArrayContains("userId",FirebaseUtils.currentUserId())
                .orderBy("lastMessageTimeStamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query,ChatRoomModel.class).build();
        adapter = new RecentChatAdapter(options,getActivity());
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.chatRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }
}