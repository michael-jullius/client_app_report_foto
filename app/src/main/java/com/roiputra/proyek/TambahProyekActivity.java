package com.roiputra.proyek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.ResponseProyek;
import com.roiputra.proyek.clientApi.ClientApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahProyekActivity extends AppCompatActivity implements View.OnClickListener {

    private ApiService apiService;
    private EditText nama_proyek;
    private ImageButton btn_back;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_proyek);

        nama_proyek = findViewById(R.id.input_nama_proyek);
        dialog = new ProgressDialog(TambahProyekActivity.this);
        apiService = ClientApi.getApi().create(ApiService.class);
        Button btn_add = findViewById(R.id.btn_add_proyek);
        btn_back = findViewById(R.id.back);

        btn_add.setOnClickListener(this);
        btn_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_proyek){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(TambahProyekActivity.this);

            dialog.setMessage("Mohon tunggu Sedang Menambahkan Data");
            dialog.show();

            String snama_proyek = nama_proyek.getText().toString();

            boolean isEmptyField = false;

            if (TextUtils.isEmpty(snama_proyek)){
                isEmptyField = true;
                Log.d("NamaProyek", "Nama Proyek kosong");
                dialog.hide();
                nama_proyek.setError("Nama Proyek tidak boleh kosong!");
            }

            if (!isEmptyField) {
                Call<ResponseProyek> dataResponse = apiService.addProyek(sharedPref.getString("id_user", null), sharedPref.getString("token", null), snama_proyek);
                dataResponse.enqueue(new Callback<ResponseProyek>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseProyek> call, @NonNull Response<ResponseProyek> response) {
                        if (response.body() != null){
                            Log.d("api", "onResponse: "+response.body().getStatus());
                            dialog.dismiss();
                            finish();
                        }else {
                            Toast.makeText(TambahProyekActivity.this, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseProyek> call, @NonNull Throwable t) {
                        Toast.makeText(TambahProyekActivity.this, "Gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else if (v.getId()==R.id.back){
            finish();
        }
    }
}