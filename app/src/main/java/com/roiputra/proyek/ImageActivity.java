package com.roiputra.proyek;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.DetailImage;
import com.roiputra.proyek.Model.ResponseDetailImage;
import com.roiputra.proyek.clientApi.ClientApi;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_URL_IMAGE = "";
    public static final String EXTRA_ID = "";
    private float mScaleFactor = 1.0f;
    private PhotoView imageView;
    private ProgressDialog pg;
    private TextView tukang, assisten_tukang, alokasi_pekerjaan, cuaca, volume, keterangan, user;
    private LinearLayout layout_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Integer role = Integer.parseInt(sharedPref.getString("role", ""));

        ImageButton img_btn = findViewById(R.id.back_image_report);
        String url = getIntent().getStringExtra(EXTRA_URL_IMAGE);
        String id_image = getIntent().getExtras().getString("id_image","");
        imageView = findViewById(R.id.img_view);
        Log.d("TAG", "onCreate: "+id_image + url);
        tukang = findViewById(R.id.tukang_detail);
        assisten_tukang = findViewById(R.id.assisten_tukang_detail);
        alokasi_pekerjaan = findViewById(R.id.alokasi_pekerjaan_detail);
        cuaca = findViewById(R.id.cuaca_detail);
        volume = findViewById(R.id.volume_detail);
        keterangan = findViewById(R.id.keterangan_detail);
        user = findViewById(R.id.user_detail);
        layout_user = findViewById(R.id.layout_keterangan_user_detail);

        pg = new ProgressDialog(this);

        ApiService api = ClientApi.getApi().create(ApiService.class);
        Call<ResponseDetailImage> detail = api.getDetailImage(sharedPref.getString("id_user",""), sharedPref.getString("token",""), id_image);
        detail.enqueue(new Callback<ResponseDetailImage>() {
            @Override
            public void onResponse(Call<ResponseDetailImage> call, Response<ResponseDetailImage> response) {
                if (response.body() != null){
                    if (response.body().isStatus()){
                        List<DetailImage> detail_image_data;
                        detail_image_data = response.body().getData();

                        if (!(detail_image_data.get(0)==null)) {
                            if (!(detail_image_data.get(0).getTukang() == null)) {
                                String data_tukang = detail_image_data.get(0).getTukang();
                                tukang.setText(data_tukang);
                            }
                            if (!(detail_image_data.get(0).getAssisten_tukang() == null)) {
                                String data_assisten_tukang = detail_image_data.get(0).getAssisten_tukang();
                                assisten_tukang.setText(data_assisten_tukang);
                            }
                            if (!(detail_image_data.get(0).getCuaca() == null)) {
                                String data_cuaca = detail_image_data.get(0).getCuaca();
                                cuaca.setText(data_cuaca);
                            }
                            if (!(detail_image_data.get(0).getVolume() == null)) {
                                String data_volume = detail_image_data.get(0).getVolume();
                                volume.setText(data_volume);
                            }
                            if (!(detail_image_data.get(0).getAlokasi_pekerjaan() == null)) {
                                String data_alokasi_pekerjaan = detail_image_data.get(0).getAlokasi_pekerjaan();
                                alokasi_pekerjaan.setText(data_alokasi_pekerjaan);
                            }
                            if (!(detail_image_data.get(0).getKeterangan() == null)) {
                                String data_keterangan = detail_image_data.get(0).getKeterangan();
                                keterangan.setText(data_keterangan);
                            }
                            if (role==1){
                                if (!(detail_image_data.get(0).getNama_user() == null)) {
                                    String data_user = detail_image_data.get(0).getNama_user();
                                    user.setText(data_user);
                                }
                            }else {
                                layout_user.setVisibility(View.GONE);
                            }
                        }
                    }
                }else {
                    Toast.makeText(ImageActivity.this, "Tunggu Beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDetailImage> call, Throwable t) {
                Log.d("TAG", "onFailure: fail "+t);
                Toast.makeText(ImageActivity.this, "Gagal Menghubungkan dengan server", Toast.LENGTH_SHORT).show();
            }
        });

        pg.setCancelable(false);
        pg.setMessage("Loading Gambar");
        pg.show();
        Picasso.get().load(url).into(imageView, new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {
                pg.dismiss();
            }

            @Override
            public void onError(Exception e) {
                pg.dismiss();
                Toast.makeText(ImageActivity.this, "Gagal Menghubungkan Dengan Server", Toast.LENGTH_SHORT).show();
            }
        });
        img_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

}