package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.Objects;

public class LostObjectsActivity extends AppCompatActivity {

    DatabaseReference reference;
    Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_objects);

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Objects");

        final ImageView imageView2 = findViewById(R.id.imageView2);
        final ImageView imageView3 = findViewById(R.id.imageView3);
        final ImageView imageView4= findViewById(R.id.imageView4);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot childNext : dataSnapshot.getChildren()) {
                        if (childNext.getKey().equals("image") && child.getValue(String.class).equals("0")){
                            picasso.get().load(childNext.getValue(String.class).replace("data:image/png;base64, ", "")).into(imageView2);
                        }
                        if (childNext.getKey().equals("image") && child.getValue(String.class).equals("1")){
                            picasso.get().load(childNext.getValue(String.class).replace("data:image/png;base64, ", "")).into(imageView3);
                        }
                        if (childNext.getKey().equals("image") && child.getValue(String.class).equals("2")){
                            picasso.get().load(childNext.getValue(String.class).replace("data:image/png;base64, ", "")).into(imageView4);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LostObjectsActivity.this, "Message d'erreur", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
