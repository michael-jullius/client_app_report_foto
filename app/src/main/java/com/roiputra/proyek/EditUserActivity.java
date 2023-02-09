package com.roiputra.proyek;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.User;
import com.roiputra.proyek.clientApi.ClientApi;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btn_back;
    private EditText nama, username, password, id_proyek;
    private SharedPreferences sharedPref;
    private Integer role = 1;
    private ApiService api;
    private Call<User> call;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        btn_back = findViewById(R.id.back_edit_user);
        nama = findViewById(R.id.nama_user_edit);
        username = findViewById(R.id.username_user_edit);
        password = findViewById(R.id.password_user_edit);
        dialog = new ProgressDialog(this);
        Button edit = findViewById(R.id.btn_edit_user);
        api = ClientApi.getApi().create(ApiService.class);


        btn_back.setOnClickListener(this);
        edit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_edit_user){
            finish();
        }else if (v.getId()==R.id.btn_edit_user){
            String sname = nama.getText().toString();
            String susername = username.getText().toString();
            String spassword = password.getText().toString();

            Call<User> call = api.editUser(sname,susername,spassword,sharedPref.getString("id_user", null), sharedPref.getString("token", null));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null){
                        Log.d("TAG", "onResponse: "+response.body().getStatus());
                        if (response.body().getStatus()){
                            Toast.makeText(EditUserActivity.this, "Berhasil Merubah User", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(EditUserActivity.this, ""+response.body().getError(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(EditUserActivity.this, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(EditUserActivity.this, "gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}