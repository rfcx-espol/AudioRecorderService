package app.recorder.audiorecorder.activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;
import app.recorder.audiorecorder.services.AudioRecorderService;
import app.recorder.audiorecorder.utils.FileUtils;
import static app.recorder.audiorecorder.utils.Identifiers.onService;

public class MainActivity extends AppCompatActivity {

    //CREAR LA ACTIVIDAD PRINCIPAL DE LA APP
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //CREAR LA CARPETA AUDIOS
        FileUtils.createPrincipalFolder();

        //INICIAR EL SERVICIO
        if(!onService) {
            startService(new Intent(this,AudioRecorderService.class));
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