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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button RegisterButton;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        RegisterButton = findViewById(R.id.RegisterButton);

        auth = FirebaseAuth.getInstance();

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_username = username.getText().toString();
                String text_email = email.getText().toString();
                String text_password = password.getText().toString();
                /* Vérification de la saisie des données avant de pouvoir procédé à l'Enregistrement
                 */
                if (TextUtils.isEmpty(text_username) | TextUtils.isEmpty(text_email) | TextUtils.isEmpty(text_password)){
                    Toast.makeText(RegisterActivity.this, "Veillez remplir tous les champs s'il vous plaît", Toast.LENGTH_SHORT).show();
                } else if (text_password.length() < 8){
                    Toast.makeText(RegisterActivity.this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                } else {
                    register(text_username, text_email, text_password);
                }
            }
        });
    }

    private void register(final String username, final String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            /* Génère un UserID automatiquement
                             */
                            final String userId = firebaseUser.getUid();

                            /* Ici on crée une table Users dans lequel on range :
                             * ->userId
                             * ->username
                             * ->imageURL
                             */
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            /* on crée un HashMap où on range nos données
                             */
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            /* on Enregistre la HashMap et on lance l'activité ProfilActivity
                             */
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(RegisterActivity.this, ProfilActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .putExtra("username", username)
                                        );
                                        finish();
                                    }
                                }
                            });
                        }
                        /* Si l'utilisateur existe déjà
                         * on affiche un Toast
                         */
                        else {
                            Toast.makeText(RegisterActivity.this, "You can't be registred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
