package app.recorder.audiorecorder.activities;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import app.recorder.audiorecorder.services.AudioRecorderService;
import app.recorder.audiorecorder.utils.FileUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //CREAR LA CARPETA AUDIOS SI NO EXISTE
        FileUtils.createPrincipalFolder();

        //INICIAR LA ALARMA
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0,
                new Intent(this, AudioRecorderService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                150000 , pendingIntent);
        setContentView(app.recorder.audiorecorder.R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(app.recorder.audiorecorder.R.menu.main_menu, menu);
        return true;
    }

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
            case app.recorder.audiorecorder.R.id.menu_test_i2c:
                break;
        }
        return true;
    }

    //REINICIAR LA APP MIENTRAS NO ESTÉ GRABANDO
    public void reboot(){
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
        boolean excecutingService= isExecuting("app.recorder.audiorecorderservice");
        Log.d("EJECUTANDO",String.valueOf(excecutingService));
        if (i != null && !excecutingService) {
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    private boolean isExecuting(String packagename) {
        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i++) {
            if(procInfos.get(i).processName.equals(packagename)) {
                return true; //ESTÁ ACTIVA
            }
        }
        return false; //ESTÁ CERRADA
    }

}