package app.recorder.audiorecorder.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverCall extends BroadcastReceiver {
    //REINICIAR EL SERVICIO AL REINICIAR EL DISPOSITIVO
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SERVICIO", "REINICIANDO EL SERVICIO");
        Intent SERVICE_INTENT_AUDIO_RECORDER = new Intent(context, AudioRecorderService.class);
        context.startService(SERVICE_INTENT_AUDIO_RECORDER);
    }
}