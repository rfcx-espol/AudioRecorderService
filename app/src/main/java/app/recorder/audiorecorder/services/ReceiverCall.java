package app.recorder.audiorecorder.services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import java.io.File;
import java.util.Arrays;

import app.recorder.audiorecorder.utils.FileUtils;
import app.recorder.audiorecorder.utils.Identifiers;

import static app.recorder.audiorecorder.utils.FileUtils.deleteAudio;
import static app.recorder.audiorecorder.utils.Identifiers.alarmManager;
import static app.recorder.audiorecorder.utils.Identifiers.audioDuration;
import static app.recorder.audiorecorder.utils.Identifiers.onService;
import static app.recorder.audiorecorder.utils.Identifiers.pendingIntent;
import static app.recorder.audiorecorder.utils.Identifiers.recordingAudioInterval;
import static app.recorder.audiorecorder.utils.Identifiers.setPreferencesApplications;

public class ReceiverCall extends BroadcastReceiver {
    //REINICIAR EL SERVICIO AL REINICIAR EL DISPOSITIVO
    @Override
    public void onReceive(Context context, Intent intent) {
        //INICIAR EL SERVICIO
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Identifiers.log = new File(Environment.getExternalStorageDirectory(), "Log - AudioRecorder.txt");
            if (!onService) {
                eraseLastFile();
                setPreferencesApplications(context);
                pendingIntent = PendingIntent.getService(context, 0,
                        new Intent(context, AudioRecorderService.class), PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + 1000,
                            recordingAudioInterval + audioDuration, pendingIntent);
                    FileUtils.escribirEnLog("INFO - SERVICIO REINICIADO DESPUÉS DE REINICIAR EL DISPOSITIVO");
                    Log.i("INFO", "SERVICIO REINICIADO DESPUÉS DE REINICIAR EL DISPOSITIVO");
                }
                onService = true;
            }
        }
    }

    //BORRAR EL ARCHIVO QUE QUEDÓ INCOMPLETO AL REINICIAR EL DISPOSITIVO
    public void eraseLastFile() {
        File[] files = new File(Environment.getExternalStorageDirectory().getPath()+ "/audios/").listFiles();
        Arrays.sort(files);
        deleteAudio(files[files.length - 1].getAbsolutePath());
    }

}