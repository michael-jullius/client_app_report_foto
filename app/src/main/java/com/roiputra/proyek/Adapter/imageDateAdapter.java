package com.roiputra.proyek.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.ImageData;
import com.roiputra.proyek.Model.ImageDate;

import com.roiputra.proyek.Model.ResponseImageData;
import com.roiputra.proyek.R;

import com.roiputra.proyek.clientApi.ClientApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class imageDateAdapter extends RecyclerView.Adapter<imageDateAdapter.holderDataImageDate>{
    private Context ctx;
    private final List<ImageDate> imageDateList;
    private List<ImageData> imageData_pagi = new ArrayList<>();
    private List<ImageData> imageData_sore = new ArrayList<>();
    private final String id_proyek;

    public imageDateAdapter(Context ctx, List<ImageDate> imageDateList, String id_proyek){
        this.id_proyek = id_proyek;
        this.ctx = ctx;
        this.imageDateList = imageDateList;
    }

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    @NonNull
    @Override
    public holderDataImageDate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_date_image, parent, false);
        return new holderDataImageDate(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull holderDataImageDate holder, int position) {
        holder.pagi.setVisibility(View.INVISIBLE);
        holder.sore.setVisibility(View.INVISIBLE);
        ImageDate imageDate = imageDateList.get(position);
        Log.d("date", "onBindViewHolder: "+imageDate.getDate());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        ApiService api = ClientApi.getApi().create(ApiService.class);
        Log.d("TAG", "onBindViewHolder: true");
        Call<ResponseImageData> call =  api.getDataImage(id_proyek,imageDate.getDate(), sharedPref.getString("id_user", null), sharedPref.getString("token", null));
        call.enqueue(new Callback<ResponseImageData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseImageData> call, @NonNull Response<ResponseImageData> response) {
                if (response.body() != null){
                    holder.title_image.setText(imageDate.getDate());
                    imageData_pagi = response.body().getPagi();
                    imageData_sore = response.body().getSore();
                    Log.d("TAG", "onResponse: pagi"+imageDate.getDate()+(imageData_pagi.size()));
                    Log.d("TAG", "onResponse: sore"+imageDate.getDate()+(imageData_sore.size()));
                    if (imageData_pagi.size()>=1){
                        holder.pagi.setVisibility(View.VISIBLE);
                        LinearLayoutManager image_list_layout = new LinearLayoutManager(ctx, RecyclerView.HORIZONTAL, false);
                        holder.rv_image.setLayoutManager(image_list_layout);
                        holder.rv_image.setAdapter(new ImageDataAdapter(holder.rv_image.getContext(), imageData_pagi, id_proyek));
                        holder.rv_image.setRecycledViewPool(viewPool);
                    }

                    if (imageData_sore.size()>=1){
                        holder.sore.setVisibility(View.VISIBLE);
                        Log.d("TAG", "onResponse: terpanggil true");
                        LinearLayoutManager image_list_layout_sore = new LinearLayoutManager(ctx, RecyclerView.HORIZONTAL, false);
                        holder.rv_image_sore.setLayoutManager(image_list_layout_sore);
                        holder.rv_image_sore.setAdapter(new ImageDataAdapter(holder.rv_image_sore.getContext(), imageData_sore, id_proyek));
                        holder.rv_image_sore.setRecycledViewPool(viewPool);
                    }
                }else {
                    Toast.makeText(ctx, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseImageData> call, @NonNull Throwable t) {
                Toast.makeText(ctx, "Gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageDateList.size();

    }

    class holderDataImageDate extends RecyclerView.ViewHolder{
        TextView title_image, pagi, sore;
        RecyclerView rv_image, rv_image_sore;
        public holderDataImageDate(@NonNull View itemView) {
            super(itemView);
            ctx = itemView.getContext();
            title_image = itemView.findViewById(R.id.titleDate);
            rv_image = itemView.findViewById(R.id.rv_image_data);
            pagi = itemView.findViewById(R.id.tv_pagi);
            sore = itemView.findViewById(R.id.tv_sore);
            rv_image_sore = itemView.findViewById(R.id.rv_image_data_sore);
        }
    }
}
