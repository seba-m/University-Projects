package com.seba.inventariado.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.seba.inventariado.R;
import com.seba.inventariado.activity.EditProductActivity;
import com.seba.inventariado.activity.ProductListFragment;
import com.seba.inventariado.model.Producto;
import com.seba.inventariado.model.Tag;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> implements Filterable {

    private final List<Producto> productos;
    private final List<Producto> productosFull;
    private final Context context;
    private final SessionManager session;
    private final RecyclerView recycler;
    private final ProductListFragment fragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgFoto;
        private final TextView txtNombreProducto;
        private final TextView txtPrecioProducto;
        private final TextView txtCantidadProducto;
        private final ChipGroup chipsGroup;
        private final Button btnEditar;
        private final Button btnEliminar;

        public ViewHolder(View view) {
            super(view);
            imgFoto = view.findViewById(R.id.card_product_image);
            txtNombreProducto = view.findViewById(R.id.card_product_text_name);
            txtPrecioProducto = view.findViewById(R.id.card_product_text_price);
            txtCantidadProducto = view.findViewById(R.id.card_product_text_quantity);
            chipsGroup = view.findViewById(R.id.card_product_group_tag);
            btnEditar = view.findViewById(R.id.producto_boton_editar);
            btnEliminar = view.findViewById(R.id.producto_boton_borrar);
        }

        public ImageView getImgFoto() {
            return imgFoto;
        }

        public TextView getTxtNombreProducto() {
            return txtNombreProducto;
        }

        public TextView getTxtPrecioProducto() {
            return txtPrecioProducto;
        }

        public TextView getTxtCantidadProducto() {
            return txtCantidadProducto;
        }

        public ChipGroup getChipsGroup() {
            return chipsGroup;
        }

        public Button getBtnEditar() {
            return btnEditar;
        }

        public Button getBtnEliminar() {
            return btnEliminar;
        }
    }

    public ProductListAdapter(Context contexto, RecyclerView view, List<Producto> productos, ProductListFragment fragment) {
        this.productos = productos;
        this.productosFull = productos != null ? new ArrayList<>(productos) : Collections.emptyList();
        this.context = contexto;
        this.recycler = view;
        this.fragment = fragment;
        session = new SessionManager(context);
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.producto, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        Producto producto = productos.get(position);

        if (producto != null) {

            if (producto.getFotos() != null && !producto.getFotos().isEmpty()) {

                String urlPhoto = context.getResources().getString(R.string.productPhotosURL, session.getUserDetails().getUserID(), producto.getId(), producto.getFotos().iterator().next().getNombreImagen());

                Glide.with(context).clear(viewHolder.getImgFoto());
                Glide.with(context)
                        .load(urlPhoto)
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_products_missing_image)
                        .into(viewHolder.getImgFoto());
            } else {
                Glide.with(context).clear(viewHolder.getImgFoto());
                Glide.with(context)
                        .load(R.drawable.ic_products_missing_image)
                        .into(viewHolder.getImgFoto());
            }

            String nombre = producto.getNombre();
            String cantidad = producto.getCantidad();
            String precio = producto.getPrecio();

            if (StringUtils.isBlank(nombre)) {
                nombre = "Invalid Product Name";
            }

            if (!StringUtils.isBlank(nombre) && nombre.length() > 49) {
                nombre = nombre.substring(0, 49) + "…";
            }

            try {
                long quantity = Long.parseLong(cantidad);

                cantidad = cantidad.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".");

                if (quantity == 1 || quantity == 0) {
                    cantidad = cantidad + " unit";
                } else {
                    cantidad = cantidad + " units";
                }

            } catch (Exception e) {
                cantidad = "Invalid Quantity";
            }

            try {
                Long.parseLong(precio);
                precio = precio.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".");
            } catch (Exception e) {
                precio = "Invalid Price";
            }

            viewHolder.getTxtNombreProducto().setText(nombre);
            viewHolder.getTxtPrecioProducto().setText(context.getResources().getString(R.string.product_money_card, precio));
            viewHolder.getTxtCantidadProducto().setText(cantidad);

            viewHolder.getBtnEditar().setOnClickListener(OnSingleClickListener.wrap((v) -> {

                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("productID", producto.getId().toString());
                context.startActivity(intent);
            }));

            viewHolder.getBtnEliminar().setOnClickListener(OnSingleClickListener.wrap((click) -> {
                Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete);

                Button btnCancelar = dialog.findViewById(R.id.boton_cancelar_delete);
                btnCancelar.setOnClickListener((v) -> dialog.dismiss());

                Button btnDelete = dialog.findViewById(R.id.boton_eliminar_delete);
                btnDelete.setOnClickListener(OnSingleClickListener.wrap((v) -> {
                    ServerService productToDelete = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
                    Call<Void> LoginResponseCall = productToDelete.deleteProduct(producto.getId());

                    LoginResponseCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                            if (response.isSuccessful()) {
                                productosFull.remove(producto);
                                productos.remove(position);
                                recycler.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, productos.size());
                                notifyDataSetChanged();
                            }
                            fragment.getProducts();
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

            //chips (or tags) of product
            if (producto.getTags() != null && !producto.getTags().isEmpty()) {
                viewHolder.getChipsGroup().removeAllViews();
                for (Tag tag : producto.getTags()) {
                    Chip chipTag = new Chip(context);
                    chipTag.setEnabled(false);
                    chipTag.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    chipTag.setText((tag.getNombre() != null) ? ((tag.getNombre().length() > 10) ? tag.getNombre().substring(0, 10) + "..." : tag.getNombre()) : context.getResources().getString(R.string.invalid_tag));
                    viewHolder.getChipsGroup().addView(chipTag);
                }
            } else {
                viewHolder.getChipsGroup().removeAllViews();
                Chip chipTag = new Chip(context);
                chipTag.setEnabled(false);
                chipTag.setText(context.getResources().getString(R.string.no_tag));
                chipTag.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                viewHolder.getChipsGroup().addView(chipTag);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return productos.size();
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private final Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Producto> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList = productosFull;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Producto item : productosFull) {
                    if (item.containsString(filterPattern.toLowerCase())) {
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
            if (productos != null) {
                productos.clear();
                productos.addAll((List<Producto>) results.values);
                notifyDataSetChanged();
            }
        }
    };
}
