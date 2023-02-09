package com.roiputra.proyek.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.roiputra.proyek.Adapter.ProyekAdapter;
import com.roiputra.proyek.Interface.ApiService;

import com.roiputra.proyek.MainActivity;
import com.roiputra.proyek.Model.Proyek;
import com.roiputra.proyek.Model.ResponseProyek;
import com.roiputra.proyek.R;
import com.roiputra.proyek.clientApi.ClientApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<Proyek> arrayList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.Adapter rva;
    private ProgressDialog dialog;

    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_proyek, container, false);
        init();
        return view;
    }

    private void  init(){
        recyclerView = view.findViewById(R.id.rv_home_proyek);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.swipehome);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbarHome);
        ((MainActivity) requireContext()).setSupportActionBar(toolbar);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();
        dialog.setCancelable(false);

        getProyek();

        refreshLayout.setOnRefreshListener(this::getProyek);

    }

    private void getProyek() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ApiService api = ClientApi.getApi().create(ApiService.class);
        Log.d("TAG", "getProyek: "+sharedPref.getString("id_user", null)+sharedPref.getString("token", null));
        Call<ResponseProyek> dataProyek = api.getProyek(sharedPref.getString("id_user", null), sharedPref.getString("token", null));
        dataProyek.enqueue(new Callback<ResponseProyek>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseProyek> call, @NonNull Response<ResponseProyek> response) {
                if (response.body() != null){
                    Log.d("api", "onResponse: "+response.body().getData().size());
                    arrayList = response.body().getData();
                    rva = new ProyekAdapter(getContext(), arrayList);
                    recyclerView.setAdapter(rva);
                    rva.notifyDataSetChanged();
                    dialog.dismiss();
                }else {
                    Toast.makeText(getContext(), "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseProyek> call, @NonNull Throwable t) {
                Log.d("api", "onFailure: "+t);
                refreshLayout.setRefreshing(false);
                dialog.hide();
            }
        });
        refreshLayout.setRefreshing(false);
    }
}
