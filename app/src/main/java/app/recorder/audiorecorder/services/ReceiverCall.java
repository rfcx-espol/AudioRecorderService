package app.recorder.audiorecorder.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import static app.recorder.audiorecorder.utils.Identifiers.SERVICE_INTENT_AUDIO_RECORDER;

public class ReceiverCall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SERVICIO", "REINICIANDO EL SERVICIO");
        SERVICE_INTENT_AUDIO_RECORDER = new Intent(context, AudioRecorderService.class);
        context.startService(SERVICE_INTENT_AUDIO_RECORDER);
    }
}