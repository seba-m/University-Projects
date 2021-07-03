package com.seba.inventariado.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.seba.inventariado.R;
import com.seba.inventariado.model.Producto;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecentItemAdapter extends RecyclerView.Adapter<RecentItemAdapter.ViewHolder> {

    private final List<Producto> productos;
    private final Context context;
    private final SessionManager session;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgFoto;
        private final TextView txtNombreProducto;
        private final TextView txtCantidadProducto;

        public ViewHolder(View view) {
            super(view);
            imgFoto = view.findViewById(R.id.card_recent_product_image);
            txtNombreProducto = view.findViewById(R.id.card_recent_product_text_name);
            txtCantidadProducto = view.findViewById(R.id.card_recent_product_text_quantity);
        }

        public ImageView getImgFoto() {
            return imgFoto;
        }

        public TextView getTxtNombreProducto() {
            return txtNombreProducto;
        }

        public TextView getTxtCantidadProducto() {
            return txtCantidadProducto;
        }

    }

    public RecentItemAdapter(Context contexto, List<Producto> productos) {
        this.productos = productos;
        this.context = contexto;
        session = new SessionManager(context);
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_producto, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        Producto producto = productos.get(position);

        if (producto != null) {

            if (producto.getFotos() != null && !producto.getFotos().isEmpty()) {

                String urlPhoto = context.getResources().getString(R.string.productPhotosURL, session.getUserDetails().getUserID(), producto.getId(), producto.getFotos().iterator().next().getNombreImagen());

                Glide.with(context)
                        .load(urlPhoto)
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .error(R.drawable.ic_products_missing_image)
                        .into(viewHolder.getImgFoto());
            }

            String nombre = producto.getNombre();
            String cantidad = producto.getCantidad();

            if (!StringUtils.isBlank(nombre) && nombre.length() > 10) {
                nombre = nombre.substring(0, 10) + "…";
            } else if (StringUtils.isBlank(nombre)) {
                nombre = "N/A";
            }

            try {
                long quantity = Long.parseLong(cantidad);

                cantidad = cantidad.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ".");

                if (quantity == 1 || quantity == 0) {
                    cantidad = cantidad + " unit";
                } else {
                    cantidad = cantidad + " units";
                }

                if (cantidad.length() > 15) {
                    cantidad = cantidad.substring(0, 15) + "…";
                }
            } catch (Exception e) {
                cantidad = "0 unit";
            }

            viewHolder.getTxtNombreProducto().setText(nombre);
            viewHolder.getTxtCantidadProducto().setText(cantidad);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if (productos != null)
            return productos.size();
        else
            return 0;
    }
}
