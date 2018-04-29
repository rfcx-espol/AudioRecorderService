package app.recorder.audiorecorder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Map;

/**
 * Created by Anny on 29/04/2018.
 */

public class Identifiers {
    public static int recordingAudioTime;
    //time in seconds of an audio duration
    public static int audioDuration;
    //These are the preferences
    public static SharedPreferences prefSettings;

    public static int samplingRate;

    public static void setPreferencesApplications(Context context){
        prefSettings = PreferenceManager.getDefaultSharedPreferences(context);
        //Default value: 1 minute y medio
        audioDuration = Integer.parseInt(prefSettings.getString("audio_duration","90"));
        //Default value: 2 minutes
        recordingAudioTime = Integer.parseInt(prefSettings.getString("recording_audio_interval", "120"));

        samplingRate = Integer.parseInt(prefSettings.getString("audio_sample_rate","48"));

        for ( Map.Entry<String,?> pref : prefSettings.getAll().entrySet() ) {
            Log.d("Utils inizialization" , pref.getKey() + ": " +pref.getValue().toString());
        }
    }
}
