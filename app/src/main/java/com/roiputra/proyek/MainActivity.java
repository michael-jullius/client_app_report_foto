package com.roiputra.proyek;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roiputra.proyek.fragment.AccountFragments;
import com.roiputra.proyek.fragment.HomeFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ProyekContainer, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();

        FloatingActionButton btn_tambah = findViewById(R.id.add_proyek);
        btn_tambah.setOnClickListener(this );

        init();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_proyek){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            int role = Integer.parseInt(sharedPref.getString("role", ""));
            if (role == 1){
                Intent add_proyek = new Intent(MainActivity.this, TambahProyekActivity.class);
                startActivity(add_proyek);
            }else {
                Toast.makeText(this, "Anda Tidak Memiliki akses ini", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.item_home: {
                    Fragment account = fragmentManager.findFragmentByTag(AccountFragments.class.getSimpleName());
                    if (account!=null){
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag(AccountFragments.class.getSimpleName()))).commit();
                        fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName()))).commit();
                    }
                    break;
                }

                case R.id.item_user: {
                    Fragment account = fragmentManager.findFragmentByTag(AccountFragments.class.getSimpleName());
                    fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName()))).commit();
                    if (account!=null){
                        fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag(AccountFragments.class.getSimpleName()))).commit();
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName()))).commit();
                    }
                    else{
                        fragmentManager.beginTransaction().add(R.id.ProyekContainer, new AccountFragments(), AccountFragments.class.getSimpleName()).commit();
                    }
                    break;
                }
            }
            return true;
        });
    }
}