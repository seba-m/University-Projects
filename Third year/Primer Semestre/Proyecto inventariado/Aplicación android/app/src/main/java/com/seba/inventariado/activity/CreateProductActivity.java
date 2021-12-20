package com.seba.inventariado.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.MimeType;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.seba.inventariado.R;
import com.seba.inventariado.model.Producto;
import com.seba.inventariado.model.Tag;
import com.seba.inventariado.model.TipoImagen;
import com.seba.inventariado.utils.ApiClient;
import com.seba.inventariado.utils.FileUtils;
import com.seba.inventariado.utils.PreviewImageAdapter;
import com.seba.inventariado.utils.ServerService;
import com.seba.inventariado.utils.SessionManager;
import com.seba.inventariado.utils.TagCompletionView;
import com.tokenautocomplete.TokenCompleteTextView;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sangcomz.fishbun.define.Define.INTENT_PATH;

public class CreateProductActivity extends AppCompatActivity {

    private TagCompletionView completionView;
    private EditText nombreProducto;
    private Button descripcionProducto;
    private EditText cantidadProducto;
    private EditText precioProducto;
    private String minLvl;
    private String minLvlOption;
    private boolean minLvlEnabled;
    private RecyclerView recListItems;
    private RelativeLayout addPhotos;
    private ImageView imageSlider;
    private ViewSwitcher switcher;
    private TextView totalValue;
    private final List<TipoImagen> listaImagenes = new ArrayList<>();
    private final int maxFotos = 10 - listaImagenes.size();
    private final int DESCRIPTION_ACTIVITY = 0x12345;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_product_layout);

        Toolbar toolbar = findViewById(R.id.toolbar_product);
        toolbar.setTitle(R.string.add_product_title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        nombreProducto = findViewById(R.id.txt_product_name);
        descripcionProducto = findViewById(R.id.etDescription);
        descripcionProducto.setOnClickListener(v -> {
            Intent intent = new Intent(CreateProductActivity.this, DescriptionInputActivity.class);

            if (descripcionProducto.getText() != null && !StringUtils.isBlank(descripcionProducto.getText().toString()))
                intent.putExtra("textoActual", descripcionProducto.getText());
            else
                intent.putExtra("textoActual", "");

            startActivityForResult(intent, DESCRIPTION_ACTIVITY);
        });

        cantidadProducto = findViewById(R.id.valueQuantity);
        cantidadProducto.setText("1");
        precioProducto = findViewById(R.id.valuePrice);
        precioProducto.setText("1");

        totalValue = findViewById(R.id.valueTotalValue);

        precioProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isBlank(s) && !StringUtils.isBlank(precioProducto.getText().toString())) {
                    totalValue.setText(new BigInteger(s.toString()).multiply(new BigInteger(cantidadProducto.getText().toString())).toString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", "."));
                } else {
                    totalValue.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cantidadProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isBlank(s) && !StringUtils.isBlank(precioProducto.getText().toString())) {
                    totalValue.setText(new BigInteger(s.toString()).multiply(new BigInteger(precioProducto.getText().toString())).toString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", "."));
                } else {
                    totalValue.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        switcher = findViewById(R.id.switch_new_product_images);
        switcher.setDisplayedChild(0);

        recListItems = findViewById(R.id.previewImages);
        recListItems.setLayoutManager(new GridLayoutManager(this, 6));

        ImageButton alarma = findViewById(R.id.alarma_new_product);
        alarma.setOnClickListener(click -> {
            Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_alarm);

            Spinner minLvlOptionSelected = dialog.findViewById(R.id.spinner_alarm);
            if (!StringUtils.isBlank(minLvlOption) && !minLvlOption.equals("0")) {
                minLvlOptionSelected.setSelection(Integer.parseInt(minLvlOption));
            }
            minLvlOptionSelected.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        minLvlOption = "0";
                        minLvlEnabled = false;
                        alarma.setImageDrawable(AppCompatResources.getDrawable(CreateProductActivity.this, R.drawable.ic_bell_disabled));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            EditText minLvlValue = dialog.findViewById(R.id.minLvlAlarm);
            if (!StringUtils.isBlank(minLvl)) {
                minLvlValue.setText(minLvl);
            }

            minLvlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (StringUtils.isBlank(s)) {
                        minLvlValue.setText(null);
                        minLvlEnabled = false;
                        //alarma.setImageDrawable(AppCompatResources.getDrawable(CreateProductActivity.this, R.drawable.ic_bell_disabled));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            Button btnCancelar = dialog.findViewById(R.id.cancel_alarm);
            btnCancelar.setOnClickListener((v) -> {
                minLvl = minLvlValue.getText().toString();
                minLvlOption = String.valueOf(minLvlOptionSelected.getSelectedItemPosition());
                dialog.dismiss();
            });

            Button btnAgregarAlarma = dialog.findViewById(R.id.add_alarm);
            btnAgregarAlarma.setOnClickListener((v) -> {
                if (!StringUtils.isBlank(minLvlValue.getText().toString()) && minLvlOptionSelected.getSelectedItemPosition() != 0) {
                    minLvl = minLvlValue.getText().toString();
                    minLvlOption = String.valueOf(minLvlOptionSelected.getSelectedItemPosition());
                    minLvlEnabled = true;
                    alarma.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_bell_enabled));
                    dialog.dismiss();
                    return;
                }

                if (StringUtils.isBlank(minLvlValue.getText().toString())) {
                    minLvl = null;
                    minLvlEnabled = false;
                    alarma.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_bell_disabled));
                }

                if (minLvlOptionSelected.getSelectedItemPosition() == 0) {
                    minLvlOption = "0";
                    minLvlEnabled = false;
                    alarma.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_bell_disabled));
                }
                dialog.dismiss();
            });

            Button btnDescartar = dialog.findViewById(R.id.delete_alarm);
            btnDescartar.setOnClickListener((v) -> {
                minLvl = null;
                minLvlOption = "0";
                minLvlEnabled = false;
                alarma.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_bell_disabled));
                dialog.dismiss();
            });


            dialog.setOnCancelListener((v) -> {
                if (!StringUtils.isBlank(minLvlValue.getText().toString())) {
                    minLvl = minLvlValue.toString();
                }

                if (minLvlOptionSelected.getSelectedItemPosition() != 0) {
                    minLvlOption = String.valueOf(minLvlOptionSelected.getSelectedItemPosition());
                }
                dialog.dismiss();
            });
            dialog.show();
        });

        addPhotos = findViewById(R.id.add_photos);
        addPhotos.setOnClickListener(v ->
                FishBun.with(CreateProductActivity.this)
                        .setImageAdapter(new GlideAdapter())
                        .setMaxCount(maxFotos)
                        .setPickerSpanCount(4)
                        .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
                        .setActionBarTitleColor(Color.parseColor("#000000"))
                        .setAlbumSpanCount(1, 2)
                        .setButtonInAlbumActivity(true)
                        //.hasCameraInPickerPage(true)
                        .setCamera(true)
                        .exceptMimeType(Arrays.asList(MimeType.GIF, MimeType.WEBP, MimeType.BMP))
                        .setReachLimitAutomaticClose(false)
                        .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(CreateProductActivity.this, R.drawable.ic_arrow_back_black_24dp))
                        .setDoneButtonDrawable(ContextCompat.getDrawable(CreateProductActivity.this, R.drawable.ic_check_black_24dp))
                        .setAllViewTitle("All of your photos")
                        .setActionBarTitle("Select images to upload.")
                        .textOnImagesSelectionLimitReached("You can't select any more.")
                        .textOnNothingSelected("I need a photo!")
                        .startAlbum());

        completionView = findViewById(R.id.TagCompletion);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
        completionView.preventFreeFormText(false);
        completionView.setThreshold(1);

        Button botonGuardarProducto = findViewById(R.id.bSaveProduct);
        botonGuardarProducto.setOnClickListener((v) -> {
            if (StringUtils.isBlank(nombreProducto.getText())) {
                nombreProducto.setError("Product name is required.");
            }

            if (StringUtils.isBlank(precioProducto.getText())) {
                nombreProducto.setError("Price is required.");
            }

            if (StringUtils.isBlank(cantidadProducto.getText())) {
                nombreProducto.setError("Quantity is required.");
            }

            if (!StringUtils.isBlank(nombreProducto.getText()) && !StringUtils.isBlank(cantidadProducto.getText().toString()) && !StringUtils.isBlank(precioProducto.getText().toString())) {
                saveProduct();
            }
        });

        imageSlider = findViewById(R.id.image_slider);

        getTags();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DESCRIPTION_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                descripcionProducto.setText(data.getStringExtra("textoResultado"));
            }
        } else {

            if (resultCode == RESULT_OK) {
                for (Parcelable foto : data.getParcelableArrayListExtra(INTENT_PATH)) {

                    TipoImagen imagen = new TipoImagen((Uri) foto);

                    if (!listaImagenes.contains(imagen) && listaImagenes.size() < 11) {
                        listaImagenes.add(imagen);
                        if (switcher.getDisplayedChild() == 0) {
                            switcher.setDisplayedChild(1);
                        }
                    } else if (listaImagenes.size() > 10) {
                        addPhotos.setVisibility(View.GONE);
                        break;
                    }
                }

                PreviewImageAdapter pv = new PreviewImageAdapter(this, listaImagenes, imageSlider, recListItems, switcher);
                recListItems.setAdapter(pv);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CreateProductActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!StringUtils.isBlank(nombreProducto.getText().toString()) || !StringUtils.isBlank(descripcionProducto.getText().toString()) || !StringUtils.isBlank(cantidadProducto.getText().toString()) || !StringUtils.isBlank(precioProducto.getText().toString())) {
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.discard_changes);

                Button btnCancelar = dialog.findViewById(R.id.boton_cancelar_cambios);
                btnCancelar.setOnClickListener((v) -> dialog.dismiss());

                Button btnDescartar = dialog.findViewById(R.id.boton_descartar_cambios);
                btnDescartar.setOnClickListener((v) -> {
                    dialog.dismiss();
                    finish();
                });
                dialog.show();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!StringUtils.isBlank(nombreProducto.getText().toString()) || !StringUtils.isBlank(descripcionProducto.getText().toString()) || !StringUtils.isBlank(cantidadProducto.getText().toString()) || !StringUtils.isBlank(precioProducto.getText().toString()) || listaImagenes.size() > 0) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.discard_changes);

            Button btnCancelar = dialog.findViewById(R.id.boton_cancelar_cambios);
            btnCancelar.setOnClickListener((v) -> dialog.dismiss());

            Button btnDescartar = dialog.findViewById(R.id.boton_descartar_cambios);
            btnDescartar.setOnClickListener((v) -> super.onBackPressed());
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    private void saveProduct() {
        SessionManager session;
        session = new SessionManager(this);

        JSONObject productoAGuardar = new JSONObject();
        try {
            productoAGuardar.put("nombre", nombreProducto.getText().toString());
            productoAGuardar.put("precio", precioProducto.getText().toString());
            productoAGuardar.put("cantidad", cantidadProducto.getText().toString());
            productoAGuardar.put("minLvl", String.valueOf(minLvl));
            productoAGuardar.put("minLvlOption", String.valueOf(minLvlOption));
            productoAGuardar.put("minLvlEnabled", String.valueOf(minLvlEnabled));
            productoAGuardar.put("descripcion", descripcionProducto.getText().toString());

            if (!(completionView.getObjects().size() == 0)) {
                JSONArray allDataArray = new JSONArray();
                for (Tag tag : completionView.getObjects()) {
                    try {
                        JSONObject eachData = new JSONObject();
                        eachData.put("nombre", tag.getNombre());
                        eachData.put("id", tag.getId());
                        allDataArray.put(eachData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                productoAGuardar.put("tags", allDataArray);
            } else {
                productoAGuardar.put("tags", null);
            }

        } catch (Exception e) {
            Toast.makeText(this, "An error ocurred, please try again, or report it.", Toast.LENGTH_LONG).show();
            finish();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), productoAGuardar.toString());

        List<MultipartBody.Part> list = new ArrayList<>();

        for (TipoImagen imagen : listaImagenes) {
            Uri uri = imagen.getTipoUri();
            if (uri != null && !Uri.EMPTY.equals(uri)) {
                list.add(prepareFilePart(uri));
            }
        }

        ServerService saveProduct = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
        Call<Producto> LoginResponseCall = saveProduct.saveProduct(requestBody, list);
        LoginResponseCall.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(@NotNull Call<Producto> call, @NotNull Response<Producto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("productoEditado", response.body());
                    setResult(Activity.RESULT_OK, returnIntent);
                }
                finish();
            }

            @Override
            public void onFailure(@NotNull Call<Producto> call, @NotNull Throwable t) {
                new AlertDialog.Builder(CreateProductActivity.this)
                        .setTitle("Error")
                        .setMessage("Internet not available, check your internet connection and try again.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", (dialog, which) -> finishAffinity()).show();
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(Uri fileUri) {
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    private void getTags() {
        SessionManager session;
        session = new SessionManager(CreateProductActivity.this);

        ServerService getTags = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
        Call<List<Tag>> LoginResponseCall = getTags.allTags();
        LoginResponseCall.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(@NotNull Call<List<Tag>> call, @NotNull Response<List<Tag>> response) {
                List<Tag> allTags = Collections.emptyList();

                if (response.isSuccessful()) {
                    allTags = (response.body());
                }

                ArrayAdapter<Tag> adapter = new ArrayAdapter<>(CreateProductActivity.this, android.R.layout.simple_spinner_dropdown_item, allTags);
                completionView.setAdapter(adapter);

            }

            @Override
            public void onFailure(@NotNull Call<List<Tag>> call, @NotNull Throwable t) {

                new AlertDialog.Builder(CreateProductActivity.this)
                        .setTitle("Error")
                        .setMessage("Internet not available, check your internet connection and try again.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", (dialog, which) -> finish()).show();
            }
        });
    }
}
