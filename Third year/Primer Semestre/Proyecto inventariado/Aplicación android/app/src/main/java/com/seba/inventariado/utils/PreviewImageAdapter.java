package com.seba.inventariado.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.MimeType;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.seba.inventariado.R;
import com.seba.inventariado.activity.PreviewImageActivity;
import com.seba.inventariado.model.TipoImagen;

import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PreviewImageAdapter extends RecyclerView.Adapter<PreviewImageAdapter.ViewHolder> {

    private final List<TipoImagen> listImages;
    private final Context context;
    private final ImageView viewPrevisualizar;
    private final RecyclerView recycler;
    private final ViewSwitcher switcher;
    private HashMap<String, Long> nombreImagenesServidor;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgFoto;
        private final LinearLayout btnPrevisualizarImagen;
        private final RelativeLayout btnEliminarImagen;
        private final ImageView btnAgregarFoto;

        public ViewHolder(View view) {
            super(view);
            btnPrevisualizarImagen = view.findViewById(R.id.img_prv_image);
            btnEliminarImagen = view.findViewById(R.id.img_prv_trash);
            imgFoto = view.findViewById(R.id.img_preview_miniatura);
            btnAgregarFoto = view.findViewById(R.id.add_images_new_product);
        }

        public ImageView getBtnAgregarFoto() {
            return btnAgregarFoto;
        }

        public ImageView getImgFoto() {
            return imgFoto;
        }

        public LinearLayout getBtnPrevisualizarImagen() {
            return btnPrevisualizarImagen;
        }

        public RelativeLayout getBtnEliminarImagen() {
            return btnEliminarImagen;
        }
    }

    public PreviewImageAdapter(Context contexto, List<TipoImagen> listImages, ImageView viewPrevisualizar, RecyclerView recycler, ViewSwitcher switcher, HashMap<String, Long> nombreImagenesServidor) {
        this.nombreImagenesServidor = nombreImagenesServidor;
        this.viewPrevisualizar = viewPrevisualizar;
        this.listImages = listImages;
        this.context = contexto;
        this.recycler = recycler;
        this.switcher = switcher;
    }

    public PreviewImageAdapter(Context contexto, List<TipoImagen> listImages, ImageView viewPrevisualizar, RecyclerView recycler, ViewSwitcher switcher) {
        this.viewPrevisualizar = viewPrevisualizar;
        this.listImages = listImages;
        this.context = contexto;
        this.recycler = recycler;
        this.switcher = switcher;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view;
        if (viewType == R.layout.mini_image_preview) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_image_preview, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.boton_agregar_imagen, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        try {
            if (position == listImages.size()) {
                viewHolder.getBtnAgregarFoto().setOnClickListener((View.OnClickListener) view ->
                        FishBun.with((Activity) context)
                                .setImageAdapter(new GlideAdapter())
                                .setMaxCount(10 - listImages.size())
                                .setPickerSpanCount(4)
                                .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
                                .setActionBarTitleColor(Color.parseColor("#000000"))
                                .setAlbumSpanCount(1, 2)
                                .setButtonInAlbumActivity(true)
                                //.hasCameraInPickerPage(true)
                                .setCamera(true)
                                .exceptMimeType(Arrays.asList(MimeType.GIF, MimeType.WEBP, MimeType.BMP))
                                .setReachLimitAutomaticClose(false)
                                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable((Activity) context, R.drawable.ic_arrow_back_black_24dp))
                                .setDoneButtonDrawable(ContextCompat.getDrawable((Activity) context, R.drawable.ic_check_black_24dp))
                                .setAllViewTitle("All of your photos")
                                .setActionBarTitle("Select images to upload.")
                                .textOnImagesSelectionLimitReached("You can't select any more.")
                                .textOnNothingSelected("I need a photo!")
                                .startAlbum());
            } else {
                TipoImagen imagen = listImages.get(position);

                if (imagen.getTipoUri() != null) {
                    Uri uri = imagen.getTipoUri();
                    Pair<String, Long> uriData = imagen.getUriData(context);

                    Glide.with(context)
                            .load(uri)
                            .error(R.drawable.ic_products_missing_image)
                            .into(viewHolder.getImgFoto());

                    viewHolder.getBtnPrevisualizarImagen().setOnClickListener(v -> {
                        Glide.with(context)
                                .load(uri)
                                //.fitCenter()
                                .centerCrop()
                                .error(R.drawable.ic_products_missing_image)
                                .into(viewPrevisualizar);

                        viewPrevisualizar.setOnClickListener(v1 -> {
                            Intent intent = new Intent(context, PreviewImageActivity.class);
                            intent.putExtra("imageURI", uri);
                            context.startActivity(intent);
                        });
                    });
                    //viewHolder.getBtnPrevisualizarImagen().setOnLongClickListener(); //TODO: agregar opcion para arrastrar las imagenes

                    viewHolder.getBtnEliminarImagen().setOnClickListener(v -> {
                        viewPrevisualizar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_products_missing_image));
                        viewPrevisualizar.setOnClickListener(null);

                        listImages.remove(position);

                        if (nombreImagenesServidor != null) {
                            nombreImagenesServidor.remove(uriData.first);
                        }

                        if (listImages.size() == 0) {
                            switcher.setDisplayedChild(0);
                        }

                        recycler.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listImages.size());
                        notifyDataSetChanged();
                    });
                }

                if (imagen.getTipoUrl() != null) {
                    String url = imagen.getTipoUrl();

                    Glide.with(context)
                            .load(url)
                            .error(R.drawable.ic_products_missing_image)
                            .into(viewHolder.getImgFoto());

                    viewHolder.getBtnPrevisualizarImagen().setOnClickListener(v -> {
                        Glide.with(context)
                                .load(url)
                                //.fitCenter()
                                .centerCrop()
                                .error(R.drawable.ic_products_missing_image)
                                .into(viewPrevisualizar);

                        viewPrevisualizar.setOnClickListener(v1 -> {
                            Intent intent = new Intent(context, PreviewImageActivity.class);
                            intent.putExtra("imageURL", url);
                            context.startActivity(intent);
                        });
                    });
                    //viewHolder.getBtnPrevisualizarImagen().setOnLongClickListener(); //TODO: agregar opcion para arrastrar las imagenes

                    viewHolder.getBtnEliminarImagen().setOnClickListener(v -> {
                        viewPrevisualizar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_products_missing_image));
                        viewPrevisualizar.setOnClickListener(null);

                        listImages.remove(position);

                        nombreImagenesServidor.remove(FilenameUtils.getName(url));

                        if (listImages.size() == 0) {
                            switcher.setDisplayedChild(0);
                        }

                        recycler.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listImages.size());
                        notifyDataSetChanged();
                    });
                }

            }
        } catch (Exception e) {
            Log.e("", "parece que fallo aca " + e.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == listImages.size()) ? R.layout.boton_agregar_imagen : R.layout.mini_image_preview;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if (listImages.size() < 10)
            return listImages.size() + 1;
        else
            return listImages.size();
    }
}
