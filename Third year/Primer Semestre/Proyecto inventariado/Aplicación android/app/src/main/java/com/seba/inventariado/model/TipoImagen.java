package com.seba.inventariado.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Pair;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class TipoImagen implements Serializable {
    private Uri tipoUri = null;
    private String tipoUrl = null;

    public TipoImagen(String tipoUrl) {
        this.tipoUrl = tipoUrl;
    }

    public TipoImagen(Uri tipoUri) {
        this.tipoUri = tipoUri;
    }

    public Uri getTipoUri() {
        return tipoUri;
    }

    public String getTipoUrl() {
        return tipoUrl;
    }

    public Pair<String, Long> getUriData(Context contexto) {
        try (Cursor returnCursor = contexto.getContentResolver().query(getTipoUri(), null, null, null, null)) {

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();

            return new Pair<>(returnCursor.getString(nameIndex), returnCursor.getLong(sizeIndex));
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoImagen that = (TipoImagen) o;
        return Objects.equals(tipoUri, that.tipoUri) && Objects.equals(tipoUrl, that.tipoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoUri, tipoUrl);
    }

    @NotNull
    @Override
    public String toString() {
        return "TipoImagen{" +
                "tipoUri=" + tipoUri +
                ", tipoUrl='" + tipoUrl + '\'' +
                '}';
    }
}
