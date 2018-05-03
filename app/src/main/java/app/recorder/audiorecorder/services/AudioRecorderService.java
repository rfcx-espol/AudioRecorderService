package app.recorder.audiorecorder.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import java.io.IOException;
import app.recorder.audiorecorder.utils.FileUtils;
import static app.recorder.audiorecorder.utils.FileUtils.deleteAudio;
import static app.recorder.audiorecorder.utils.Identifiers.setPreferencesApplications;
import static app.recorder.audiorecorder.utils.Identifiers.audioDuration;
import static app.recorder.audiorecorder.utils.Identifiers.recordingAudioInterval;
import static app.recorder.audiorecorder.utils.Identifiers.samplingRate;
import static app.recorder.audiorecorder.utils.Identifiers.bitRate;
import static app.recorder.audiorecorder.utils.Identifiers.channels;
import static app.recorder.audiorecorder.utils.Identifiers.format;

public class AudioRecorderService extends Service implements MediaRecorder.OnInfoListener {
    MediaRecorder mediaRecorder;
    MediaRecorder.OnInfoListener listener = this;
    public static PowerManager.WakeLock wakeLock;
    private final IBinder mBinder = new LocalBinder();
    public static String audioPath;

    public AudioRecorderService() {}

    public class LocalBinder extends Binder {
        public AudioRecorderService getService() {
            return AudioRecorderService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //CREAR EL SERVICIO DE GRABACIÓN DE AUDIOS
    @Override
    public void onCreate(){
        super.onCreate();
        //INICIALIZAR LAS CONFIGURACIONES
        setPreferencesApplications(getApplicationContext());

        //MANTENER ENCENDIDO EL CPU DEL CELULAR AL APAGAR LA PANTALLA
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        wakeLock.acquire();
    }

    //CREAR EL SERVICIO DE GRABACIÓN DE AUDIOS
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return Service.START_STICKY;
    }

    //DESTRUIR EL SERVICIO DE GRABACIÓN DE AUDIOS
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaRecorder != null){
            stopRecording();
            deleteAudio(audioPath);
        }
        wakeLock.release();
    }

    //CREAR EL ARCHIVO DE SALIDA DEL AUDIO
    public void mediaRecorderSetOutPutFile(){
        long captureTimeStamp = System.currentTimeMillis();
        String captureDir = FileUtils.createAudiosFilesDir(captureTimeStamp);
        audioPath = FileUtils.getCaptureFilePath(captureDir,captureTimeStamp,format);
        mediaRecorder.setOutputFile(audioPath);
    }

    //DETENER LA GRABACIÓN Y ACTUALIZAR LAS PREFERENCIAS
    public void stopRecording(){
        mediaRecorder.stop();
        Log.d("GRABACIÓN", "LA GRABACIÓN TERMINÓ");
        Log.d("GRABACIÓN", "ESPERANDO " + String.valueOf(recordingAudioInterval / 1000) +
                " SEGUNDOS PARA LA PRÓXIMA GRABACIÓN");
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        setPreferencesApplications(getApplicationContext());
    }

    //INICIAR LA GRABACIÓN DE AUDIOS SI HAY ESPACIO DISPONIBLE
    public void startRecording() {
        if(FileUtils.getAvailableExternalMemorySize()){
            Log.d("GRABAR", "SI HAY ESPACIO PARA GRABAR");
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            if(format.equals("m4a")) {
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            } else {
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            }
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioChannels(channels);
            mediaRecorder.setAudioEncodingBitRate(bitRate);
            mediaRecorder.setAudioSamplingRate(samplingRate);
            mediaRecorder.setMaxDuration(audioDuration);
            mediaRecorder.setOnInfoListener(listener);
            mediaRecorderSetOutPutFile();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Log.d("GRABACIÓN","GRABANDO DURANTE "+ String.valueOf(audioDuration / 1000) + " SEGUNDOS");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("GRABAR", "NO HAY ESPACIO PARA GRABAR");
        }
    }

    //DETECTAR CUANDO SE LLEGA A LA DURACIÓN MÁXIMA DEL AUDIO
    public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            stopRecording();
        }
    }

}