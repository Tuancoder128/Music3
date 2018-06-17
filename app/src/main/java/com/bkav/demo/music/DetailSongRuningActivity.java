package com.bkav.demo.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetailSongRuningActivity extends AppCompatActivity {
    private ImageView mHinhDanhSach;
    private ImageView mPopupMenu;
    private ImageView mLike;
    private ImageView mPrevious;
    private ImageView mPlayStart;
    private ImageView mNext;
    private ImageView mDisLike;
    public ImageView mHinhCaSy;
    public TextView mTenCaSy;
    public TextView mTenBaiHat;
    public boolean iboundService = false;
    public ServiceMusic mserviceMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_song_runing);

        initView();


    }

    private void initView() {
        mHinhDanhSach = (ImageView) findViewById(R.id.danhsach);
        mPopupMenu = (ImageView) findViewById(R.id.popup_song);
        mPlayStart = (ImageView) findViewById(R.id.pause_play);
        mNext = (ImageView) findViewById(R.id.next);
        mHinhCaSy = (ImageView) findViewById(R.id.hinh_casy);
        mTenCaSy = (TextView) findViewById(R.id.tencasy);
        mTenBaiHat = (TextView) findViewById(R.id.tenbaihat);


    }
}

















