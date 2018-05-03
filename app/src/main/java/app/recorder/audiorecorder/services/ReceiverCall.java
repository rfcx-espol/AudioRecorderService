package app.recorder.audiorecorder.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import java.io.File;
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
        Log.i("SERVICIO", "REINICIANDO EL SERVICIO");

        //INICIAR EL SERVICIO
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (!onService) {
                setPreferencesApplications(context);
                //BORRAR EL ARCHIVO QUE QUEDÓ INCOMPLETO AL REINICIAR EL DISPOSITIVO
                File[] files = new File(Environment.getExternalStorageDirectory().getPath()+ "/audios/").listFiles();
                File[] f1 = files[files.length - 1].listFiles();
                File[] f2 = f1[f1.length - 1].listFiles();
                Log.d("BORRAR", "ARCHIVO INCOMPLETO BORRADO DESPUES DE REINICIAR");
                deleteAudio(f2[0].getAbsolutePath());
                pendingIntent = PendingIntent.getService(context, 0,
                        new Intent(context, AudioRecorderService.class), PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                            recordingAudioInterval + audioDuration, pendingIntent);
                    Log.d("ALARMA", "ALARMA CREADA DESPUÉS DE REINICIAR EL DISPOSITIVO");
                }
                onService = true;
            }
        }
    }
}