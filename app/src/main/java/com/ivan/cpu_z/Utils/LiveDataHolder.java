package com.ivan.cpu_z.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveDataHolder extends ViewModel {
    private MutableLiveData<String>batteryPower = new MutableLiveData<>();
    private MutableLiveData<String>remainingExtStorage = new MutableLiveData<>();
    private MutableLiveData<String>liveAndroid = new MutableLiveData<>();

    public LiveData<String> getLiveBatteryStatus(Context context){
        String batteryLevel = DeviceInformationUtil.getBatteryLevel(context);
        batteryPower.setValue(batteryLevel);
        return batteryPower;
    }

    public LiveData<String> getLiveExtStorage(){
        String extStorage = DeviceInformationUtil.getRemainingExtStorage();
        remainingExtStorage.setValue(extStorage);
        return remainingExtStorage;
    }

    public LiveData<String> getLiveAndroidVersion(){
        String version = DeviceInformationUtil.getAndroidVersion();
        String sdk=DeviceInformationUtil.getAndroidSDK();

        String result = String.format("%s - %s",version,sdk);

        liveAndroid.setValue(result);
        return liveAndroid;
    }

}
