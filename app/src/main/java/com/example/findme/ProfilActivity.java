package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfilActivity extends AppCompatActivity {

    Button logoutButton;
    DatabaseReference reference;
    TextView image, username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        this.image = findViewById(R.id.image);
        this.username = findViewById(R.id.username);
        this.password = findViewById(R.id.password);

        /* Récuperer des données depuis FireBase
         * Ici je récupère les données en récupérant l'identifiant de l'utilisateur current
         * je crée une écoute dans la BD appélé "Reference" en indiquant le chemin où je souhaite lire les données
         */
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users/" + userId);


        /* Une fois l'écouté placé sur le bon chemin, ici --> "Users/(userId=ilbuhclqmiq634q6sd36qsd)/username
         * ce chemin pointe directement la donnée représentant le nom de l'utilisateur courant
         */
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /* Le DataSnapShot contient sous forme
                 * de HashMap les data s'il y a plusieurs
                 * de Variable s'il ne contient qu'une valeur
                 * Ici --> dataSnapShot contient toutes les data de l'utilisateur courant
                 */

                /*Log.i("CLE", Objects.requireNonNull(dataSnapshot.getKey()));
                Log.i("VALUE", Objects.requireNonNull(dataSnapshot.getValue(String.class)));*/

                /* Afficher le résultat dans la console
                 */

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (Objects.equals(child.getKey(), "id"))
                        password.setText(child.getValue(String.class));
                    if (Objects.equals(child.getKey(), "username"))
                        username.setText(child.getValue(String.class));
                    if (Objects.equals(child.getKey(), "imageURL"))
                        image.setText(child.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfilActivity.this, getString(R.string.failed_getUserInformations), Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        /* Déconnexion du compte à la BD
         */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfilActivity.this, MainActivity.class));
            }
        });
    }
}