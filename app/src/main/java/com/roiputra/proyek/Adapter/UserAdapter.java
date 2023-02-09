package com.roiputra.proyek.Adapter;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.TypedArrayUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.roiputra.proyek.EditUserActivity;
import com.roiputra.proyek.EditUserManagementActivty;
import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.Model.User;
import com.roiputra.proyek.R;
import com.roiputra.proyek.clientApi.ClientApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.holderDataUser>{

    private Context ctx;
    private List<User> userList;
    public UserAdapter(Context ctx, List<User> userList){
        this.ctx =ctx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public holderDataUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        return new holderDataUser(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull holderDataUser holder, @SuppressLint("RecyclerView") int position) {
        User user = userList.get(position);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        Integer role;
        role= Integer.parseInt(user.getRole());
        if (role==1){
            holder.role.setText("admin");
        }else if (role==2){
            holder.role.setText("user lapangan");
        }else if (role==3){
            holder.role.setText("view only");
        }
        holder.nama.setText(user.getNama());

        holder.delete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setCancelable(true);
            builder.setTitle("konfirmasi delete");
            builder.setMessage("Yakin ingin menghapus ini");
            builder.setPositiveButton("Confirm", (dialog, which) -> {
                Log.d("TAG", "onClick: delete true");
                ApiService api = ClientApi.getApi().create(ApiService.class);
                Call<User> call = api.deleteUser(sharedPref.getString("id_user",""), sharedPref.getString("token", ""), user.getId());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null){
                            if (response.body().getStatus()){
                                userList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemChanged(position, userList.size());
                                holder.itemView.setVisibility(View.GONE);
                                Toast.makeText(ctx, "Berhasil Menghapus user", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ctx, response.body().getError(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ctx, "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("TAG", "onFailure: "+t);
                        Toast.makeText(ctx, "Gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
                    }
                });

            });
            builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("TAG", "onClick: delete false");
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, EditUserManagementActivty.class);
            Log.d("TAG", "onBindViewHolder: "+user.getId());
            intent.putExtra("id_user", user.getId());
            intent.putExtra("nama", user.getNama());
            intent.putExtra("username", user.getUsername());
            intent.putExtra("role", user.getRole());
            intent.putExtra("id_proyek", user.getId_proyek());
            ctx.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    class holderDataUser extends RecyclerView.ViewHolder{
        TextView nama, role;
        ImageButton edit, delete;
        public holderDataUser(@NonNull View itemView) {
            super(itemView);

            ctx = itemView.getContext();
            nama = itemView.findViewById(R.id.nama_user_list);
            role = itemView.findViewById(R.id.user_role_list);
            edit = itemView.findViewById(R.id.user_edit_table);
            delete = itemView.findViewById(R.id.user_delete_table);
        }
    }
}
