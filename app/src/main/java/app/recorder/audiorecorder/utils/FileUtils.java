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
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        String date;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(captureTimeStamp);
        simpleDateFormat = new SimpleDateFormat(format);
        date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    //OBTIENE LA DIRECCIÓN DE LA CARPETA AUDIOS
    public static String sdCardFilesDir() {
        return (new StringBuilder()).append(Environment.getExternalStorageDirectory().toString()).append("/audios/").toString();
    }

    //OBTIENE LA DIRECCIÓN DE LAS CARPETAS DENTRO DE AUDIOS
    public static String audiosFilesDir(long timeStamp) {
        String year=formatFolder("MM-yyyy",timeStamp);
        String day=formatFolder("dd a",timeStamp);
        String folders=year+"/"+day+"/";
        return (new StringBuilder()).append(sdCardFilesDir()).append(folders).toString();
    }

    //CREA LAS CARPETAS DENTRO DE AUDIOS SI NO EXISTE RETORNA LA DIRECCIÓN
    public static String createAudiosFilesDir(long timeStamp){
        File file = new File(audiosFilesDir(timeStamp));
        if(!file.exists()){
            if(!file.mkdirs()){
                Log.d("App", "failed to create directory");
            }
        }
        return file.getAbsolutePath();
    }

    //CREA LA CARPETA AUDIOS SI NO EXISTE
    public static void createPrincipalFolder(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "audios");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
    }

    //RETORNA EL FORMATO DEL AUDIO
    public static String getCaptureFilePath(String captureDir, String formattedDate, String fileExtension) {
        return (new StringBuilder()).append(captureDir).append("/").append(formattedDate).append(".").append(fileExtension).toString();
    }

    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static boolean getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long size = availableBlocks * blockSize;
            Log.i("Utils", "Available size in bytes: " + size);
            if (size < 10485760){ //10MB
                return false;
            }else{
                return true;
            }
        } else {
            return false;
        }
    }

}