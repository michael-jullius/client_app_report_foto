package com.roiputra.proyek.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.roiputra.proyek.ImageReportActivity;
import com.roiputra.proyek.Model.Proyek;
import com.roiputra.proyek.R;


import java.util.List;

public class ProyekAdapter extends RecyclerView.Adapter<ProyekAdapter.HolderDataProyek>{
    private Context ctx;
    private final List<Proyek> proyekList;

    public ProyekAdapter(Context ctx, List<Proyek> proyekList)
    {
        this.ctx=ctx;
        this.proyekList=proyekList;
    }

    @NonNull
    @Override
    public HolderDataProyek onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proyek, parent, false);
        return new HolderDataProyek(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDataProyek holder, int position) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        Proyek proyek = proyekList.get(position);
        Log.d("position", "onBindViewHolder: "+proyekList.get(position).getNama_proyek());
        if (Integer.parseInt(sharedPref.getString("role",  "")) == 1){
            holder.nama_proyek.setText(proyek.getId()+"_"+proyek.getNama_proyek());
        }else {
            holder.nama_proyek.setText(proyek.getNama_proyek());
        }


        holder.nama_proyek.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, ImageReportActivity.class);
            intent.putExtra(ImageReportActivity.EXTRA_ID_PROYEK, proyek.getId());
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return proyekList.size();
    }

    class HolderDataProyek extends RecyclerView.ViewHolder{
        TextView nama_proyek;
        public HolderDataProyek(@NonNull View itemView) {
            super(itemView);
            ctx =itemView.getContext();
            nama_proyek = itemView.findViewById(R.id.tv_nama_proyek);
        }
    }
}
