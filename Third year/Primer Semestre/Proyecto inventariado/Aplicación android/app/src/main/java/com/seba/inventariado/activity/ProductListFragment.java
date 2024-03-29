package com.seba.inventariado.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.seba.inventariado.R;
import com.seba.inventariado.model.Producto;
import com.seba.inventariado.utils.ApiClient;
import com.seba.inventariado.utils.Heapsort;
import com.seba.inventariado.utils.ICompararProductos;
import com.seba.inventariado.utils.ProductListAdapter;
import com.seba.inventariado.utils.ServerService;
import com.seba.inventariado.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment {

    private Context context;
    private List<Producto> listaProductos;
    private SessionManager session;
    private ViewSwitcher switcher;
    private RecyclerView recListItems;
    private ProductListAdapter adaptador;
    private LinearLayoutManager layoutManager;

    private Pair<Integer, Integer> sortOrder;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        session = new SessionManager(context);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        getActivity().setTitle("Items");

        switcher = view.findViewById(R.id.simpleViewSwitcher);
        recListItems = view.findViewById(R.id.list_productos);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_plus);
        fab.setOnClickListener(view1 -> startActivity(new Intent(context, CreateProductActivity.class)));

        SwipeRefreshLayout swipeContainer = view.findViewById(R.id.swipe_container_products);
        swipeContainer.setOnRefreshListener(() -> {
            getProducts();
            if (switcher.getDisplayedChild() == 1) {
                adaptador = new ProductListAdapter(context, recListItems, listaProductos, ProductListFragment.this);
                adaptador.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        checkIfEmpty();
                    }

                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        checkIfEmpty();
                    }

                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);
                        checkIfEmpty();
                    }

                    private void checkIfEmpty() {
                        switcher.setDisplayedChild(adaptador.getItemCount() > 0 ? 1 : 0);
                    }
                });
                recListItems.setAdapter(adaptador);
            }
            swipeContainer.setRefreshing(false);
        });

        adaptador = new ProductListAdapter(context, recListItems, listaProductos, ProductListFragment.this);
        adaptador.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIfEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkIfEmpty();
            }

            private void checkIfEmpty() {
                switcher.setDisplayedChild(adaptador.getItemCount() > 0 ? 1 : 0);
            }
        });
        recListItems.setLayoutManager(layoutManager);
        recListItems.setAdapter(adaptador);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.empty_tall_divider_small));
        recListItems.addItemDecoration(itemDecorator);

        TextView sort = view.findViewById(R.id.sort_items_list_products);
        sort.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.bottom_sort_items);

            ImageView sortByName = dialog.findViewById(R.id.sort_by_name);
            ImageView sortByQuantity = dialog.findViewById(R.id.sort_by_quantity);
            ImageView sortByUpdate = dialog.findViewById(R.id.sort_by_update);
            ImageView sortByValue = dialog.findViewById(R.id.sort_by_value);

            sortByName.setOnClickListener(a -> {
                if (sortOrder.first == 0 && sortOrder.second == 0) {
                    ordenarProductos(new ICompararProductos.ComparadorNombreZ_A());
                    sortByName.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                    sort.setText(R.string.sort_by_name);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                    sortOrder = new Pair<>(0, 1);
                } else if (sortOrder.first == 0 && sortOrder.second == 1) {
                    ordenarProductos(new ICompararProductos.ComparadorNombreA_Z());
                    sortByName.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_menos_mas));
                    sort.setText(R.string.sort_by_name);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_menos_mas, 0, 0, 0);
                    sortOrder = new Pair<>(0, 0);
                } else {
                    ordenarProductos(new ICompararProductos.ComparadorNombreZ_A());
                    sortByName.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                    sort.setText(R.string.sort_by_name);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                    sortOrder = new Pair<>(0, 0);
                }
            });

            sortByUpdate.setOnClickListener(c -> {
                if (sortOrder.first == 1 && sortOrder.second == 0) {
                    ordenarProductos(new ICompararProductos.ComparadorFechaReciente());
                    sortByUpdate.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                    sort.setText(R.string.sort_by_update);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                    sortOrder = new Pair<>(1, 1);
                } else if (sortOrder.first == 1 && sortOrder.second == 1) {
                    ordenarProductos(new ICompararProductos.ComparadorFechaAntigua());
                    sortByUpdate.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_menos_mas));
                    sort.setText(R.string.sort_by_update);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_menos_mas, 0, 0, 0);
                    sortOrder = new Pair<>(1, 0);
                } else {
                    ordenarProductos(new ICompararProductos.ComparadorFechaReciente());
                    sortByUpdate.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                    sort.setText(R.string.sort_by_update);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                    sortOrder = new Pair<>(1, 0);
                }
            });

            sortByQuantity.setOnClickListener(b -> {
                if (sortOrder.first == 2 && sortOrder.second == 0) {
                    ordenarProductos(new ICompararProductos.ComparadorCantidadMayorMenor());
                    sortByQuantity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                    sort.setText(R.string.sort_by_quantity);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                    sortOrder = new Pair<>(2, 1);
                } else if (sortOrder.first == 2 && sortOrder.second == 1) {
                    ordenarProductos(new ICompararProductos.ComparadorCantidadMenorMayor());
                    sortByQuantity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_menos_mas));
                    sort.setText(R.string.sort_by_quantity);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_menos_mas, 0, 0, 0);
                    sortOrder = new Pair<>(2, 0);
                } else {
                    ordenarProductos(new ICompararProductos.ComparadorCantidadMayorMenor());
                    sortByQuantity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                    sort.setText(R.string.sort_by_quantity);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                    sortOrder = new Pair<>(2, 0);
                }

            });

            sortByValue.setOnClickListener(d -> {
                if (sortOrder.first == 3 && sortOrder.second == 0) {
                    ordenarProductos(new ICompararProductos.ComparadorValorTotalMayorMenor());
                    sortByValue.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                    sort.setText(R.string.sort_by_value);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                    sortOrder = new Pair<>(3, 1);
                } else if (sortOrder.first == 3 && sortOrder.second == 1) {
                    ordenarProductos(new ICompararProductos.ComparadorValorTotalMenorMayor());
                    sortByValue.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_menos_mas));
                    sort.setText(R.string.sort_by_value);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_menos_mas, 0, 0, 0);
                    sortOrder = new Pair<>(3, 0);
                } else {
                    ordenarProductos(new ICompararProductos.ComparadorValorTotalMayorMenor());
                    sortByValue.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                    sort.setText(R.string.sort_by_value);
                    sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                    sortOrder = new Pair<>(3, 0);
                }
            });

            TextView sortByNameDefault = dialog.findViewById(R.id.sort_by_name_default);
            TextView sortByUpdateDefault = dialog.findViewById(R.id.sort_by_update_default);
            TextView sortByQuantityDefault = dialog.findViewById(R.id.sort_by_quantity_default);
            TextView sortByValueDefault = dialog.findViewById(R.id.sort_by_value_default);
            sortByNameDefault.setOnClickListener(e -> {
                ordenarProductos(new ICompararProductos.ComparadorNombreA_Z());
                sortByName.setVisibility(View.VISIBLE);
                sortByName.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_menos_mas));
                sortByQuantity.setVisibility(View.GONE);
                sortByUpdate.setVisibility(View.GONE);
                sortByValue.setVisibility(View.GONE);
                sort.setText(R.string.sort_by_name);
                sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_menos_mas, 0, 0, 0);
                sortOrder = new Pair<>(0, 1);
            });

            sortByUpdateDefault.setOnClickListener(f -> {
                ordenarProductos(new ICompararProductos.ComparadorFechaReciente());
                sortByUpdate.setVisibility(View.VISIBLE);
                sortByUpdate.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                sortByQuantity.setVisibility(View.GONE);
                sortByName.setVisibility(View.GONE);
                sortByValue.setVisibility(View.GONE);
                sort.setText(R.string.sort_by_update);
                sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_mas_menos, 0, 0, 0);
                sortOrder = new Pair<>(1, 1);
            });

            sortByQuantityDefault.setOnClickListener(g -> {
                ordenarProductos(new ICompararProductos.ComparadorCantidadMenorMayor());
                sortByQuantity.setVisibility(View.VISIBLE);
                sortByQuantity.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_menos_mas));
                sortByName.setVisibility(View.GONE);
                sortByUpdate.setVisibility(View.GONE);
                sortByValue.setVisibility(View.GONE);
                sort.setText(R.string.sort_by_quantity);
                sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_menos_mas, 0, 0, 0);
                sortOrder = new Pair<>(2, 0);
            });

            sortByValueDefault.setOnClickListener(h -> {
                ordenarProductos(new ICompararProductos.ComparadorValorTotalMenorMayor());
                sortByValue.setVisibility(View.VISIBLE);
                sortByValue.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_menos_mas));
                sortByQuantity.setVisibility(View.GONE);
                sortByUpdate.setVisibility(View.GONE);
                sortByName.setVisibility(View.GONE);
                sort.setText(R.string.sort_by_value);
                sort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sort_menos_mas, 0, 0, 0);
                sortOrder = new Pair<>(3, 0);
            });

            ImageView closeDialog = dialog.findViewById(R.id.iv_close);
            closeDialog.setOnClickListener(i -> {
                dialog.dismiss();
            });

            dialog.show();
        });

        getProducts();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptador.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void ordenarProductos(Comparator<? super Producto> comp) {
        adaptador = new ProductListAdapter(context, recListItems, Heapsort.heapSort(listaProductos, comp), ProductListFragment.this);
        recListItems.setAdapter(adaptador);
    }

    public void getProducts() {

        ServerService dashboardValuesServer = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
        Call<List<Producto>> LoginResponseCall = dashboardValuesServer.allProducts();
        LoginResponseCall.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(@NotNull Call<List<Producto>> call, @NotNull Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    switcher.setDisplayedChild(1);
                    listaProductos = response.body();
                    adaptador = new ProductListAdapter(context, recListItems, Heapsort.heapSort(listaProductos, new ICompararProductos.ComparadorNombreA_Z()), ProductListFragment.this);
                    adaptador.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            checkIfEmpty();
                        }

                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            checkIfEmpty();
                        }

                        @Override
                        public void onItemRangeRemoved(int positionStart, int itemCount) {
                            super.onItemRangeRemoved(positionStart, itemCount);
                            checkIfEmpty();
                        }

                        private void checkIfEmpty() {
                            switcher.setDisplayedChild(adaptador.getItemCount() > 0 ? 1 : 0);
                        }
                    });
                    recListItems.setAdapter(adaptador);
                    sortOrder = new Pair<>(0, 1);
                } else {
                    switcher.setDisplayedChild(0);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Producto>> call, @NotNull Throwable t) {

                t.printStackTrace();

                switcher.setDisplayedChild(0);
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Error")
                        .setMessage("Internet not available, check your internet connection and try again.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", (dialog, which) -> requireActivity().finishAffinity()).show();
            }
        });
    }

}