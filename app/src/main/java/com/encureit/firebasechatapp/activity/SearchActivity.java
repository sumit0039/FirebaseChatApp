package com.encureit.firebasechatapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.encureit.firebasechatapp.adapters.SearchAdapter;
import com.encureit.firebasechatapp.databinding.ActivitySearchBinding;
import com.encureit.firebasechatapp.model.UserModel;
import com.encureit.firebasechatapp.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private SearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchUserInput.requestFocus();

        binding.backBtn.setOnClickListener(v -> {
           onBackPressed();
        });

        binding.searchUserBtn.setOnClickListener(v -> {
            String searchTerm = binding.searchUserInput.getText().toString();
            if (searchTerm.isEmpty() || searchTerm.length()<3){
                binding.searchUserInput.setError("Invalid Username");
                return;
            }
            setupSearchRV(searchTerm);
        });
    }

    private void setupSearchRV(String searchTerm) {
        Query query = FirebaseUtils.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("userName",searchTerm);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();
        adapter = new SearchAdapter(options,getApplicationContext());
        binding.searchUserRv.setLayoutManager(new LinearLayoutManager(this));
        binding.searchUserRv.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}