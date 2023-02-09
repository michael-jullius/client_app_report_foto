package com.roiputra.proyek;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.ResponseImage;
import com.roiputra.proyek.clientApi.ClientApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddImageActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ID_PROYEK = "";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99 ;
    private static final int SELECT_REQUEST_CODE = 1000;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private ImageButton btn_back;
    File file_image = null;
    private String id_proyek_data;
    private EditText tukang, assisten_tukang, alokasi_pekerjaan, cuaca, volume, keterangan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        imageView = findViewById(R.id.view_image);
        Button selectImage = findViewById(R.id.pilih_gambar);
        Button save = findViewById(R.id.btn_submit);
        btn_back = findViewById(R.id.back);
        tukang = findViewById(R.id.ed_tukang);
        assisten_tukang = findViewById(R.id.ed_assisten_tukang);
        alokasi_pekerjaan = findViewById(R.id.ed_alokasi_pekerjaan);
        cuaca = findViewById(R.id.ed_cuaca);
        volume = findViewById(R.id.ed_volume);
        keterangan =findViewById(R.id.ed_keterangan);

        btn_back.setOnClickListener(this);
        id_proyek_data = getIntent().getStringExtra(EXTRA_ID_PROYEK);
        save.setOnClickListener(this);
        selectImage.setOnClickListener(this);

        CheckPermission();
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(AddImageActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AddImageActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AddImageActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddImageActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(AddImageActivity.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(AddImageActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(AddImageActivity.this)
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(AddImageActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_LOCATION);


                            startActivity(new Intent(AddImageActivity
                                    .this, MainActivity.class));
                            AddImageActivity.this.overridePendingTransition(0, 0);
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(AddImageActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            String stukang, s_assisten_tukang, s_alokasi_pekerjaan, s_cuaca, s_volume, s_keterangan;
            stukang = tukang.getText().toString();
            s_assisten_tukang = assisten_tukang.getText().toString();
            s_alokasi_pekerjaan = alokasi_pekerjaan.getText().toString();
            s_cuaca = cuaca.getText().toString();
            s_volume = volume.getText().toString();
            s_keterangan = keterangan.getText().toString();
            progressDialog = new ProgressDialog(AddImageActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (file_image == null){
                Toast.makeText(this, "pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }else {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AddImageActivity.this);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file_image);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", file_image.getName(), requestFile);
                RequestBody id_user = RequestBody.create(MediaType.parse("multipart/form-data"), sharedPref.getString("id_user", null));
                RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), sharedPref.getString("token", null));
                RequestBody id_proyek = RequestBody.create(MediaType.parse("multipart/form-data"), id_proyek_data);
                RequestBody data_tukang = RequestBody.create(MediaType.parse("multipart/form-data"), stukang);
                RequestBody data_assisten_tukang = RequestBody.create(MediaType.parse("multipart/form-data"), s_assisten_tukang);
                RequestBody data_alokasi_pekerjaan = RequestBody.create(MediaType.parse("multipart/form-data"), s_alokasi_pekerjaan);
                RequestBody data_cuaca = RequestBody.create(MediaType.parse("multipart/form-data"), s_cuaca);
                RequestBody data_volume = RequestBody.create(MediaType.parse("multipart/form-data"), s_volume);
                RequestBody data_keterangan = RequestBody.create(MediaType.parse("multipart/form-data"), s_keterangan);
                Log.d("TAG", "onActivityResult: "+body);

                ApiService api = ClientApi.getApi().create(ApiService.class);
                Call<ResponseImage> call = api.postImage(id_user, token, id_proyek, body, data_tukang, data_assisten_tukang, data_alokasi_pekerjaan, data_cuaca, data_volume, data_keterangan);

                call.enqueue(new Callback<ResponseImage>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseImage> call, @NonNull Response<ResponseImage> response) {
                        if (response.body() != null){
                            if (response.body().getStatus()){
                                progressDialog.dismiss();
                                Toast.makeText(AddImageActivity.this, "Upload image Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(AddImageActivity.this, "Gagal mengupload Image", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(AddImageActivity.this, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseImage> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Log.d("TAG", "onFailure: "+t);
                        Toast.makeText(AddImageActivity.this, "Gagal Menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (v.getId() == R.id.pilih_gambar) {
            Intent iGalery = new Intent(Intent.ACTION_PICK);
            iGalery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGalery, SELECT_REQUEST_CODE);
        }else if (v.getId()==R.id.back){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode==SELECT_REQUEST_CODE){
                assert data != null;
                Uri uri = data.getData();
                String path = RealPath.getRealPath(AddImageActivity.this, uri);
                file_image = new File(path);

                imageView.setImageURI(data.getData());

            }
        }
    }
}
