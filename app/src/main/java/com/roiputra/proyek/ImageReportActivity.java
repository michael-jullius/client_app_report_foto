package com.roiputra.proyek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roiputra.proyek.Adapter.imageDateAdapter;
import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.ImageDate;
import com.roiputra.proyek.Model.ResponseImage;
import com.roiputra.proyek.clientApi.ClientApi;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageReportActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_ID_PROYEK = "";
    private String id_proyek;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.Adapter recyclerAdapter;
    private ProgressDialog dialog;
    private ApiService api;
    private List<ImageDate> imageDateList = new ArrayList<>();
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ImageReportActivity.this);
        setContentView(R.layout.activity_image_report);
        id_proyek = getIntent().getStringExtra(EXTRA_ID_PROYEK);
        Log.d("TAG", "onCreate: "+id_proyek);
        FloatingActionButton btn_add_image = findViewById(R.id.add_image);
        btn_add_image.setOnClickListener(this);
        api = ClientApi.getApi().create(ApiService.class);
        recyclerView = findViewById(R.id.rv_image_date);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.swipeimagedate);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        btn_back = findViewById(R.id.back);


        btn_back.setOnClickListener(this);

        if (Integer.parseInt(sharedPref.getString("role", null)) == 3){
            btn_add_image.setVisibility(View.INVISIBLE);
        }



        getImageDate();

        refreshLayout.setOnRefreshListener(this::getImageDate);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_image){
            Intent intent = new Intent(ImageReportActivity.this, AddImageActivity.class);
            intent.putExtra(AddImageActivity.EXTRA_ID_PROYEK, id_proyek);
            startActivity(intent);
        }else if (v.getId()==R.id.back){
            finish();
        }
    }

    public void getImageDate(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ImageReportActivity.this);
        Integer role = Integer.parseInt(sharedPref.getString("role", ""));
        Call<ResponseImage> dataImageDate = api.getImageDate(id_proyek, sharedPref.getString("id_user", null), sharedPref.getString("token", null));
        dataImageDate.enqueue(new Callback<ResponseImage>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseImage> call, @NonNull Response<ResponseImage> response) {
                if (response.body() != null){
                    Log.d("TAG", "onResponse: "+response.body().getData().size()+response.body().getStatus());
                    imageDateList = response.body().getData();
                    recyclerAdapter = new imageDateAdapter(ImageReportActivity.this, imageDateList, id_proyek);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }else {
                    Toast.makeText(ImageReportActivity.this, "Tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseImage> call, @NonNull Throwable t) {
                Log.d("TAG", "onFailure: "+t);
                Toast.makeText(ImageReportActivity.this, "Gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
                dialog.dismiss();
            }
        });
        refreshLayout.setRefreshing(false);
    }
}