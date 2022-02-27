package com.example.databasedadjod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private Button buttonStart, buttonLogin, buttonRegistration, buttonExit;
    private TextView textViewUserEmail;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
    }

    private void init() {
        editTextEmail = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        mAuth = FirebaseAuth.getInstance();
        buttonStart = findViewById(R.id.buttonContinue);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonExit = findViewById(R.id.buttonExit);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            showSigned();
            userName = "Вы вошли как: " + currentUser.getEmail();
            textViewUserEmail.setText(userName);

            Toast.makeText(this, "Данный пользователь " + currentUser.getEmail() + " существует", Toast.LENGTH_SHORT).show();
        } else {
            signedNotShow();
            Toast.makeText(this, "Данного пользователя не существует", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickButtonLogin(View view) {
        if (!TextUtils.isEmpty(editTextEmail.getText().toString()) && !TextUtils.isEmpty(editTextPassword.getText().toString())) {
            mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        showSigned();
                        sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Пользователь вошел успешно", Toast.LENGTH_SHORT).show();
                    } else {
                        signedNotShow();
                        Toast.makeText(LoginActivity.this, "Пользователь не смог войти", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onClickButtonRegistration(View view) {
        if (!TextUtils.isEmpty(editTextEmail.getText().toString()) && !TextUtils.isEmpty(editTextPassword.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        showSigned();
                        Toast.makeText(getApplicationContext(), "Пользователь зарегистрировался", Toast.LENGTH_SHORT).show();
                    } else {
                        signedNotShow();
                        Toast.makeText(getApplicationContext(), "Пользователь не зарегистрирован", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Зарегистрируйтесь", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickButtonExit(View view) {
        FirebaseAuth.getInstance().signOut();
        signedNotShow();
    }

    public void onClickButtonStart(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showSigned() {
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        if (user.isEmailVerified()) {
            userName = "Вы вошли как: " + user.getEmail();
            textViewUserEmail.setText(userName);
            buttonStart.setVisibility(View.VISIBLE);
            buttonExit.setVisibility(View.VISIBLE);
            textViewUserEmail.setVisibility(View.VISIBLE);

            editTextEmail.setVisibility(View.GONE);
            editTextPassword.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.GONE);
            buttonRegistration.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "Перейдите на вашу почту для подтверждения Email адреса", Toast.LENGTH_SHORT).show();
        }
    }

    private void signedNotShow() {
        buttonStart.setVisibility(View.GONE);
        buttonExit.setVisibility(View.GONE);
        textViewUserEmail.setVisibility(View.GONE);

        editTextEmail.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        buttonLogin.setVisibility(View.VISIBLE);
        buttonRegistration.setVisibility(View.VISIBLE);
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Перейдите на вашу почту для подтверждения Email адреса", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Такого Email адреса не существует в нашей базе данных", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
