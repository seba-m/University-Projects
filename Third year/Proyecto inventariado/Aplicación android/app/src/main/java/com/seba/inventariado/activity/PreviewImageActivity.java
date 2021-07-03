package com.seba.inventariado.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.seba.inventariado.R;

public class PreviewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        Toolbar toolbar = findViewById(R.id.toolbar_image_preview);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Uri uri = getIntent().getParcelableExtra("imageURI");
        String url = getIntent().getStringExtra("imageURL");
        ImageView imageView = findViewById(R.id.image_preview);

        if (uri != null) {
            Glide.with(this)
                    .load(uri)
                    .error(AppCompatResources.getDrawable(this, R.drawable.ic_products_missing_image))
                    .into(imageView);

        } else if (url != null) {
            Glide.with(this)
                    .load(url)
                    .error(AppCompatResources.getDrawable(this, R.drawable.ic_products_missing_image))
                    .into(imageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
