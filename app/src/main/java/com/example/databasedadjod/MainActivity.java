package com.example.databasedadjod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName, editTextSecName, editTextEmail;
    private DatabaseReference myDataBase;
    private String UserKey;
    private ImageView imageBD;
    private StorageReference storageRef;
    private Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        editTextName = findViewById(R.id.editTextName);
        editTextSecName = findViewById(R.id.editTextSecondName);
        editTextEmail = findViewById(R.id.editTextEmail);
        UserKey = "User";
        myDataBase = FirebaseDatabase.getInstance().getReference(UserKey);
        imageBD = findViewById(R.id.imageView);
        storageRef = FirebaseStorage.getInstance().getReference("ImageDB");
    }

    public void onClickButtonSave(View view) {
        uploadImage();
    }

    private void saveUser() {
        String id = myDataBase.push().getKey();
        String name = editTextName.getText().toString();
        String secName = editTextSecName.getText().toString();
        String email = editTextEmail.getText().toString();

        User newUser = new User(id, name, secName, email, uploadUri.toString());

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(secName) && !TextUtils.isEmpty(email)) {

            if (id != null) {
                myDataBase.child(id).setValue(newUser);
            }

            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Напишите данные во всех полях", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickButtonRead(View view) {
        Intent intent = new Intent(MainActivity.this, ReadActivity.class);
        startActivity(intent);
    }

    public void onClickButtonChooseImage(View view) {
        getImage();
    }

    private void getImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                Log.d("MyLog", "Image URI : " + data.getData());
                imageBD.setImageURI(data.getData());
            }
        }
    }

    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imageBD.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        final StorageReference mountainsRef = storageRef.child("mountains.jpg");
        UploadTask uploadTask = mountainsRef.putBytes(byteArray);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mountainsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
                saveUser();

                Toast.makeText(MainActivity.this, "Картинка выбрана", Toast.LENGTH_SHORT).show();
            }
        });
    }
}