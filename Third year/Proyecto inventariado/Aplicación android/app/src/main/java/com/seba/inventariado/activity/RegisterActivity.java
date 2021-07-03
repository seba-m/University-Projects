package com.seba.inventariado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seba.inventariado.R;
import com.seba.inventariado.utils.ApiClient;
import com.seba.inventariado.utils.ServerService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText mFirstName;
    EditText mLastName;
    EditText mTextEmail;
    EditText mTextPassword;
    Button mButtonSignUp;
    ImageButton mButtonLoginHere;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mTextEmail = findViewById(R.id.email);
        mTextPassword = findViewById(R.id.password);
        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastName);

        mButtonLoginHere = findViewById(R.id.boton_back_login);
        mButtonLoginHere.setOnClickListener(v -> new Handler().postDelayed(() -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)), 300));

        mButtonSignUp = findViewById(R.id.boton_registro);
        mButtonSignUp.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mTextEmail.getText().toString()) || TextUtils.isEmpty(mTextPassword.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "All fields are required.", Toast.LENGTH_LONG).show();
            } else {
                register();
            }
        });
    }

    public void register() {
        String Email = (mTextEmail.getText().toString());
        String Password = (mTextPassword.getText().toString());
        String FirstName = (mFirstName.getText().toString());
        String LastName = (mLastName.getText().toString());

        ServerService test = ApiClient.createService(ServerService.class);
        Call<Void> LoginResponseCall = test.register(Email, Password, FirstName, LastName);
        LoginResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                Toast.makeText(RegisterActivity.this, "Check your email, to activate your account.", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(() -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)), 700);
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Failed to send the email.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
