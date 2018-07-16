package app.recorder.audiorecorder.activities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import app.recorder.audiorecorder.R;
import app.recorder.audiorecorder.services.AudioRecorderService;
import app.recorder.audiorecorder.utils.FileUtils;
import static app.recorder.audiorecorder.utils.Identifiers.alarmManager;
import static app.recorder.audiorecorder.utils.Identifiers.onService;
import static app.recorder.audiorecorder.utils.Identifiers.pendingIntent;
import static app.recorder.audiorecorder.utils.Identifiers.recordingAudioInterval;
import static app.recorder.audiorecorder.utils.Identifiers.audioDuration;
import static app.recorder.audiorecorder.utils.Identifiers.setPreferencesApplications;

public class MainActivity extends AppCompatActivity {

    //CREAR LA ACTIVIDAD PRINCIPAL DE LA APP
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //CREAR LA CARPETAS AUDIOS
        FileUtils.createPrincipalFolder();

        //INICIAR EL SERVICIO
        if(!onService) {
            createAlarm();
        }
        setContentView(app.recorder.audiorecorder.R.layout.activity_main);
    }

    //CREAR EL MENÚ PRINCIPAL
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(app.recorder.audiorecorder.R.menu.main_menu, menu);
        return true;
    }

    //CREAR EL MENÚ DE OPCIONES
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case app.recorder.audiorecorder.R.id.menu_prefs:
                startActivity(new Intent(this, PrefsActivity.class));
                break;
            case app.recorder.audiorecorder.R.id.menu_reboot:
                reboot();
                break;
            case app.recorder.audiorecorder.R.id.menu_screenshot:
                break;
        }
        return true;
    }

    private void createAlarm() {
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        setPreferencesApplications(getApplicationContext());

        pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                new Intent(this, AudioRecorderService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                    recordingAudioInterval + audioDuration , pendingIntent);
            Log.d("ALARMA", "ALARMA CREADA");
        }
        onService = true;
    }

    //REINICIAR EL SERVICIO
    public void reboot(){
        Intent intentAudioRecord = new Intent(this, AudioRecorderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                new Intent(this, AudioRecorderService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AudioRecorderService.class.getName().equals(service.service.getClassName())) {
                alarmManager.cancel(pendingIntent);
                stopService(intentAudioRecord);
                break;
            }
        }
        createAlarm();
        Toast.makeText(this, "SERVICIO REINICIADO", Toast.LENGTH_SHORT).show();
    }

}