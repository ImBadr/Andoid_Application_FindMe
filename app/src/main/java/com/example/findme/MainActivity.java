package com.example.findme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button LoginButton;
    private Button RegisterButton;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Je crois que c'est pas comme ca que l'on fait pour récupérer l'utilisateur
         * connecté actuellement
         */
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        /* Si l'utilisateur ne s'est jamais déconnecté
         * On le redirige directement sur la page ProfilActivity
         */
        if (firebaseUser != null){
            startActivity(new Intent(MainActivity.this, ProfilActivity.class));
        }

        this.LoginButton = findViewById(R.id.LoginButton);
        this.RegisterButton = findViewById(R.id.RegisterButton);

        /* Redirection sur la page de connexion
         */
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        /* Redirection sur la page de d'enregistrement
         */
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

    }
}
