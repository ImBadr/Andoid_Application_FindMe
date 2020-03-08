package com.example.findme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class NewObjectActivity extends AppCompatActivity {

    DatabaseReference reference;

    Button ButtonHeure, dateButton, takePictureButton, shareButton;
    MaterialEditText salle;
    TextView date, heure, description;
    ImageView image;
    Bitmap bitmap;

    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_object);

        ButtonHeure = findViewById(R.id.ButtonHeure);
        dateButton = findViewById(R.id.ButtonDate);
        takePictureButton = findViewById(R.id.ButtonTakePicture);
        shareButton = findViewById(R.id.ButtonShare);
        salle = findViewById(R.id.Salle);
        date = findViewById(R.id.Date);
        heure = findViewById(R.id.Heure);
        image = findViewById(R.id.ImageTaked);
        description = findViewById(R.id.Description);

        ButtonHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(NewObjectActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        heure.setText(hourOfDay + ":" + minute);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(NewObjectActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, 0, 0, 0);
                datePickerDialog.show();
            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Faire des vérification
                 * appeler une méthode pour envoyer les données
                 */
            }
        });
    }

    private String getImageInBase64(Bitmap image){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        image.setImageBitmap((Bitmap) Objects.requireNonNull(data.getExtras()).get("data"));
    }

    private void register(final String username, final String email, String password){

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("date", date.toString());
        hashMap.put("description", description.toString());
        hashMap.put("heure", heure.toString());
        hashMap.put("image", bitmap.toString());
        hashMap.put("salle",salle.toString());


        byte[] array = new byte[10];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);

        reference = FirebaseDatabase.getInstance().getReference("Objects").child(generatedString);

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(NewObjectActivity.this, LostObjectsActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                    );
                    finish();
                }
            }
        });
    }

}
