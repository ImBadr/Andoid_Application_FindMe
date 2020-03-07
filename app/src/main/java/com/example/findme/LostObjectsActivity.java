package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
                    final View view = layoutInflater.inflate(R.layout.my_scroll_layout, linearLayout, false);

                    reference.child("/" + child.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childNext : dataSnapshot.getChildren()) {

                                TextView salle = view.findViewById(R.id.Salle);
                                TextView heure = view.findViewById(R.id.Heure);
                                TextView date = view.findViewById(R.id.Date);
                                ImageView image = view.findViewById(R.id.image);
                                TextView description = view.findViewById(R.id.Description);

                                if (Objects.equals(childNext.getKey(), "salle")){
                                    salle.setText(childNext.getValue(String.class));
                                }
                                if (Objects.equals(childNext.getKey(), "heure")){
                                    heure.setText(childNext.getValue(String.class));
                                }
                                if (Objects.equals(childNext.getKey(), "date")){
                                    date.setText(childNext.getValue(String.class));
                                }
                                if (Objects.equals(childNext.getKey(), "description")){
                                    description.setText(childNext.getValue(String.class));
                                }
                                if (Objects.equals(childNext.getKey(), "image")){
                                    byte[] image64 = Base64.decode(Objects.requireNonNull(childNext.getValue(String.class)).replace("data:image/png;base64,", ""), Base64.DEFAULT);
                                    image.setImageBitmap(BitmapFactory.decodeByteArray( image64, 0, image64.length));
                                }
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
