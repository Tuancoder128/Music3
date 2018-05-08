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

public class DetailSongRuningActivity extends AppCompatActivity implements View.OnClickListener {
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
    private TextView mTimeStart;
    private TextView mTimeAll;
    private SeekBar mSeekBar;
    private Intent intent;
    public Bundle bundle;
    public int mLocaltionSong;
    public ArrayList<String> arrList;
    public Uri uri;
    public MediaPlayer mediaPlayer;
    private int time, t = 0;
    private static final int MY_RESULT_CODE_1 = 100;
    private static final int MY_RESULT_CODE_5 = 500;
    private static final int MY_RESULT_CODE_1000 = 1000;
    public boolean iboundService = false;
    public ServiceMusic mserviceMusic ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_song_runing);
        intent = getIntent();
        Intent intent = new Intent(DetailSongRuningActivity.this,ServiceMusic.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

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
                Intent i = new Intent(DetailSongRuningActivity.this, MainActivity.class);
                i.putExtra("time", t);
                setResult(000, i);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                finish();
            }
        });

    }

    private void getDataPush() {
        bundle = intent.getBundleExtra("dulieu");
        mLocaltionSong = bundle.getInt("vitri");
        arrList = bundle.getStringArrayList("tenbai");
        time = bundle.getInt("thoigian");
        playSongLocaltion();
        updateSong();
        updateTime();
        runSeekbar();
        allTimeSong();

    }


    private void runSeekbar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                    mSeekBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);

                        Thread.sleep(MY_RESULT_CODE_1000);

                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSeekBar.setProgress(msg.what);
        }
    };

    public void playSongLocaltion() {
        uri = Uri.parse(arrList.get(mLocaltionSong).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.seekTo(time);
        mediaPlayer.start();

        completionListener();
        updateSong();
    }

    private void completionListener(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mLocaltionSong < (arrList.size() - 1)) {
                    playsong(mLocaltionSong+1);
                    mLocaltionSong = mLocaltionSong + 1;
                    allTimeSong();
                    updateTime();
                    updateSong();
                }
            }
        });
    }

    public void playsong(int mLocaltionSong){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(arrList.get(mLocaltionSong));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void allTimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        mTimeAll.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        mSeekBar.setMax(mediaPlayer.getDuration());


    }

    public void updateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                mTimeStart.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                t = mediaPlayer.getCurrentPosition();
                handler.postDelayed(this, MY_RESULT_CODE_5);
            }
        }, MY_RESULT_CODE_1);
    }

    public void updateSong() {
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
                mserviceMusic.previousSong();
                break;

            case R.id.pause_play:
                mserviceMusic.playpauseSong();
                break;

            case R.id.next:
                mserviceMusic.nextSong();
                break;


        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DetailSongRuningActivity.this, MainActivity.class);
        i.putExtra("time", t);
        setResult(000, i);
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
        finish();
        super.onBackPressed();
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iboundService = true;
            ServiceMusic.MyBinder binder = (ServiceMusic.MyBinder) iBinder;
            mserviceMusic = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
