package com.seba.inventariado.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.seba.inventariado.R;
import com.seba.inventariado.model.Tag;
import com.seba.inventariado.utils.ApiClient;
import com.seba.inventariado.utils.ICompararTags;
import com.seba.inventariado.utils.ServerService;
import com.seba.inventariado.utils.SessionManager;
import com.seba.inventariado.utils.TagAdapter;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagFragment extends Fragment {

    private Context context;
    private List<Tag> listaTags;
    private SessionManager session;
    private ViewSwitcher switcher;
    private RecyclerView recListItems;
    private TagAdapter adaptador;
    private LinearLayoutManager layoutManager;
    private Pair<Integer, Integer> sortOrder;

    private boolean mostrarBotones = false;

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
        View view = inflater.inflate(R.layout.fragment_tag, container, false);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        getActivity().setTitle("Tags");

        switcher = view.findViewById(R.id.viewSwitcherTags);
        recListItems = view.findViewById(R.id.list_tags);

        Button addTag = view.findViewById(R.id.add_new_tag);
        addTag.setOnClickListener(click -> {
            Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_tag);

            EditText tagName = dialog.findViewById(R.id.tagName);

            Button btnCancelar = dialog.findViewById(R.id.cancel_tag);
            btnCancelar.setOnClickListener((v) -> dialog.dismiss());

            Button btnGuardar = dialog.findViewById(R.id.add_tag);
            btnGuardar.setOnClickListener((v) -> {
                if (!StringUtils.isBlank(tagName.getText().toString())) {

                    ServerService tagToAdd = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
                    Call<Tag> tagResponseCall = tagToAdd.saveTag(tagName.getText().toString());

                    tagResponseCall.enqueue(new Callback<Tag>() {
                        @Override
                        public void onResponse(@NotNull Call<Tag> call, @NotNull Response<Tag> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                adaptador = new TagAdapter(context, listaTags, recListItems, switcher, mostrarBotones, TagFragment.this);
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
                } else {
                    tagName.setError("This field must have a valid name.");
                }
            });

            dialog.setOnCancelListener((v) -> dialog.dismiss());
            dialog.show();
        });

        SwipeRefreshLayout swipeContainer = view.findViewById(R.id.swipe_tags);
        swipeContainer.setOnRefreshListener(() -> {
            getTags();
            swipeContainer.setRefreshing(false);
        });

        adaptador = new TagAdapter(context, listaTags, recListItems, switcher, mostrarBotones, this);
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

        ImageButton sort = view.findViewById(R.id.sort_tags);
        sort.setOnClickListener(a -> {
            if (sortOrder.first == 0 && sortOrder.second == 0) {
                ordenarTags(new ICompararTags.ComparadorNombreZ_A());
                sort.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                sortOrder = new Pair<>(0, 1);
            } else if (sortOrder.first == 0 && sortOrder.second == 1) {
                ordenarTags(new ICompararTags.ComparadorNombreA_Z());
                sort.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_menos_mas));
                sortOrder = new Pair<>(0, 0);
            } else {
                ordenarTags(new ICompararTags.ComparadorNombreZ_A());
                sort.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sort_mas_menos));
                sortOrder = new Pair<>(0, 0);
            }
        });

        SearchView searchView = view.findViewById(R.id.busqueda_tags);
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

        getTags();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tags, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            mostrarBotones = !mostrarBotones;
            adaptador = new TagAdapter(context, listaTags, recListItems, switcher, mostrarBotones, this);
            recListItems.setAdapter(adaptador);
        } else if (id == R.id.action_add) {
            Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_tag);

            EditText tagName = dialog.findViewById(R.id.tagName);

            Button btnCancelar = dialog.findViewById(R.id.cancel_tag);
            btnCancelar.setOnClickListener((v) -> dialog.dismiss());

            Button btnGuardar = dialog.findViewById(R.id.add_tag);
            btnGuardar.setOnClickListener((v) -> {
                if (!StringUtils.isBlank(tagName.getText().toString())) {

                    ServerService tagToAdd = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
                    Call<Tag> tagResponseCall = tagToAdd.saveTag(tagName.getText().toString());

                    tagResponseCall.enqueue(new Callback<Tag>() {
                        @Override
                        public void onResponse(@NotNull Call<Tag> call, @NotNull Response<Tag> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                getTags();
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
                } else {
                    tagName.setError("This field must have a valid name.");
                }
            });

            dialog.setOnCancelListener((v) -> dialog.dismiss());
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ordenarTags(Comparator<? super Tag> comp) {
        Collections.sort(listaTags, comp);
        adaptador = new TagAdapter(context, listaTags, recListItems, switcher, mostrarBotones, this);
        recListItems.setAdapter(adaptador);
    }

    public void getTags() {
        getTagsFromServer();
        if (switcher.getDisplayedChild() == 1) {
            adaptador = new TagAdapter(context, listaTags, recListItems, switcher, mostrarBotones, this);
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
    }

    private void getTagsFromServer() {
        ServerService dashboardValuesServer = ApiClient.createService(ServerService.class, session.getUserDetails().getToken());
        Call<List<Tag>> LoginResponseCall = dashboardValuesServer.allTags();
        LoginResponseCall.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(@NotNull Call<List<Tag>> call, @NotNull Response<List<Tag>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    switcher.setDisplayedChild(1);
                    listaTags = response.body();
                    ordenarTags(new ICompararTags.ComparadorNombreA_Z());
                    adaptador = new TagAdapter(context, listaTags, recListItems, switcher, mostrarBotones, TagFragment.this);
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
            public void onFailure(@NotNull Call<List<Tag>> call, @NotNull Throwable t) {

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