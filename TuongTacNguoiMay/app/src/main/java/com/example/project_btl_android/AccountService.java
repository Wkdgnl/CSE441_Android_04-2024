package com.example.project_btl_android;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CompletableFuture;

public class AccountService {
    DatabaseReference db;
    public AccountService() {
        this.db = FirebaseDatabase.getInstance().getReference("Account");
    }

    public CompletableFuture<String> getUserNameById(String id) {
        CompletableFuture<String> future = new CompletableFuture<>();

        db.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("UserName").getValue(String.class);
                    future.complete(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }
}
