package app.recorder.audiorecorder.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.Map;

public class Identifiers {
    //these is the intent of service audio recorder, initilized  in the boot receiver
    public static Intent SERVICE_INTENT_AUDIO_RECORDER;
    //time in seconds in which is intended to recorder an audio
    public static int RECORDING_AUDIO_TIME;
    //time in seconds of an audio duration
    public static int AUDIO_DURATION;
    //boolean to determine if recorder or not audios
    public static boolean IS_ON_SERVICE;
    //Audio format 3gp or m4a
    public static String AUDIO_FORMAT;

    //These are the preferences
    public static SharedPreferences PREFS_SETTINGS;

    public static void setPreferencesApplications(Context context){
        PREFS_SETTINGS = PreferenceManager.getDefaultSharedPreferences(context);
        //Default value: 1 minute y medio
        AUDIO_DURATION = Integer.parseInt(PREFS_SETTINGS.getString("audio_duration","90"));
        //Default value: 2 minutes
        RECORDING_AUDIO_TIME = Integer.parseInt(PREFS_SETTINGS.getString("recording_audio_time", "120"));
        //Default value: false
        IS_ON_SERVICE = PREFS_SETTINGS.getBoolean("is_on_service", false);
        //Default value: m4a
        AUDIO_FORMAT = PREFS_SETTINGS.getString("audio_format","m4a");

        for ( Map.Entry<String,?> pref : PREFS_SETTINGS.getAll().entrySet() ) {
            Log.d("Utils inizialization" , pref.getKey() + ": " +pref.getValue().toString());
        }
    }
}