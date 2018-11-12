package app.recorder.audiorecorder.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import app.recorder.audiorecorder.utils.FileUtils;

public class BatteryLevelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //DETECTAR UN NIVEL DE BATERÍA BAJO
        if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
            FileUtils.escribirEnLog("INFO - NIVEL DE BATERÍA BAJO");
        } else if(intent.getAction().equals("android.intent.action.BATTERY_OKAY")) {
            FileUtils.escribirEnLog("INFO - NIVEL DE BATERÍA VUELVE A UN NIVEL ACEPTABLE");
        }

    }

}
