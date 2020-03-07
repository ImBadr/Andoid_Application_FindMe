package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LostObjectsActivity extends AppCompatActivity {

    DatabaseReference reference;
    LinearLayout linearLayout;
    LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_objects);

        linearLayout = findViewById(R.id.LinearViewImages);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        reference = FirebaseDatabase.getInstance().getReference("Objects/");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    View view = layoutInflater.inflate(R.layout.my_scroll_layout, linearLayout, false);

                    reference.child("/" + child.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childNext : dataSnapshot.getChildren()) {



                                /*if (Objects.equals(childNext.getKey(), "image")){
                                    Log.i("Image" , Objects.requireNonNull(childNext.getValue(String.class)));
                                    byte[] image = Base64.decode(Objects.requireNonNull(childNext.getValue(String.class)).replace("data:image/png;base64,", ""), Base64.DEFAULT);
                                    imageView2.setImageBitmap(BitmapFactory.decodeByteArray( image, 0, image.length));
                                }*/
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(LostObjectsActivity.this, "Message d'erreur", Toast.LENGTH_SHORT).show();
                        }
                    });

                    linearLayout.addView(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LostObjectsActivity.this, "Message d'erreur", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
