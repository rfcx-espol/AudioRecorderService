package app.recorder.audiorecorder.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileUtils {

    //RETORNA EL FORMATO DE LA CARPETA CON FECHA
    private static String formatFolder(String format, long captureTimeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(captureTimeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(calendar.getTime());
    }

    //OBTIENE LA DIRECCIÓN DE LA CARPETA AUDIOS
    private static String sdCardFilesDir() {
        return (new StringBuilder()).append(Environment.getExternalStorageDirectory().toString()).append("/audios/").toString();
    }

    //OBTENER LA DIRECCIÓN DE LAS CARPETAS DENTRO DE AUDIOS
    private static String audiosFilesDir(long timeStamp) {
        String year=formatFolder("MM-yyyy",timeStamp);
        String day=formatFolder("dd a",timeStamp);
        String folders=year+"/"+day+"/";
        return (new StringBuilder()).append(sdCardFilesDir()).append(folders).toString();
    }

    //CREAR LAS CARPETAS DENTRO DE AUDIOS SI NO EXISTE RETORNA LA DIRECCIÓN
    public static String createAudiosFilesDir(long timeStamp){
        File file = new File(audiosFilesDir(timeStamp));
        if(!file.exists()){
            if(!file.mkdirs()){
                Log.d("App", "failed to create directory");
            }
        }
        return file.getAbsolutePath();
    }

    //CREAR LA CARPETA AUDIOS
    public static void createPrincipalFolder(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "audios");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
    }

    //RETORNAR LA RUTA COMPLETA DEL ARCHIVO DE AUDIO
    public static String getCaptureFilePath(String captureDir, long captureTimeStamp, String fileExtension) {
        return (new StringBuilder()).append(captureDir).append("/").append(captureTimeStamp).append(".").append(fileExtension).toString();
    }

}