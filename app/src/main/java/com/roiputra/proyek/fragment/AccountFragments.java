package com.roiputra.proyek.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.roiputra.proyek.Adapter.UserAdapter;
import com.roiputra.proyek.AddUserActivity;
import com.roiputra.proyek.EditUserActivity;
import com.roiputra.proyek.Interface.ApiService;
import com.roiputra.proyek.LoginActivity;
import com.roiputra.proyek.Model.ResponseUser;
import com.roiputra.proyek.Model.User;
import com.roiputra.proyek.R;
import com.roiputra.proyek.clientApi.ClientApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragments extends Fragment implements View.OnClickListener {
    private List<User> listUser = new ArrayList<>();
    private RecyclerView recyclerView;
    private View view;
    private RecyclerView.Adapter rva;
    private SwipeRefreshLayout refreshLayout;
    public AccountFragments(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_account, container, false);
        init();
        Log.d("TAG", "onCreateView: "+true);
        return view;
    }

    public void init(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        TextView nama = (TextView) view.findViewById(R.id.nama_user);
        TextView username = (TextView) view.findViewById(R.id.username);
        TextView user_level = (TextView) view.findViewById(R.id.level);
        Button btn_logout = (Button) view.findViewById(R.id.logout);
        Button btn_add_user = (Button) view.findViewById(R.id.add_user);
        Button btn_edit_user = view.findViewById(R.id.edit_user);
        refreshLayout = view.findViewById(R.id.swipeUser);
        recyclerView = view.findViewById(R.id.rv_list_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout.setOnRefreshListener(this::getUser);

        btn_logout.setOnClickListener(this);
        btn_add_user.setOnClickListener(this);
        btn_edit_user.setOnClickListener(this);

        Integer role = Integer.parseInt(sharedPref.getString("role",null));
        nama.setText(sharedPref.getString("nama", null));
        username.setText(sharedPref.getString("username", null));
        if (role == 1){
            user_level.setText("admin");
        }else if (role == 2){
            LinearLayout management_user = (LinearLayout) view.findViewById(R.id.management_user);
            management_user.setVisibility(View.INVISIBLE);
            user_level.setText("user lapangan");
        }
        getUser();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout){
            Intent login = new Intent(getContext(), LoginActivity.class);
            startActivity(login);
            getActivity().finish();
        }else if(v.getId() == R.id.add_user){
            Intent add_user = new Intent(getContext(), AddUserActivity.class);
            startActivity(add_user);
        }else if (v.getId() == R.id.edit_user){
            Intent edit_user = new Intent(getContext(), EditUserActivity.class);
            startActivity(edit_user);
        }
    }


    public void getUser(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        ApiService api = ClientApi.getApi().create(ApiService.class);
        Call<ResponseUser> call = api.getUser(sharedPref.getString("id_user", ""), sharedPref.getString("token", ""));
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.body() != null){
                    Log.d("TAG", "user list total: "+response.body().getData().size());
                    listUser = response.body().getData();
                    rva = new UserAdapter(getContext(), listUser);
                    recyclerView.setAdapter(rva);
                    rva.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), "tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Gagal menghubungkan dengan server", Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setRefreshing(false);
    }
}
