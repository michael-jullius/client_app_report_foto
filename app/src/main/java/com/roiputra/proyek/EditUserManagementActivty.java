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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.User;
import com.roiputra.proyek.clientApi.ClientApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserManagementActivty extends AppCompatActivity implements View.OnClickListener {
    private String id_user, nama, username, id_proyek, role;
    private Integer role_int;
    private EditText nama_edt, username_edt, password_edt, ed_id_proyek;
    private ApiService api;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_management_activty);
        Bundle extra_data = getIntent().getExtras();


        ImageButton btn_back = findViewById(R.id.back_edit_user_management);
        id_user = extra_data.getString("id_user");
        nama = extra_data.getString("nama");
        username = extra_data.getString("username");
        id_proyek = extra_data.getString("id_proyek");
        RadioGroup rg = findViewById(R.id.rg);
        RadioButton rb1 = findViewById(R.id.rb1);
        RadioButton rb2 = findViewById(R.id.rb2);
        RadioButton rb3 = findViewById(R.id.rb3);
        role = extra_data.getString("role");
        role_int = Integer.parseInt(role);
        rb2.setChecked(true);
        ed_id_proyek = findViewById(R.id.id_proyek);
        nama_edt = findViewById(R.id.nama_user);
        username_edt = findViewById(R.id.username_user);
        password_edt = findViewById(R.id.password_user);
        Button btn_edit = findViewById(R.id.btn_ubah);
        api = ClientApi.getApi().create(ApiService.class);
        dialog = new ProgressDialog(this);

        ed_id_proyek.setEnabled(false);
        ed_id_proyek.setBackgroundColor(Color.parseColor("#e8e8e8"));
        nama_edt.setHint(nama);
        username_edt.setHint(username);

        Log.d("TAG", "onCreate: "+role_int);
        if (role_int == 1) {
            rb1.setChecked(true);
        }else if (role_int ==2){
            rb2.setChecked(true);
        }else if (role_int ==3){
            rb3.setChecked(true);
            ed_id_proyek.setEnabled(true);
            ed_id_proyek.setHint(id_proyek);
            ed_id_proyek.setBackgroundResource(R.drawable.border_user);
        }
        Log.d("TAG", "onCreate: "+id_user+nama+username+id_proyek+role);

        btn_edit.setOnClickListener(this);



        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rb1){
                    role_int = 1;
                    ed_id_proyek.setEnabled(false);
                    ed_id_proyek.setBackgroundColor(Color.parseColor("#e8e8e8"));
                }else if (checkedId==R.id.rb2){
                    role_int = 2;
                    ed_id_proyek.setEnabled(false);
                    ed_id_proyek.setBackgroundColor(Color.parseColor("#e8e8e8"));
                }else if (checkedId==R.id.rb3){
                    role_int = 3;
                    ed_id_proyek.setEnabled(true);
                    ed_id_proyek.setBackgroundResource(R.drawable.border_user);
                }
            }
        });



        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back_edit_user_management){
            finish();
        }else if (v.getId()==R.id.btn_ubah){
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String sid = id_user.toString();
            String snama = nama_edt.getText().toString();
            String susername = username_edt.getText().toString();
            String spassword = password_edt.getText().toString();
            String srole = role_int.toString();
            String id_proyek = ed_id_proyek.getText().toString();

            Call<User> call = api.editUserData(sid, snama, susername, spassword, srole, id_proyek, sharedPref.getString("id_user", null), sharedPref.getString("token", null));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null){
                        Log.d("TAG", "onResponse: "+response.body().getStatus());
                        if (response.body().getStatus()){
                            Toast.makeText(EditUserManagementActivty.this, "Berhasil merubah user", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }else {
                            Toast.makeText(EditUserManagementActivty.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }else {
                        Toast.makeText(EditUserManagementActivty.this, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("TAG", "onFailure: "+t);
                    dialog.dismiss();
                    Toast.makeText(EditUserManagementActivty.this, "Gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}