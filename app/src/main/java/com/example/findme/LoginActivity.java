package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email, password;
    Button LoginButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        LoginButton = findViewById(R.id.LoginButton);

        auth = FirebaseAuth.getInstance();

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_email = Objects.requireNonNull(email.getText()).toString();
                String text_password = Objects.requireNonNull(password.getText()).toString();

                if (TextUtils.isEmpty(text_email) | TextUtils.isEmpty(text_password)){
                    Toast.makeText(LoginActivity.this, "Veillez remplir tous les champs s'il vous plaît", Toast.LENGTH_SHORT).show();
                } else if (text_password.length() < 8){
                    Toast.makeText(LoginActivity.this, "Le mot de passe doit contenir 8 caractères minimum", Toast.LENGTH_SHORT).show();
                } else {
                    login( text_email, text_password);
                }
            }
        });
    }

    public void login(final String email, final String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, ProfilActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } else{
                            Toast.makeText(LoginActivity.this, "L'identifiant ou le mot de passe est incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
