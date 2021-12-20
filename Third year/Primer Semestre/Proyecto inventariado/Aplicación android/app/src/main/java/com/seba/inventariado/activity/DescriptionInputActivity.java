package com.seba.inventariado.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.seba.inventariado.R;

import org.apache.commons.lang3.StringUtils;

public class DescriptionInputActivity extends AppCompatActivity {

    private String finalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_input);

        Toolbar toolbar = findViewById(R.id.toolbar_image_preview);
        toolbar.setTitle("Product description");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        EditText descripcion = findViewById(R.id.etDescription);
        Button guardar = findViewById(R.id.saveDescription);
        guardar.setEnabled(false);
        guardar.setVisibility(View.GONE);

        finalText = getIntent().getStringExtra("textoActual");

        if (StringUtils.isBlank(finalText)) finalText = "";

        descripcion.setText(finalText);

        descripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                guardar.setEnabled(!finalText.contentEquals(s));
                guardar.setVisibility(finalText.contentEquals(s) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        guardar.setOnClickListener(v -> {
            if (!descripcion.getText().toString().equals(finalText)) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("textoResultado", descripcion.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                guardar.setEnabled(false);
                guardar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}