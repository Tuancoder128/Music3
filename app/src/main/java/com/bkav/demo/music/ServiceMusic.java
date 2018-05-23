package com.bkav.demo.music;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
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
    // Bkav QuangLH Review 20180507: khong de public khong can thiet.

    private final IBinder mBinder = new MyBinder();
    public ArrayList<String> mArrayListSong;
    public Bundle mBundler;
    public int mLocalSong;
    public String mLocalSongRuning;
    public static MediaPlayer mMediaPlayer;
    private Uri mUriSong;
    private MyBroastReceiver mMyBroastReceiver;
    private Intent mIntentBroadCast;
    public static final String VALUE_DATA_INTENT = "ValueDataIntent";
    private static final String NAME_TITLE = "tenbaihat";
    private static final String NAME_ARTIST = "tenalbum";
    private static final String DATA_BUNDLER = "dataBunder";
    private IntentFilter mIntentFilter;
    public String mTenBaiHat;
    public String mTenAlbum;


    @Override
    public void onCreate() {

        mMyBroastReceiver = new MyBroastReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(VALUE_DATA_INTENT);
        registerReceiver(mMyBroastReceiver, mIntentFilter);
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
        unregisterReceiver(mMyBroastReceiver);
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
        autoPLayNextSong();

    }

    public void getLocaltionSong(int mLocal) {
        mLocalSong = mLocal;

    }

    public void getArrayListSong(ArrayList<String> mArrayList) {
        mArrayListSong = mArrayList;
    }

    public void previousSong() {
        mLocalSong -= 1;
        if (mLocalSong < 0) {
            mLocalSong = mArrayListSong.size() - 1;
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            playSongLocaltion();
            autoPLayNextSong();

        }
    }

    public void nextSong() {
        mLocalSong += 1;
        if (mLocalSong > mArrayListSong.size() - 1) {
            mLocalSong = 0;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        playSongLocaltion();
        autoPLayNextSong();
    }


    public void playSongLocaltion() {
        mUriSong = Uri.parse(mArrayListSong.get(mLocalSong).toString());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), mUriSong);
        mMediaPlayer.start();
        // TODO : get image and time from song is running

        autoPLayNextSong();
    }

    public void autoPLayNextSong() {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mLocalSong = mLocalSong + 1;
                getNameSong();
                //TODO send progerss and all time song
                mIntentBroadCast = new Intent();
                mBundler = new Bundle();
                mBundler.putString(NAME_ARTIST, mTenAlbum);
                mBundler.putString(NAME_TITLE, mTenBaiHat);
                mIntentBroadCast.putExtra(DATA_BUNDLER,mBundler);
                mIntentBroadCast.setAction(VALUE_DATA_INTENT);
                sendBroadcast(mIntentBroadCast);
                if (mLocalSong > mArrayListSong.size() - 1) {
                    mLocalSong = 0;
                    playSongLocaltion();
                } else {
                    playSongLocaltion();
                }

            }
        });
    }

    private void getNameSong() {

        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mArrayListSong.get(mLocalSong));
            mTenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            mTenBaiHat = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);


        }
    }


    public class MyBinder extends Binder {
        ServiceMusic getService() {
            return ServiceMusic.this;
        }
    }
}
















































