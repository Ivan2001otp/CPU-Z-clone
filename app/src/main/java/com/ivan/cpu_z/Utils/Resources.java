package com.ivan.cpu_z.Utils;

import android.content.Context;
import android.hardware.SensorManager;

public class Resources {

    public static SensorManager buildSensorManager(Context context){

        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }
}
