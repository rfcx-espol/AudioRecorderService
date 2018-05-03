package app.recorder.audiorecorder.activities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    //REINICIAR LA APP MIENTRAS NO ESTÉ GRABANDO
    public void reboot(){
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
        boolean executingService= isExecuting();
        Log.d("EJECUTANDO",String.valueOf(executingService));
        if (i != null && !executingService) {
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    //VERIFICAR LA EJECUCIÓN DEL SERVICIO
    private boolean isExecuting() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i++) {
            if(procInfos.get(i).processName.equals("app.recorder.audiorecorderservice")) {
                return true; //EL SERVICIO ESTÁ ACTIVO
            }
        }
        return false; //EL SERVICIO ESTÁ INACTIVO
    }

}