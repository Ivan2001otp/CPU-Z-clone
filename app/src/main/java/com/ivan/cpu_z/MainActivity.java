package com.ivan.cpu_z;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.ivan.cpu_z.Fragments.InfoFragment;
import com.ivan.cpu_z.Fragments.SensorFragment;
import com.ivan.cpu_z.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentShifter(new InfoFragment());



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
           case R.id.sensor_fragment:
                FragmentShifter(new SensorFragment());
                break;

           case R.id.info_fragment:
                FragmentShifter(new InfoFragment());
                break;
        }

        return true;
    }

    private void FragmentShifter(Fragment fragement){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(binding.targetFrameLayout.getId(),fragement);
        fragmentTransaction.commit();
    }
}