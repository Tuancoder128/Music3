package com.bkav.demo.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by vst on 03/05/2018.
 */

public class ServiceMusic extends Service {


    private final IBinder mBinder = new MyBinder();
    private ArrayList<ThongTinBaiHat> arrayList;
    private ImageView mHinhDanhSach;
    private ImageView mPopupMenu;
    private ImageView mLike;
    private ImageView mPrevious;
    private ImageView mPlayStart;
    private ImageView mNext;
    private ImageView mDisLike;
    // Bkav QuangLH Review 20180507: khong de cac View duoi service
    // --> Dao tao anh em ve BroadcastReceiver
    //
    // Bkav QuangLH Review 20180507: khong de public khong can thiet.
    private ImageView mHinhCaSy;
    private TextView mTenCaSy;
    private TextView mTenBaiHat;
    private TextView mTimeStart;
    private TextView mTimeAll;
    private SeekBar mSeekBar;
    private Intent intent;
    public Bundle bundle;
    private Button mClickStart;
    private int mLocaltionSong;
    private ArrayList<String> arrList;
    private ArrayList<String> mPath;
    private Uri uri;
    public static MediaPlayer mediaPlayer; // Bkav QuangLH Review 20180507: convention. Ra soat va sua het.
    private int time, t = 0;
    private static final int MY_RESULT_CODE_1 = 100;
    private static final int MY_RESULT_CODE_5 = 500;
    private static final int MY_RESULT_CODE_1000 = 1000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getAllListMusic();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public class MyBinder extends Binder {
        ServiceMusic getService() {
            return ServiceMusic.this;
        }
    }

    public void nextSong() {

        mLocaltionSong += 1;
        if (mLocaltionSong > arrList.size() - 1) {
            mLocaltionSong = 0;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        playSongLocaltion1();
        completionListener();

    }

    public void previousSong() {
        mLocaltionSong -= 1;
        if (mLocaltionSong < 0) {
            mLocaltionSong = arrList.size() - 1;
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            playSongLocaltion1();
            completionListener();

        }
    }


    public void playpauseSong() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else {
            mediaPlayer.start();
        }


    }


    private void completionListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mLocaltionSong < (arrList.size() - 1)) {
                    playsong(mLocaltionSong + 1);
                    mLocaltionSong = mLocaltionSong + 1;

                }
            }
        });
    }

    public void playsong(int mLocaltionSong) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(arrList.get(mLocaltionSong));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setLocalSong(String localsong) throws IOException {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }

        mediaPlayer =  new MediaPlayer();
        mediaPlayer.setDataSource(localsong);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }




    public void playSongLocaltion1() {
        uri = Uri.parse(arrList.get(mLocaltionSong).toString());
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
    }

    private void getAllListMusic() {
        mPath = new ArrayList<>();
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music";
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String s = files[i].getName();
            if (s.endsWith(".mp3")) {
                mPath.add(files[i].getAbsolutePath());
            }
        }
    }


}


















