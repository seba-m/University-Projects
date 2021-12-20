package com.seba.inventariado.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.seba.inventariado.R;
import com.seba.inventariado.model.DashboardDto;
import com.seba.inventariado.model.Producto;
import com.seba.inventariado.utils.ApiClient;
import com.seba.inventariado.utils.RecentItemAdapter;
import com.seba.inventariado.utils.ServerService;
import com.seba.inventariado.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private DashboardDto dashboardValues;
    private SessionManager session;
    private SwipeRefreshLayout swipeContainer;
    private ViewSwitcher switcher;
    private List<Producto> listaProductosRecientes = new ArrayList<>();
    private Context context;
    private LinearLayoutManager layoutManager;
    private RecentItemAdapter adaptador;
    private RecyclerView recListItems;

    TextView itemAmmount, tagsAmmount, stockAmmount, totalAmmount;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        session = new SessionManager(context);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        getActivity().setTitle("Dashboard");

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        itemAmmount = view.findViewById(R.id.text_total_item_ammount);
        tagsAmmount = view.findViewById(R.id.text_total_tags_ammount);
        stockAmmount = view.findViewById(R.id.text_stock_ammount);
        totalAmmount = view.findViewById(R.id.text_total_ammount);
        switcher = view.findViewById(R.id.recent_items_switcher);
        recListItems = view.findViewById(R.id.list_productos_recientes);

        swipeContainer = view.findViewById(R.id.swipe_container_dashboard);
        swipeContainer.setOnRefreshListener(() -> {
            getDashboardValues();
            swipeContainer.setRefreshing(false);
        });

        adaptador = new RecentItemAdapter(context, listaProductosRecientes);
        recListItems.setLayoutManager(layoutManager);
        recListItems.setAdapter(adaptador);
        getDashboardValues();
        return view;
    }

    private void getDashboardValues() {

        ServerService dashboardValuesServer = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
        Call<DashboardDto> LoginResponseCall = dashboardValuesServer.dashboard();

        LoginResponseCall.enqueue(new Callback<DashboardDto>() {
            @Override
            public void onResponse(@NotNull Call<DashboardDto> call, @NotNull Response<DashboardDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dashboardValues = response.body();

                    itemAmmount.setText(dashboardValues.getItems());
                    tagsAmmount.setText(dashboardValues.getTags());
                    stockAmmount.setText(dashboardValues.getTotalQuantity());
                    totalAmmount.setText(context.getString(R.string.money_format, dashboardValues.getTotalValue()));

                    if (dashboardValues.getRecentEditProducts() != null && !dashboardValues.getRecentEditProducts().isEmpty()) {
                        switcher.setDisplayedChild(1);
                        listaProductosRecientes = dashboardValues.getRecentEditProducts();
                        adaptador = new RecentItemAdapter(context, listaProductosRecientes);
                        recListItems.setAdapter(adaptador);
                    } else {
                        switcher.setDisplayedChild(0);
                    }
                } else {
                    itemAmmount.setText("0");
                    tagsAmmount.setText("0");
                    stockAmmount.setText("0");
                    totalAmmount.setText("$ 0");

                    switcher.setDisplayedChild(0);
                }
            }

            @Override
            public void onFailure(@NotNull Call<DashboardDto> call, @NotNull Throwable t) {
                itemAmmount.setText("0");
                tagsAmmount.setText("0");
                stockAmmount.setText("0");
                totalAmmount.setText("$ 0");
                switcher.setDisplayedChild(0);

                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Internet not available, check your internet connection and try again.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", (dialog, which) -> ((Activity) context).finishAffinity()).show();
            }
        });
    }

}