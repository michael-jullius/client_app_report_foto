package com.roiputra.proyek.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.roiputra.proyek.ImageActivity;
import com.roiputra.proyek.ImageReportActivity;
import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.DetailImage;
import com.roiputra.proyek.Model.ImageData;

import com.roiputra.proyek.Model.ResponseDetailImage;
import com.roiputra.proyek.Model.ResponseImage;
import com.roiputra.proyek.R;
import com.roiputra.proyek.clientApi.ClientApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageDataAdapter extends RecyclerView.Adapter<ImageDataAdapter.holderImageData>{
    private final Context ctx;
    private final List<ImageData> imageDateList;
    private final String id_proyek;

    public ImageDataAdapter(Context ctx, List<ImageData> data, String id_proyek){
        this.ctx = ctx;
        this.imageDateList = data;
        this.id_proyek = id_proyek;
    }

    @NonNull
    @Override
    public holderImageData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image, parent, false);
        return new holderImageData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull holderImageData holder, int position) {
        ImageData imageData = imageDateList.get(position);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        ApiService api = ClientApi.getApi().create(ApiService.class);
        Log.d("TAG", "onBindViewHolder: "+imageData.getMini_image());
        Picasso.get().load(imageData.getMini_image()).resize(100, 100).centerCrop().into(holder.image);
        holder.image_btn.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, ImageActivity.class);
            intent.putExtra("id_image", imageData.getId());
            intent.putExtra(ImageActivity.EXTRA_URL_IMAGE, imageData.getImage());
            ctx.startActivity(intent);
        });
        Call<ResponseDetailImage> detail = api.getDetailImage(sharedPref.getString("id_user",""), sharedPref.getString("token",""), imageData.getId());
        detail.enqueue(new Callback<ResponseDetailImage>() {
            @Override
            public void onResponse(Call<ResponseDetailImage> call, Response<ResponseDetailImage> response) {
                boolean status = false;

                if (response.body() != null){
                    status = true;
                }

                if (status){
                    List<DetailImage> detail_image_data;
                    detail_image_data = response.body().getData();

                    if (!(detail_image_data.get(0)==null)){
                        if (!(detail_image_data.get(0).getTukang() == null)){
                            String data_tukang = detail_image_data.get(0).getTukang();
                            holder.tukang.setText(data_tukang);
                        }
                        if (!(detail_image_data.get(0).getAssisten_tukang()==null)){
                            String data_assisten_tukang = detail_image_data.get(0).getAssisten_tukang();
                            holder.assisten_tukang.setText(data_assisten_tukang);
                        }
                        if (!(detail_image_data.get(0).getCuaca()==null)){
                            String data_cuaca = detail_image_data.get(0).getCuaca();
                            holder.cuaca.setText(data_cuaca);
                        }
                        if (!(detail_image_data.get(0).getVolume()==null)){
                            String data_volume = detail_image_data.get(0).getVolume();
                            holder.volume.setText(data_volume);
                        }
                        if (!(detail_image_data.get(0).getNama_user() == null)) {
                            String data_user = detail_image_data.get(0).getNama_user();
                            holder.user.setText(data_user);
                        }
                        if (!(detail_image_data.get(0).getAlokasi_pekerjaan() == null)){
                            String data_alokasi_pekerjaan  = detail_image_data.get(0).getAlokasi_pekerjaan().substring(0,5)+"..";
                            holder.alokasi_pekerjaan.setText(data_alokasi_pekerjaan);
                        }
                        if (!(detail_image_data.get(0).getKeterangan() == null)){
                            String data_keterangan = detail_image_data.get(0).getKeterangan().substring(0,5) + "..";
                            holder.keterangan.setText(data_keterangan);
                        }
                    }
                }else {
                    Toast.makeText(ctx, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDetailImage> call, Throwable t) {
                Log.d("TAG", "onFailure: fail "+t);
            }
        });
        int role = Integer.parseInt(sharedPref.getString("role",""));
        if (role == 1){
            holder.image_btn.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setCancelable(true);
                builder.setTitle("konfirmasi delete");
                builder.setMessage("Yakin ingin menghapus ini");
                builder.setPositiveButton("Confirm", (dialog, which) -> {
                    Call<ResponseImage> call = api.deleteGalery(sharedPref.getString("id_user",""), sharedPref.getString("token",""), imageData.getId());
                    call.enqueue(new Callback<ResponseImage>() {
                        @Override
                        public void onResponse(Call<ResponseImage> call, Response<ResponseImage> response) {
                            if (response.body() != null){
                                if (response.body().getStatus()){
                                    imageDateList.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemChanged(holder.getAdapterPosition(), imageDateList.size());
                                    holder.itemView.setVisibility(View.GONE);
                                    Log.d("TAG", "image data: "+ imageDateList.size());
                                    if (imageDateList.size()==0){
                                        ((Activity) ctx).finish();
                                        Intent intent = new Intent(ctx, ImageReportActivity.class);
                                        intent.putExtra(ImageReportActivity.EXTRA_ID_PROYEK, id_proyek);
                                        ctx.startActivity(intent);
                                    }
                                    Toast.makeText(ctx, "Berhasil Menghapus Gambar", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ctx, "gagal menghapus Gambar", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(ctx, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseImage> call, Throwable t) {
                            Toast.makeText(ctx, "gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return imageDateList.size();
    }

    static class holderImageData extends RecyclerView.ViewHolder{
        ShapeableImageView image;
        RelativeLayout image_btn;
        TextView tukang, assisten_tukang, alokasi_pekerjaan, cuaca, volume, user, keterangan;
        LinearLayout layout_user;
        public holderImageData(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            image_btn = itemView.findViewById(R.id.id_image_fokus);
            tukang = itemView.findViewById(R.id.tukang);
            assisten_tukang = itemView.findViewById(R.id.assisten_tukang);
            alokasi_pekerjaan = itemView.findViewById(R.id.alokasi_pekerjaan);
            cuaca = itemView.findViewById(R.id.cuaca);
            volume = itemView.findViewById(R.id.volume);
            user = itemView.findViewById(R.id.user);
            layout_user = itemView.findViewById(R.id.layout_ket_user);
            keterangan = itemView.findViewById(R.id.keteranga);
        }
    }
}
