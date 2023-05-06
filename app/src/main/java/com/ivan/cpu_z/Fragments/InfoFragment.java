package com.ivan.cpu_z.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ivan.cpu_z.R;
import com.ivan.cpu_z.Utils.DeviceInformationUtil;
import com.ivan.cpu_z.Utils.LiveDataHolder;
import com.ivan.cpu_z.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment {
    private FragmentInfoBinding binding;
    private LiveDataHolder holderViewModel;

    public InfoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        holderViewModel = new ViewModelProvider(requireActivity()).get(LiveDataHolder.class);

        //binding.androidVersionTv.setText(String.format("Android version:%s - %s", DeviceInformationUtil.getAndroidVersion(), DeviceInformationUtil.getAndroidSDK()));
        binding.cameraApertureTv.setText("Camera Aperture: f/"+DeviceInformationUtil.getCameraAperture(requireActivity()));
        binding.cameraPixelTv.setText("Camera Pixel: "+DeviceInformationUtil.getCameraPixels(requireActivity())+" MP");
        //binding.currentBatteryTv.setText("Battery level:"+DeviceInformationUtil.getBatteryLevel(requireActivity())+"%");
        binding.gpuTv.setText("Gpu info:"+DeviceInformationUtil.getGpuInformation(requireActivity())+" GHz");
        binding.manufacturerTv.setText("Manufacturer :"+DeviceInformationUtil.getDeviceManufacturer());
        binding.modelNameTv.setText("ModelName: "+DeviceInformationUtil.getModelName());
        binding.modelNumberTv.setText("ModelNumber: "+DeviceInformationUtil.getModelNumber());
        binding.processorTv.setText("Processor info: "+DeviceInformationUtil.getProcessorInformation());
        binding.ramTv.setText("Total Ram: "+DeviceInformationUtil.getRamStorage(requireActivity())+" MB");
        //binding.StorageTv.setText("Avail ExtStorage: "+DeviceInformationUtil.getRemainingExtStorage()+" MB");


        //setting the live data observers;

        final Observer<String>versionObserver = new Observer<String>(){
            @Override
            public void onChanged(String version) {
                   String res = String.format("Android version : %s",version);
                   binding.currentBatteryTv.setText(res);
            }
        };
        holderViewModel.getLiveAndroidVersion().observe(requireActivity(),versionObserver);



        final Observer<String>ExtStorage = new Observer<String>() {
            @Override
            public void onChanged(String storage) {
                String res = String.format("External storage : %s MB",storage);
                binding.StorageTv.setText(res);
            }
        };

        holderViewModel.getLiveExtStorage().observe(requireActivity(),ExtStorage);


        final Observer<String>batteryCharge = new Observer<String>() {
            @Override
            public void onChanged(String batteryLevel) {
                String res = String.format("Battery Level : %s percent",batteryLevel);
                binding.currentBatteryTv.setText(res);
            }
        };
        holderViewModel.getLiveBatteryStatus(requireActivity()).observe(requireActivity(),batteryCharge);
    }
}