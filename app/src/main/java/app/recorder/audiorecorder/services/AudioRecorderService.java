package app.recorder.audiorecorder.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import app.recorder.audiorecorder.utils.FileUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AudioRecorderService extends Service implements MediaRecorder.OnInfoListener {
    MediaRecorder mediaRecorder;
    public static PowerManager.WakeLock wakeLock;
    private final IBinder mBinder = new LocalBinder();

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

    @Override
    public void onCreate(){
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        wakeLock.acquire();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
        wakeLock.release();
        Intent intent = new Intent("services.ReceiverCall");
        sendBroadcast(intent);
    }

    //ESTABLECE EL OUTPUT DEL AUDIO
    public String mediaRecorderSetOutPutFile(){
        long captureTimeStamp = System.currentTimeMillis();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH,mm,ss");
        String formattedDate = df.format(c);
        String captureDir = FileUtils.createAudiosFilesDir(captureTimeStamp);
        String inputPath = FileUtils.getCaptureFilePath(captureDir,formattedDate,"m4a");
        mediaRecorder.setOutputFile(inputPath);
        return inputPath;
    }

    public void stopRecording(){
        mediaRecorder.stop();
        Log.d("ATENCION", "GRABACION TERMINADA");
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        Log.d("ATENCION", "ESPERANDO PARA PROXIMA GRABACION..");
    }

    public void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(2);
        mediaRecorder.setAudioEncodingBitRate(448000);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setMaxDuration(90000);
        mediaRecorder.setOnInfoListener(this);
        String sourceFile = mediaRecorderSetOutPutFile();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.d("ATENCION", "GRABANDO");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            stopRecording();
        }
    }

}