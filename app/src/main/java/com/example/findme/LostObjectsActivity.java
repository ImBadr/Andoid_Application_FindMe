package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.example.findme.R.*;
import static com.example.findme.R.string.*;


public class LostObjectsActivity extends AppCompatActivity {

    DatabaseReference reference;
    LinearLayout linearLayout;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_lost_objects);

        linearLayout = findViewById(id.LinearViewImages);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        reference = FirebaseDatabase.getInstance().getReference(getString(Path_Objects));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    final View view = layoutInflater.inflate(layout.my_scroll_layout, linearLayout, false);

                    reference.child("/" + child.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childNext : dataSnapshot.getChildren()) {

                                TextView room = view.findViewById(id.Room);
                                TextView hour = view.findViewById(id.Hour);
                                TextView date = view.findViewById(id.Date);
                                ImageView image = view.findViewById(id.image);
                                TextView description = view.findViewById(id.Description);

                                if (Objects.equals(childNext.getKey(), getString(string.room))){
                                    room.setText(childNext.getValue(String.class));
                                }
                                if (Objects.equals(childNext.getKey(), getString(string.hour))){
                                    hour.setText(childNext.getValue(String.class));
                                }
                                if (Objects.equals(childNext.getKey(), getString(string.date))){
                                    date.setText(childNext.getValue(String.class));
                                }
                                if (Objects.equals(childNext.getKey(), getString(string.description))){
                                    description.setText(childNext.getValue(String.class));
                                }
                                if (Objects.equals(childNext.getKey(), getString(string.image))){
                                    byte[] image64 = Base64.decode(Objects.requireNonNull(childNext.getValue(String.class)).replace(getString(string.replace_base_64_string), ""), Base64.DEFAULT);
                                    image.setImageBitmap(BitmapFactory.decodeByteArray( image64, 0, image64.length));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(LostObjectsActivity.this, getString(string.error), Toast.LENGTH_SHORT).show();
                        }
                    });

                    linearLayout.addView(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LostObjectsActivity.this, getString(string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
