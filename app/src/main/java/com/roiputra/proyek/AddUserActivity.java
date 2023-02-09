package com.roiputra.proyek;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.User;
import com.roiputra.proyek.clientApi.ClientApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btn_back;
    private EditText nama, username, password, id_proyek;
    private Integer role = 1;
    private ApiService api;
    private Call<User> call;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
        RadioButton rb1 = (RadioButton) findViewById(R.id.rb1) ;
        rb1.setChecked(true);
        Button simpan = (Button) findViewById(R.id.btn_simpan_user);
        nama = findViewById(R.id.nama_user_add);
        username = findViewById(R.id.username_user_add);
        password = findViewById(R.id.password_user_add);
        id_proyek = findViewById(R.id.id_proyek_add_user);
        api = ClientApi.getApi().create(ApiService.class);


        simpan.setOnClickListener(this);
        EditText ed_id_proyek = (EditText) findViewById(R.id.id_proyek_add_user);
        ed_id_proyek.setEnabled(false);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rb1){
                    role = 1;
                    ed_id_proyek.setEnabled(false);
                    ed_id_proyek.setBackgroundColor(Color.parseColor("#e8e8e8"));
                }else if (checkedId==R.id.rb2){
                    role = 2;
                    ed_id_proyek.setEnabled(false);
                    ed_id_proyek.setBackgroundColor(Color.parseColor("#e8e8e8"));
                }else if (checkedId==R.id.rb3){
                    role = 3;
                    ed_id_proyek.setEnabled(true);
                    ed_id_proyek.setBackgroundResource(R.drawable.border_user);
                }
            }
        });
        btn_back = findViewById(R.id.back);
        btn_back.bringToFront();
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back){
            finish();
        }

        if (v.getId() == R.id.btn_simpan_user){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String snama = nama.getText().toString();
            String susername = username.getText().toString();
            String spassword = password.getText().toString();
            String sid_proyek = id_proyek.getText().toString();

            boolean is_empty = false;

            if (TextUtils.isEmpty(snama)){
                is_empty = true;
                nama.setError("nama_tidak boleh kosong!!");
            }

            if (TextUtils.isEmpty(susername)){
                is_empty = true;
                username.setError("username tidak boleh kosong!!");
            }

            if (TextUtils.isEmpty(spassword)){
                is_empty = true;
                password.setError("password tidak boleh kosong");
            }

            if (role == 3){
                call = api.addUser(sid_proyek, snama, susername, spassword, role.toString(), sharedPref.getString("id_user", null), sharedPref.getString("token", null));
                if (TextUtils.isEmpty(sid_proyek)){
                    is_empty = true;
                    id_proyek.setError("id proyek tidak boleh kosong");
                }
            }else{
                call = api.addUser("", snama, susername, spassword, role.toString(), sharedPref.getString("id_user", null), sharedPref.getString("token", null));
            }


            if (!is_empty){
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.d("TAG", "onResponse: "+response.body().getStatus());
                        if (response.body() != null){
                            if (response.body().getStatus()){
                                Toast.makeText(AddUserActivity.this, "Success Menambahkan User", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(AddUserActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(AddUserActivity.this, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(AddUserActivity.this, "gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onFailure: "+t);
                    }
                });

            }

        }
    }
}