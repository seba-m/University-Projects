package com.seba.inventariado.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.seba.inventariado.R;
import com.seba.inventariado.activity.TagFragment;
import com.seba.inventariado.model.Tag;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> implements Filterable {

    private final List<Tag> listTags;
    private final List<Tag> listTagsFull;
    private final Context context;
    private final SessionManager session;
    private final RecyclerView recycler;
    private final ViewSwitcher switcher;
    private final boolean mostrarBotones;
    private final TagFragment fragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton btnEditar;
        private final ImageButton btnEliminar;
        private final Button btnTag;

        public ViewHolder(View view) {
            super(view);
            btnTag = view.findViewById(R.id.card_tag_text_name);
            btnEditar = view.findViewById(R.id.tag_boton_editar);
            btnEliminar = view.findViewById(R.id.tag_boton_borrar);
        }

        public Button getBtnTag() {
            return btnTag;
        }

        public ImageButton getBtnEditar() {
            return btnEditar;
        }

        public ImageButton getBtnEliminar() {
            return btnEliminar;
        }
    }

    public TagAdapter(Context contexto, List<Tag> tagList, RecyclerView recycler, ViewSwitcher switcher, boolean mostrarBotones, TagFragment fragment) {
        this.listTags = tagList;
        this.mostrarBotones = mostrarBotones;
        this.fragment = fragment;
        this.listTagsFull = listTags != null ? new ArrayList<>(listTags) : Collections.emptyList();
        this.context = contexto;
        this.recycler = recycler;
        this.switcher = switcher;
        session = new SessionManager(context);
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tag, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        Tag tag = listTags.get(position);

        viewHolder.getBtnTag().setText(tag.getNombre());
        viewHolder.getBtnTag().setOnClickListener(v -> {
            //TODO: mostrar los productos que tienen este tag
        });

        viewHolder.getBtnEditar().setVisibility(!mostrarBotones ? View.GONE : View.VISIBLE);
        viewHolder.getBtnEditar().setOnClickListener(click -> {
            Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_change_tag);

            EditText tagName = dialog.findViewById(R.id.tagName);
            tagName.setHint(tag.getNombre());

            Button btnCancelar = dialog.findViewById(R.id.cancel_tag);
            btnCancelar.setOnClickListener((v) -> dialog.dismiss());

            Button btnEditar = dialog.findViewById(R.id.add_tag);
            btnEditar.setOnClickListener(OnSingleClickListener.wrap((v) -> {
                if (!StringUtils.isBlank(tagName.getText().toString())) {

                    ServerService tagToAdd = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
                    tag.setNombre(tagName.getText().toString().replaceAll("[^\\da-zA-Z ]", ""));
                    Call<Tag> tagResponseCall = tagToAdd.editTag(tag.getId().toString(), tag);

                    tagResponseCall.enqueue(new Callback<Tag>() {
                        @Override
                        public void onResponse(@NotNull Call<Tag> call, @NotNull Response<Tag> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                listTags.set(position, response.body());
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<Tag> call, @NotNull Throwable t) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Error")
                                    .setMessage("Internet not available, check your internet connection and try again.")
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setNeutralButton("OK", (dialog, which) -> ((Activity) context).finishAffinity()).show();
                        }
                    });

                    dialog.dismiss();
                    return;
                }

                dialog.dismiss();
            }));

            dialog.setOnCancelListener((v) -> dialog.dismiss());
            dialog.show();
        });

        viewHolder.getBtnEliminar().setVisibility(!mostrarBotones ? View.GONE : View.VISIBLE);
        viewHolder.getBtnEliminar().setOnClickListener(OnSingleClickListener.wrap((click) -> {
            Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_delete);

            Button btnCancelar = dialog.findViewById(R.id.boton_cancelar_delete);
            btnCancelar.setOnClickListener((v) -> dialog.dismiss());

            Button btnDelete = dialog.findViewById(R.id.boton_eliminar_delete);
            btnDelete.setOnClickListener(OnSingleClickListener.wrap((v) -> {
                ServerService productToDelete = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
                Call<Void> LoginResponseCall = productToDelete.deleteTag(tag.getId());

                LoginResponseCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            listTagsFull.remove(tag);
                            listTags.remove(position);
                            recycler.removeViewAt(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, listTags.size());
                            notifyDataSetChanged();
                            if (listTags.size() == 0) {
                                switcher.setDisplayedChild(0);
                            }
                        }
                        fragment.getTags();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        dialog.dismiss();
                        new AlertDialog.Builder(context)
                                .setTitle("Error")
                                .setMessage("Internet not available, check your internet connection and try again.")
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setNeutralButton("OK", (dialog, which) -> ((Activity) context).finishAffinity()).show();
                    }
                });
            }));

            dialog.setOnCancelListener((v) -> dialog.dismiss());
            dialog.show();
        }));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (listTags != null) ? listTags.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private final Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Tag> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList = listTagsFull;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Tag item : listTagsFull) {
                    Log.e("nombre tag", item.getNombre());
                    if (item.getNombre() != null && item.getNombre().toLowerCase().contains(filterPattern.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (listTags != null) {
                listTags.clear();
                listTags.addAll((List<Tag>) results.values);
                notifyDataSetChanged();
            }
        }
    };
}
