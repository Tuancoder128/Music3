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
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vst on 03/05/2018.
 */

public class ServiceMusic extends Service {


    // Bkav QuangLH Review 20180507: khong de cac View duoi service
    // --> Dao tao anh em ve BroadcastReceiver
    //
    // Bkav QuangLH Review 20180507: khong de public khong can thiet.

    private final IBinder mBinder = new MyBinder();
    public ArrayList<String> mArrayListSong;
    public Bundle mBundler;
    public int mLocalSong;
    private int mLocalSongRuning;
    public static MediaPlayer mMediaPlayer;
    private Uri mUriSong;


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void playMusic(String mLocalSong) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mLocalSong);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getLocaltionSong(int local) {
        mLocalSong = local;

    }

    public void getArrayListSong(ArrayList<String> arraylistbh) {
        mArrayListSong = arraylistbh;
    }

    public void clickPausePlayMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
    }

    public void previousSong() {
        mLocalSong -= 1;
        if (mLocalSong < 0) {
            mLocalSong = mArrayListSong.size() - 1;
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            playSongLocaltion();
        }


    }

    public void nextSong() {
        mLocalSong += 1;
        if (mLocalSong > mArrayListSong.size() - 1) {
            mLocalSong = 0;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        playSongLocaltion();
    }

    public void repeatSong() {

    }

    public void playSongLocaltion() {
        mUriSong = Uri.parse(mArrayListSong.get(mLocalSong).toString());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), mUriSong);
        mMediaPlayer.start();

    }

    public class MyBinder extends Binder {
        ServiceMusic getService() {
            return ServiceMusic.this;
        }
    }

}
















































