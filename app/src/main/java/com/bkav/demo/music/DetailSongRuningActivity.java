package com.bkav.demo.music;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetailSongRuningActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mHinhDanhSach;
    private ImageView mPopupMenu;
    private ImageView mLike;
    private ImageView mPrevious;
    private ImageView mPlayStart;
    private ImageView mNext;
    private ImageView mDisLike;
    private ImageView mHinhCaSy;
    private TextView mTenCaSy;
    private TextView mTenBaiHat;
    private TextView mTimeStart;
    private TextView mTimeAll;
    private SeekBar mSeekBar;
    private Intent intent;
    private Bundle bundle;
    private int mLocaltionSong;
    private ArrayList<String> arrList;
    private Uri uri;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_song_runing);
        intent = getIntent();
        initView();
        getDataPush();
        backHome();

    }

    private void initView() {
        mHinhDanhSach = (ImageView) findViewById(R.id.danhsach);
        mPopupMenu = (ImageView) findViewById(R.id.popup_song);
        mLike = (ImageView) findViewById(R.id.like);
        mDisLike = (ImageView) findViewById(R.id.dis_like);
        mPrevious = (ImageView) findViewById(R.id.previous);
        mPlayStart = (ImageView) findViewById(R.id.pause_play);
        mNext = (ImageView) findViewById(R.id.next);
        mHinhCaSy = (ImageView) findViewById(R.id.hinh_casy);

        mTenCaSy = (TextView) findViewById(R.id.tencasy);
        mTenBaiHat = (TextView) findViewById(R.id.tenbaihat);
        mTimeStart = (TextView) findViewById(R.id.time_start);
        mTimeAll = (TextView) findViewById(R.id.time_all);

        mSeekBar = (SeekBar) findViewById(R.id.seekbar);

        mPlayStart.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);


    }

    private void backHome() {
        mHinhDanhSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ibackhome = new Intent(DetailSongRuningActivity.this, MainActivity.class);
                startActivity(ibackhome);
            }
        });

    }

    private void getDataPush() {
        bundle = intent.getBundleExtra("dulieu");
        mLocaltionSong = bundle.getInt("vitri");
        arrList = bundle.getStringArrayList("tenbai");
        playSongLocaltion();
        updateSong();
        updateSong();
        runSeekbar();
        allTimeSong();


    }

    private void runSeekbar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                if (mTimeStart.equals(simpleDateFormat.format(mediaPlayer.getCurrentPosition() + ""))) {
                    mLocaltionSong += 1;
                    playSongLocaltion();
                    mPlayStart.setImageResource(R.drawable.ic_play_black);
                    allTimeSong();
                    updateTime();


                }
            }
        });


    }

    private void playSongLocaltion() {
        uri = Uri.parse(arrList.get(mLocaltionSong).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        updateSong();

    }

    private void allTimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        mTimeAll.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        mSeekBar.setMax(mediaPlayer.getDuration());


    }

    private void updateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                mTimeStart.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }, 100);


    }

    private void updateSong() {
        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(arrList.get(mLocaltionSong));

            String tenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String ten = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            mTenBaiHat.setText(ten);
            mTenCaSy.setText(tenAlbum);
            mHinhCaSy.setImageResource(R.drawable.anhtho);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous:
                mLocaltionSong -= 1;
                if (mLocaltionSong < 0) {
                    mLocaltionSong = arrList.size() - 1;
                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    playSongLocaltion();
                    //mPrevious.setImageResource(R.drawable.ic_fab_play_btn_normal);
                    allTimeSong();
                    updateTime();
                }
                break;

            case R.id.pause_play:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mPlayStart.setImageResource(R.drawable.ic_fab_play_btn_normal);
                } else {
                    mediaPlayer.start();

                }
                playSongLocaltion();
               // mPlayStart.setImageResource(R.drawable.ic_fab_play_btn_normal);

                allTimeSong();
                updateTime();
                break;

            case R.id.next:
                mLocaltionSong += 1;
                if (mLocaltionSong > arrList.size() - 1) {
                    mLocaltionSong = 0;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                playSongLocaltion();
               // mNext.setImageResource(R.drawable.ic_fab_play_btn_normal);
                allTimeSong();
                updateTime();
                break;


        }
    }
}





