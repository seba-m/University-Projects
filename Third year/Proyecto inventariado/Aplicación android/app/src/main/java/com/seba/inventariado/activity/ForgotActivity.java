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

public class ForgotActivity extends AppCompatActivity {

    ImageButton mButtonLogin;
    Button mButtonEmail;
    EditText mTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        mTextEmail = findViewById(R.id.email);
        mButtonLogin = findViewById(R.id.boton_back_login);
        mButtonLogin.setOnClickListener(v -> new Handler().postDelayed(() -> startActivity(new Intent(ForgotActivity.this, LoginActivity.class)), 100));

        mButtonEmail = findViewById(R.id.boton_enviar_mail);
        mButtonEmail.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mTextEmail.getText().toString())) {
                Toast.makeText(ForgotActivity.this, "Email required.", Toast.LENGTH_LONG).show();
            } else {
                sendEmail();
            }
        });
    }

    public void sendEmail() {
        String Email = (mTextEmail.getText().toString());

        ServerService test = ApiClient.createService(ServerService.class);
        Call<Void> LoginResponseCall = test.recover(Email);
        LoginResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                Toast.makeText(ForgotActivity.this, "Check your email, a new password has been generated.", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(() -> startActivity(new Intent(ForgotActivity.this, LoginActivity.class)), 700);
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Toast.makeText(ForgotActivity.this, "Failed to send new password", Toast.LENGTH_LONG).show();
            }
        });
    }

}