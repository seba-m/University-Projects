package com.seba.inventariado.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.MimeType;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.seba.inventariado.R;
import com.seba.inventariado.model.Users;
import com.seba.inventariado.utils.ApiClient;
import com.seba.inventariado.utils.FileUtils;
import com.seba.inventariado.utils.ServerService;
import com.seba.inventariado.utils.SessionManager;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sangcomz.fishbun.define.Define.INTENT_PATH;

public class ProfileFragment extends Fragment {

    Context context;
    SessionManager session;

    private TextInputEditText primerNombre;
    private TextInputEditText ultimoNombre;
    private TextInputEditText correo;

    private TextInputEditText passOriginal;
    private TextInputEditText passNueva;
    private TextInputEditText passRepetida;

    private Button saveButton;

    private CircleImageView imageView;
    private Uri uri;

    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        session = new SessionManager(context);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");

        saveButton = getActivity().findViewById(R.id.saveButton);
        saveButton.setVisibility(View.GONE);
        saveButton.setOnClickListener(v -> {
            if (verificarCampos()) {
                actualizarDatos();
            } else {
                saveButton.setVisibility(View.GONE);
            }
        });

        Users usuario = session.getUserDetails();

        FloatingActionButton fab = (getActivity()).findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        primerNombre = view.findViewById(R.id.profile_first_name);
        primerNombre.setText(usuario.getFirstName());
        primerNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isBlank(s) && !s.toString().equals(usuario.getFirstName())) {
                    saveButton.setVisibility(View.VISIBLE);
                } else {
                    saveButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ultimoNombre = view.findViewById(R.id.profile_last_name);
        ultimoNombre.setText(usuario.getLastName());
        ultimoNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isBlank(s) && !s.toString().equals(usuario.getLastName())) {
                    saveButton.setVisibility(View.VISIBLE);
                } else {
                    saveButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        correo = view.findViewById(R.id.profile_email);
        correo.setText(usuario.getEmail());
        correo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isBlank(s) && !s.toString().equals(usuario.getEmail())) {
                    saveButton.setVisibility(View.VISIBLE);
                } else {
                    saveButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passOriginal = view.findViewById(R.id.profile_password_original);
        passOriginal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isBlank(s) && !StringUtils.isBlank(passNueva.getText()) && !StringUtils.isBlank(passRepetida.getText())) {
                    saveButton.setVisibility(View.VISIBLE);
                } else {
                    saveButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passNueva = view.findViewById(R.id.profile_password_nueva);
        passNueva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isBlank(s) && !StringUtils.isBlank(passOriginal.getText()) && !StringUtils.isBlank(passRepetida.getText()) && s.toString().equals(passRepetida.getText().toString())) {
                    saveButton.setVisibility(View.VISIBLE);
                } else {
                    saveButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passRepetida = view.findViewById(R.id.profile_password_nueva_repetida);
        passRepetida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isBlank(s) && !StringUtils.isBlank(passOriginal.getText()) && !StringUtils.isBlank(passNueva.getText()) && s.toString().equals(passNueva.getText().toString())) {
                    saveButton.setVisibility(View.VISIBLE);
                } else {
                    saveButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button logout = view.findViewById(R.id.button_logout_account);
        logout.setOnClickListener(v -> session.logoutUser());

        Button eliminarCuenta = view.findViewById(R.id.button_delete_account);
        eliminarCuenta.setOnClickListener(v -> {
            ServerService deleteUser = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
            Call<Void> LoginResponseCall = deleteUser.deleteUserAccount();
            LoginResponseCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        session.logoutUser();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Internet not available, check your internet connection and try again.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNeutralButton("OK", (dialog, which) -> ((Activity) context).finishAffinity()).show();
                }
            });
        });

        imageView = view.findViewById(R.id.profile_image);
        imageView.setOnClickListener(v ->
                FishBun.with(ProfileFragment.this)
                        .setImageAdapter(new GlideAdapter())
                        .setMaxCount(1)
                        .setPickerSpanCount(4)
                        .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
                        .setActionBarTitleColor(Color.parseColor("#000000"))
                        .setAlbumSpanCount(1, 2)
                        .setButtonInAlbumActivity(true)
                        .setCamera(true)
                        .exceptMimeType(Arrays.asList(MimeType.GIF, MimeType.WEBP, MimeType.BMP))
                        .setReachLimitAutomaticClose(false)
                        .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_black_24dp))
                        .setDoneButtonDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_black_24dp))
                        .setAllViewTitle("All of your photos")
                        .setActionBarTitle("Select images to upload.")
                        .textOnImagesSelectionLimitReached("You can't select any more.")
                        .textOnNothingSelected("I need a photo!")
                        .startAlbum());

        imageView.setImageResource(0);

        Glide.with(context)
                .load(usuario.getPhoto())
                .error(R.drawable.avatardefault)
                .into(imageView);
        return view;
    }

    private boolean verificarCampos() {
        if (uri != null) {
            return true;
        }

        if (StringUtils.isBlank(primerNombre.getText())) {
            primerNombre.setError("First name cannot be empty.");
            return false;
        }

        if (StringUtils.isBlank(ultimoNombre.getText())) {
            ultimoNombre.setError("Last name cannot be empty.");
            return false;
        }

        if (StringUtils.isBlank(correo.getText())) {
            correo.setError("Email cannot be empty.");
            return false;
        }

        if (!StringUtils.isBlank(passOriginal.getText()) || !StringUtils.isBlank(passNueva.getText()) || !StringUtils.isBlank(passRepetida.getText())) {
            return !StringUtils.isBlank(passOriginal.getText()) && !StringUtils.isBlank(passNueva.getText()) && !StringUtils.isBlank(passRepetida.getText());
        }

        return true;
    }

    private void actualizarDatos() {
        saveButton.setVisibility(View.GONE);

        SessionManager session;
        session = new SessionManager(context);

        Users usuarioActual = session.getUserDetails();

        JSONObject usuarioAGuardar = new JSONObject();
        try {
            usuarioAGuardar.put("firstName", (primerNombre.getText() != null) ? primerNombre.getText().toString() : usuarioActual.getFirstName());
            usuarioAGuardar.put("lastName", (ultimoNombre.getText() != null) ? ultimoNombre.getText().toString() : usuarioActual.getLastName());
            usuarioAGuardar.put("email", (correo.getText() != null) ? correo.getText().toString() : usuarioActual.getEmail());
            usuarioAGuardar.put("passwordOriginal", (passOriginal.getText() != null) ? passOriginal.getText().toString() : "null");
            usuarioAGuardar.put("passwordChanged", (passNueva.getText() != null) ? passNueva.getText().toString() : "null");
            usuarioAGuardar.put("passwordChangedRepeated", (passRepetida.getText() != null) ? passRepetida.getText().toString() : "null");
        } catch (Exception e) {
            Toast.makeText(context, "An error ocurred, please try again, or report it.", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), usuarioAGuardar.toString());

        MultipartBody.Part foto = null;

        if (uri != null) {
            foto = prepareFilePart(uri);
        }

        ServerService saveProduct = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
        Call<Map<String, String>> LoginResponseCall = saveProduct.updateUserAccount(requestBody, foto);
        LoginResponseCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(@NotNull Call<Map<String, String>> call, @NotNull Response<Map<String, String>> response) {
                Users usuarioSharedPref = session.getUserDetails();
                usuarioSharedPref.setFirstName((primerNombre.getText() != null) ? primerNombre.getText().toString() : usuarioActual.getFirstName());
                usuarioSharedPref.setLastName((ultimoNombre.getText() != null) ? ultimoNombre.getText().toString() : usuarioActual.getLastName());
                usuarioSharedPref.setEmail((correo.getText() != null) ? correo.getText().toString() : usuarioActual.getEmail());

                if (response.isSuccessful() && response.body() != null && !StringUtils.isBlank(response.body().get("fotoURL"))) {
                    session.updatePhoto(null);
                    usuarioSharedPref.setPhoto(response.body().get("fotoURL"));
                    session.createLoginSession(usuarioSharedPref);
                    session.updatePhoto(response.body().get("fotoURL"));
                    imageView.setImageResource(0);
                    Glide.with(context)
                            .load(usuarioSharedPref.getPhoto())
                            .error(R.drawable.avatardefault)
                            .into(imageView);
                } else {
                    imageView.setImageResource(0);
                    session.updatePhoto(null);
                    usuarioSharedPref.setPhoto(null);
                    session.createLoginSession(usuarioSharedPref);
                    Glide.with(context)
                            .load(usuarioSharedPref.getPhoto())
                            .error(R.drawable.avatardefault)
                            .into(imageView);
                }


                if (passOriginal.getText() != null) {
                    passOriginal.getText().clear();
                }
                if (passNueva.getText() != null) {
                    passNueva.getText().clear();
                }
                if (passRepetida.getText() != null) {
                    passRepetida.getText().clear();
                }

                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NotNull Call<Map<String, String>> call, @NotNull Throwable t) {
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Internet not available, check your internet connection and try again.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", (dialog, which) -> ((Activity) context).finishAffinity()).show();
            }
        });
    }

    private MultipartBody.Part prepareFilePart(Uri fileUri) {
        File file = FileUtils.getFile(context, fileUri);

        if (file != null && file.exists()) {

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse(context.getContentResolver().getType(fileUri)), file);

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        } else {
            Toast.makeText(context, "Sorry, we couldn't get the photo.", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            uri = (Uri) data.getParcelableArrayListExtra(INTENT_PATH).iterator().next();

            Glide.with(context)
                    .load(uri)
                    .error(R.drawable.ic_products_missing_image)
                    .into(imageView);

            saveButton.setVisibility((verificarCampos()) ? View.VISIBLE : View.GONE);

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(context, "You haven't picked Image", Toast.LENGTH_LONG).show();
            saveButton.setVisibility((verificarCampos()) ? View.VISIBLE : View.GONE);
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}