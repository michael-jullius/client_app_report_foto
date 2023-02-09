package com.roiputra.proyek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.User;
import com.roiputra.proyek.clientApi.ClientApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ApiService apiService;
    private EditText nama_perusahaan, nama, username, password;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama_perusahaan = findViewById(R.id.nama_perusahaan);
        nama = findViewById(R.id.nama);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        dialog = new ProgressDialog(RegisterActivity.this);
        Button btn_register = findViewById(R.id.btn_register);
        apiService = ClientApi.getApi().create(ApiService.class);

        btn_register.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register){
            dialog.setMessage("Registering...");
            dialog.setCancelable(false);
            dialog.show();

            String snama_perusahaan = nama_perusahaan.getText().toString();
            String snama = nama.getText().toString();
            String susername = username.getText().toString();
            String spassword = password.getText().toString();

            boolean isEmptyField = false;

            if (TextUtils.isEmpty(snama_perusahaan)){
                isEmptyField = true;
                nama_perusahaan.setError("nama perusahaan tidak boleh kosong!!");
                dialog.hide();
            }

            if (TextUtils.isEmpty(snama)){
                isEmptyField = true;
                nama.setError("nama tidak boleh kosong!!");
                dialog.hide();
            }

            if (TextUtils.isEmpty(susername)){
                isEmptyField = true;
                username.setError("username tidak boleh kosong!!");
                dialog.hide();
            }

            if (TextUtils.isEmpty(spassword)){
                isEmptyField = true;
                password.setError("password tidak boleh kosong!!");
                dialog.hide();
            }

            if (!isEmptyField) {
                Call<User> register = apiService.register(snama_perusahaan, snama, susername, spassword);
                register.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.body() != null){
                            Log.d("api", "onResponse: "+response.body().getStatus());
                            if (response.body().getStatus()){
                                Toast.makeText(RegisterActivity.this, "Berhasil Mendaftar", Toast.LENGTH_SHORT).show();
                                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                                dialog.dismiss();
                                startActivity(login);
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, ""+response.body().getError(), Toast.LENGTH_SHORT).show();
                                dialog.hide();
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.d("api", "onFailure: "+t);
                        Toast.makeText(RegisterActivity.this, "Gagal Menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                        dialog.hide();
                    }
                });
            }
        }
    }
}