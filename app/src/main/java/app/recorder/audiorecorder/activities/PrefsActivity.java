package app.recorder.audiorecorder.activities;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import app.recorder.audiorecorder.R;
import app.recorder.audiorecorder.services.AudioRecorderService;
import static app.recorder.audiorecorder.utils.Identifiers.setPreferencesApplications;
import static app.recorder.audiorecorder.utils.Identifiers.alarmManager;
import static app.recorder.audiorecorder.utils.Identifiers.pendingIntent;
import static app.recorder.audiorecorder.utils.Identifiers.recordingAudioInterval;
import static app.recorder.audiorecorder.utils.Identifiers.audioDuration;

public class PrefsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    //CREAR EL MENÚ DE PREFERENCIAS
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    //LISTENER QUE DETECTA CAMBIOS EN LAS PREFERENCIAS
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("METODO CHANGE", "CAMBIÓ LA CONFIGURACIÓN: " + key);
        if (key.equals("audio_duration") || key.equals("recording_audio_interval")) {
            Log.d("METODO CHANGE", "ENTRÓ");
            stopService(new Intent(this, AudioRecorderService.class));
            setPreferencesApplications(getApplicationContext());
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                    recordingAudioInterval + audioDuration , pendingIntent);
            Log.d("ALARMA", "ALARMA CAMBIADA");
        }
    }

}