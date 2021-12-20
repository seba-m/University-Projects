package com.seba.inventariado.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.seba.inventariado.R;
import com.seba.inventariado.activity.EditProductActivity;
import com.seba.inventariado.model.Producto;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private final List<Producto> productos;
    private final Context context;
    private final SessionManager session;
    private final RecyclerView recycler;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgFoto;
        private final TextView txtNombreProducto;
        private final TextView txtMensajeAlarma;
        private final Button btnEditar;
        private final Button btnEliminar;

        public ViewHolder(View view) {
            super(view);
            imgFoto = view.findViewById(R.id.card_alarm_image);
            txtNombreProducto = view.findViewById(R.id.card_alarm_text_name);
            txtMensajeAlarma = view.findViewById(R.id.card_alarm_text_message);
            btnEditar = view.findViewById(R.id.alarm_boton_editar);
            btnEliminar = view.findViewById(R.id.alarm_boton_borrar);
        }

        public ImageView getImgFoto() {
            return imgFoto;
        }

        public TextView getTxtNombreProducto() {
            return txtNombreProducto;
        }

        public TextView gettxtMensajeAlarma() {
            return txtMensajeAlarma;
        }

        public Button getBtnEditar() {
            return btnEditar;
        }

        public Button getBtnEliminar() {
            return btnEliminar;
        }
    }

    public AlarmListAdapter(Context contexto, RecyclerView view, List<Producto> productos) {
        this.productos = productos;
        this.context = contexto;
        this.recycler = view;
        session = new SessionManager(context);
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarma, viewGroup, false);
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
                        .error(R.drawable.ic_products_missing_image)
                        .into(viewHolder.getImgFoto());
            }

            String nombre = producto.getNombre();
            if (StringUtils.isBlank(nombre)) {
                nombre = "Invalid Product Name";
            }
            viewHolder.getTxtNombreProducto().setText(nombre);

            String mensaje = "";
            switch (producto.getMinLvlOption()) {
                case "1":
                    mensaje = context.getResources().getString(R.string.alarm_message_1, producto.getMinLvl());
                    break;
                case "2":
                    mensaje = context.getResources().getString(R.string.alarm_message_2, producto.getMinLvl());
                    break;
                case "3":
                    mensaje = context.getResources().getString(R.string.alarm_message_3, producto.getMinLvl());
                    break;
                case "4":
                    mensaje = context.getResources().getString(R.string.alarm_message_4, producto.getMinLvl());
                    break;
                default:
                    mensaje = context.getResources().getString(R.string.alarm_message_0);
                    break;
            }

            viewHolder.gettxtMensajeAlarma().setText(mensaje);

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
                                productos.remove(position);
                                recycler.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, productos.size());
                                notifyDataSetChanged();
                            }
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
