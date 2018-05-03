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

    //ELIMINA UN AUDIO
    public static void deleteAudio(String audioPath){
        File file = new File(audioPath);
        if(file.exists()){
            file.delete();
            Log.d("DELETED", audioPath);
        }
    }

    //VERIFICAR SI HAY ESPACIO EN LA MEMORIA EXTERNA
    public static boolean getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long size = availableBlocks * blockSize;
            Log.i("Utils", "Available size in bytes: " + size);
            return size >= 30000000;
        }
        return false;
    }

    //RETORNAR LA MEMORIA EXTERNA
    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

}