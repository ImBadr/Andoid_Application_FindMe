package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfilActivity extends AppCompatActivity {

    Button logoutButton;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

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

                /* Afficher le résultat dans la console
                 */
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i("KEY", Objects.requireNonNull(child.getKey()));
                    Log.i("VALUE", Objects.requireNonNull(child.getValue(String.class)));
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