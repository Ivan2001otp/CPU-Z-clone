package com.ivan.cpu_z.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.speech.RecognitionService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ivan.cpu_z.MainActivity;
import com.ivan.cpu_z.R;
import com.ivan.cpu_z.Utils.Resources;
import com.ivan.cpu_z.databinding.FragmentSensorBinding;

import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SensorFragment extends Fragment implements SensorEventListener {
    private FragmentSensorBinding binding;
    private SensorManager sensorManager;
    private Sensor ambientLightSensor;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;
    private Sensor rotationSensor ;
    private Sensor proximitySensorVar;
    private Sensor pressureSensor;
    //gps,barometer

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 100;

    public SensorFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSensorBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        sensorManager = Resources.buildSensorManager(requireActivity());
        ambientLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor= sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        rotationSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        proximitySensorVar=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);



        if(pressureSensor==null) {Log.d("tag", "onViewCreated: pressure sensor null");
            binding.barometerTv.setText("Barometer: None.");
            Toast.makeText(requireActivity(),"There is no pressure sensor on your device",Toast.LENGTH_SHORT)
                    .show();
        }


        binding.getLocationBtn.setOnClickListener(v->{
            Log.d("tag", "onViewCreated: btn clicked");
            getLastLocation();
        });


    }

    private void getLastLocation() {
        Log.d("tag", "onViewCreated: fun inside");

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                        @Override
                        public void onSuccess(Location location) {
                                if(location!=null){

                                    Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
                                    List<Address>address=null;
                                    try{
                                        address = geocoder.getFromLocation(location.getLatitude()
                                                , location.getLongitude(), 1);

                                        binding.addressTv.setText(String.format("Address: %s", address.get(0).getAddressLine(0)));
                                        binding.CountryTv.setText(String.format("Country: %s", address.get(0).getCountryName()));
                                        binding.localityTv.setText(String.format("Locality: %s", address.get(0).getLocality()));
                                        binding.Latitude.setText(String.format("Latitude: %s", address.get(0).getLatitude()));
                                        binding.longitudeTv.setText(String.format("Longitude: %s", address.get(0).getLongitude()));
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }

                                }
                        }
                    });
        }else{
            askPermissions();
        }
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(requireActivity(),new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(requireActivity(), "Fine Location access not granted !", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch(sensorEvent.sensor.getType()){
            case Sensor.TYPE_LIGHT:
                float lightLevel = sensorEvent.values[0];
                binding.ambientLighttv.setText("Ambient Light: "+lightLevel +" lux");
                break;

            case Sensor.TYPE_ACCELEROMETER:
                float xAcc = sensorEvent.values[0];
                float yAcc = sensorEvent.values[1];
                float zAcc = sensorEvent.values[2];

                String res = String.valueOf(xAcc) + " <-> " + String.valueOf(yAcc) + " <-> " + String.valueOf(zAcc);
                binding.accelerometerTv.setText("Accelerometer(m/s2) :"+res);

                break;

            case Sensor.TYPE_GYROSCOPE:
                float xGyr = sensorEvent.values[0];
                float yGyr = sensorEvent.values[1];
                float zGyr = sensorEvent.values[2];

                String res1 = String.valueOf(xGyr) + "<->" + String.valueOf(yGyr) + "<->" + String.valueOf(zGyr);

                if(res1.isEmpty()){
                    binding.gyroscopeTv.setText("Gyroscope: None");
                }else{
                    binding.gyroscopeTv.setText("Gyroscope: "+res1);
                }
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                float xQuaternion = sensorEvent.values[0];
                float yQuaternion = sensorEvent.values[1];
                float zQuaternion = sensorEvent.values[2];
                float wQuaternion = sensorEvent.values[3];

                String res2 = String.valueOf(xQuaternion) + "<->" + String.valueOf(yQuaternion) + "<->" + String.valueOf(zQuaternion);
                binding.rotationTv.setText("Rotation :" + res2);
                break;

            case Sensor.TYPE_PROXIMITY:
                float dist = sensorEvent.values[0];
                binding.proximitytv.setText("Proximity: "+String.valueOf(dist));
                break;

            case Sensor.TYPE_PRESSURE:
                float pascal = -1;
                pascal = sensorEvent.values[0];

                if(pascal==-1){
                    binding.barometerTv.setText("Barometer: None.");
                }else
                binding.barometerTv.setText("Pressure: "+String.valueOf(pascal)+"mbar.");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        switch(sensor.getType()){
            case Sensor.TYPE_LIGHT:
                break;

            case Sensor.TYPE_ACCELEROMETER:
                break;

            case Sensor.TYPE_GYROSCOPE:
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                break;

            case Sensor.TYPE_PROXIMITY:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this,ambientLightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,gyroscopeSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,rotationSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,proximitySensorVar,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,pressureSensor,SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}