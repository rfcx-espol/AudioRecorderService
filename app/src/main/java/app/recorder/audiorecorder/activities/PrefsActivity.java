package app.recorder.audiorecorder.activities;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
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
    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    Preference singlePref = preferenceGroup.getPreference(j);
                    Log.d("pref", String.valueOf(singlePref));
                    updatePreference(singlePref, singlePref.getKey());

                }
            } else {
                updatePreference(preference, preference.getKey());
            }
        }
    }

    //LISTENER QUE DETECTA CAMBIOS EN LAS PREFERENCIAS
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(findPreference(key), key);
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
    private void updatePreference(Preference preference, String key) {
        if (preference == null) return;
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
            return;
        }
        SharedPreferences sharedPrefs = getPreferenceManager().getSharedPreferences();
        if(key.equals("recording_audio_interval")){
            preference.setSummary(sharedPrefs.getString(key, "Default")+" Segundos");
        }else if(key.equals("audio_duration")){
            preference.setSummary(sharedPrefs.getString(key, "Default")+ " Segundos");
        }else if(key.equals("audio_encode_bitrate")){
            preference.setSummary(sharedPrefs.getString(key, "Default")+" Kbps \n" +
                    "Nota: 16 kbps a 256 kbps (mono) / 16 a 448 kbps (stereo)");
        }
        else{
            preference.setSummary(sharedPrefs.getString(key, "Default"));
        }


    }

}