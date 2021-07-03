package com.seba.inventariado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gk.emon.lovelyLoading.LoadingPopup;
import com.seba.inventariado.R;
import com.seba.inventariado.model.Users;
import com.seba.inventariado.utils.ApiClient;
import com.seba.inventariado.utils.ServerService;
import com.seba.inventariado.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText mTextEmail, mTextPassword;
    Button mButtonLogin, mButtonRegister, mButtonContraOlvidada;

    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        mTextEmail = findViewById(R.id.email);
        mTextPassword = findViewById(R.id.password);

        mButtonContraOlvidada = findViewById(R.id.boton_contra_olvidada);
        mButtonContraOlvidada.setOnClickListener(v -> new Handler().postDelayed(() -> startActivity(new Intent(LoginActivity.this, ForgotActivity.class)), 300));

        mButtonLogin = findViewById(R.id.boton_login);
        mButtonLogin.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mTextEmail.getText().toString()) || TextUtils.isEmpty(mTextPassword.getText().toString())) {
                Toast.makeText(LoginActivity.this, "Email / Password required.", Toast.LENGTH_LONG).show();
            } else {
                login();
            }
        });

        mButtonRegister = findViewById(R.id.boton_registro);
        mButtonRegister.setOnClickListener(v -> new Handler().postDelayed(() -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)), 400));
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void login() {
        LoadingPopup.getInstance(LoginActivity.this).defaultLovelyLoading().build();
        LoadingPopup.showLoadingPopUp();

        String Email = (mTextEmail.getText().toString());
        String Password = (mTextPassword.getText().toString());

        ServerService test = ApiClient.createService(ServerService.class, Email, Password);
        Call<Users> LoginResponseCall = test.login();
        LoginResponseCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Users users = response.body();
                    users.setEmail(Email);
                    Log.e("xd", users.getPhoto());
                    session.createLoginSession(users);

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(() -> {
                        LoadingPopup.hideLoadingPopUp();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }, 700);
                } else {
                    LoadingPopup.hideLoadingPopUp();
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
                LoadingPopup.hideLoadingPopUp();
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
