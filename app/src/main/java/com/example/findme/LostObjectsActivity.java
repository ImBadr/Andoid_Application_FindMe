package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Base64;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_objects);

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Objects/");

        final ImageView imageView2 = findViewById(R.id.imageView2);
        final ImageView imageView3 = findViewById(R.id.imageView3);
        final ImageView imageView4= findViewById(R.id.imageView4);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    reference.child("/" + child.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childNext : dataSnapshot.getChildren()) {
                                Log.i(childNext.getKey(), Objects.requireNonNull(childNext.getValue(String.class)));
                                if (Objects.equals(childNext.getKey(), "image")){
                                    Log.i("Image" , Objects.requireNonNull(childNext.getValue(String.class)));
                                    byte[] image = Base64.decode(Objects.requireNonNull(childNext.getValue(String.class)).replace("data:image/png;base64,", ""), Base64.DEFAULT);
                                    imageView2.setImageBitmap(BitmapFactory.decodeByteArray( image, 0, image.length));
                                }
                                if ( Objects.equals(childNext.getKey(), "image") && Objects.equals(child.getKey(), "1")){
                                    byte[] image = Base64.decode(Objects.requireNonNull(childNext.getValue(String.class)).replace("data:image/png;base64,", ""), Base64.DEFAULT);
                                    imageView3.setImageBitmap(BitmapFactory.decodeByteArray( image, 0, image.length));
                                }
                                if ( childNext.getKey().equals("image") ){
                                    byte[] image = Base64.decode(Objects.requireNonNull(childNext.getValue(String.class)).replace("data:image/png;base64,", ""), Base64.DEFAULT);
                                    imageView4.setImageBitmap(BitmapFactory.decodeByteArray( image, 0, image.length));
                                    //Picasso.get().load(Objects.requireNonNull(childNext.getValue(String.class)).replace("data:image/png;base64, ", "")).into(imageView4);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LostObjectsActivity.this, "Message d'erreur", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
