package app.recorder.audiorecorder.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    //OBTIENE LA DIRECCIÓN DE LA CARPETA AUDIOS
    private static String sdCardFilesDir() {
        return (new StringBuilder()).append(Environment.getExternalStorageDirectory().toString()).append("/audios/").toString();
    }

    //CREAR LA CARPETA AUDIOS
    public static void createPrincipalFolder(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "audios");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("ERROR", "NO SE PUDO CREAR EL DIRECTORIO DE AUDIOS");
                FileUtils.escribirEnLog("ERROR - NO SE PUDO CREAR EL DIRECTORIO DE AUDIOS");
            }
        }
    }

    //RETORNAR LA RUTA COMPLETA DEL ARCHIVO DE AUDIO
    public static String getCaptureFilePath(long captureTimeStamp, String fileExtension) {
        return (new StringBuilder()).append(sdCardFilesDir()).append(captureTimeStamp).append(".").append(fileExtension).toString();
    }

    //ELIMINA UN AUDIO
    public static void deleteAudio(String audioPath){
        File file = new File(audioPath);
        if(file.exists()){
            file.delete();
            Log.i("INFO", "ARCHIVO INCOMPLETO BORRADO DESPUES DE REINICIAR: " + audioPath);
            FileUtils.escribirEnLog("INFO - ARCHIVO INCOMPLETO BORRADO DESPUES DE REINICIAR EL DISPOSITIVO: " + audioPath);
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
            return size >= 30000000;
        }
        return false;
    }

    //RETORNAR LA MEMORIA EXTERNA
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    //VERIFICA SI SE PUEDE ESCRIBIR EN LA MEMORIA EXTERNA
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static void escribirEnLog(String mensaje) {
        FileOutputStream fos;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        String fecha = sdf.format(new Date());
        String cadena = fecha + ": " + mensaje + "\n";
        try {
            fos = new FileOutputStream(Identifiers.log.getPath(), true);
            if(fos.getChannel().position() > 1000000) {
                fos.close();
                fos = new FileOutputStream(Identifiers.log.getPath(), false);
                fos.close();
                fos = new FileOutputStream(Identifiers.log.getPath(), true);
                fos.write(cadena.getBytes());
                fos.close();
            } else {
                fos.write(cadena.getBytes());
                fos.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("ERROR", "EL ARCHIVO LOG NO EXISTE");
            if(FileUtils.crearLog())
                Log.e("ERROR", "NO SE PUDO CREAR EL ARCHIVO LOG");
            else
                Log.i("INFO", "ARCHIVO LOG CREADO EXITOSAMENTE");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean crearLog(){
        if(FileUtils.externalMemoryAvailable() && FileUtils.isExternalStorageWritable() &&
                FileUtils.getAvailableExternalMemorySize()) {
            Identifiers.log = new File(Environment.getExternalStorageDirectory(), "Log - AudioRecorder.txt");
            Log.i("INFO", "ARCHIVO LOG CREADO EN: " + Identifiers.log.getPath());
            FileUtils.escribirEnLog("INFO - APLICACIÓN INICIADA");
            return false;
        }
        return true;
    }

}