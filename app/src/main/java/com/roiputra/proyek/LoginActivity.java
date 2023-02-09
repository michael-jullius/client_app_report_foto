package com.roiputra.proyek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ApiService apiService;
    private EditText username, password;
    private ProgressDialog dialog;
    private final Context context = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        dialog = new ProgressDialog(LoginActivity.this);
        apiService = ClientApi.getApi().create(ApiService.class);

        Button btn = findViewById(R.id.submit);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit){
            dialog.setMessage("login");
            dialog.show();

            String susername = username.getText().toString();
            String spassword = password.getText().toString();

            boolean isEmptyField = false;

            if (TextUtils.isEmpty(susername)){
                isEmptyField = true;
                Log.d("username", "usename kosong");
                dialog.hide();
                username.setError("username tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(spassword)){
                isEmptyField = true;
                Log.d("password", "password kosong");
                dialog.hide();
                password.setError("password tidak boleh kosong!");
            }

            if (!isEmptyField){

                Call<User> flogin = apiService.login(susername, spassword);
                flogin.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.body() != null){
                            Log.d("API", "Response :" +response.body().getStatus());
                            if (response.body().getStatus()){
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("id_user", response.body().getId());
                                editor.putString("token", response.body().getToken());
                                editor.putString("role", response.body().getRole());
                                Log.d("TAG", "onResponse: "+response.body().getNama());
                                editor.putString("nama", response.body().getNama());
                                editor.putString("username", response.body().getUsername());
                                editor.apply();
                                String role = response.body().getRole();
                                Log.d("TAG", "onResponse: "+response.body().getStatus());

                                if (Integer.parseInt(role) == 3){
                                    Log.d("TAG", "onResponse: true 3");
                                    Intent main = new Intent(LoginActivity.this, ImageReportActivity.class);
                                    main.putExtra(ImageReportActivity.EXTRA_ID_PROYEK, response.body().getId_proyek());
                                    dialog.dismiss();
                                    startActivity(main);
                                    finish();
                                }else {
                                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                    dialog.dismiss();
                                    startActivity(main);
                                    finish();
                                }
                            }else {
                                dialog.hide();
                                Toast.makeText(LoginActivity.this, "Username/password salah", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(context, "Tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.d("Api", "Response :" +t);
                        dialog.hide();
                        Toast.makeText(LoginActivity.this, "KONEKSI KE SERVER GAGAL", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else if (v.getId() == R.id.register){
            Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(register);
        }
    }
}