package com.bkav.demo.music;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by vst on 22/04/2018.
 */

public class FragmentDetails extends Fragment implements View.OnClickListener {
    private ImageView mHinhDanhSach;
    private ImageView mPausePlay;
    private ImageView mLike;
    private ImageView mPrevious;
    private ImageView mPlayStart;
    private ImageView mNext;
    private ImageView mDisLike;
    public ImageView mHinhCaSy;
    public TextView mTenAlbum;
    public TextView mTenBaiHat;
    private TextView mTimeStart;
    private TextView mTimeAll;
    private SeekBar mSeekBar;
    public Intent mIntentService;
    public Bundle mBundle;
    private String mTenAlbumBack;
    private String mTenBaiHatBack;
    FragmentListSong mFragmentListSong;
    private static final int MY_RESULT_CODE_1 = 500;
    private static final int MY_RESULT_CODE_5 = 1000;
    private static final int MY_RESULT_CODE_1000 = 10000;
    public boolean mboundService = false;
    public ServiceMusic mServiceMusic;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(mServiceMusic, "this is Fragment List", Toast.LENGTH_SHORT).show();
                    mFragmentListSong = new FragmentListSong();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_main, mFragmentListSong);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    return true;
                }
                return false;

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        connectionService();

        mHinhDanhSach = (ImageView) view.findViewById(R.id.danhsach);
        mPausePlay = (ImageView) view.findViewById(R.id.pause_play);
        mLike = (ImageView) view.findViewById(R.id.like);
        mDisLike = (ImageView) view.findViewById(R.id.dis_like);
        mPrevious = (ImageView) view.findViewById(R.id.previous);
        mPlayStart = (ImageView) view.findViewById(R.id.pause_play);
        mNext = (ImageView) view.findViewById(R.id.next);
        mHinhCaSy = (ImageView) view.findViewById(R.id.hinh_casy);

        mLike.setOnClickListener(this);
        mDisLike.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mPlayStart.setOnClickListener(this);
        mNext.setOnClickListener(this);

        mTenAlbum = (TextView) view.findViewById(R.id.tencasy);
        mTenBaiHat = (TextView) view.findViewById(R.id.tenbaihat);
        mTimeStart = (TextView) view.findViewById(R.id.time_start);
        mTimeAll = (TextView) view.findViewById(R.id.time_all);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);


        mBundle = getArguments();
        mTenAlbum.setText(mBundle.getString("tenbaihat").toString());
        mTenBaiHat.setText(mBundle.getString("tencasy").toString());


        managerSeekBar();
        allTimeSong();

        return view;
    }


    private void updateNameSong() {

        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mServiceMusic.mArrayListSong.get(mServiceMusic.mLocalSong));
            mTenAlbumBack = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            mTenBaiHatBack = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            mTenBaiHat.setText(mTenBaiHatBack);
            mTenAlbum.setText(mTenAlbumBack);
            mHinhCaSy.setImageResource(R.drawable.anhtho);


        }

    }

    public void managerSeekBar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                ServiceMusic.mMediaPlayer.seekTo(i);
                mSeekBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // sự kiện khi chạm vào seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //sự kiên khi nhả seekbar
                ServiceMusic.mMediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ServiceMusic.mMediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = ServiceMusic.mMediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(MY_RESULT_CODE_1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
        updateTime();
    }





    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSeekBar.setProgress(msg.what);
        }
    };

    private void allTimeSong() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        mTimeAll.setText(simpleDateFormat.format(ServiceMusic.mMediaPlayer.getDuration()));
        mSeekBar.setMax(ServiceMusic.mMediaPlayer.getDuration());


    }


    private void updateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                mTimeStart.setText(simpleDateFormat.format(ServiceMusic.mMediaPlayer.getCurrentPosition()));
                mSeekBar.setProgress(ServiceMusic.mMediaPlayer.getCurrentPosition());
                handler.postDelayed(this, MY_RESULT_CODE_5);

            }
        }, MY_RESULT_CODE_1);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous:
                mServiceMusic.previousSong();
                updateNameSong();
                break;

            case R.id.next:
                mServiceMusic.nextSong();
                updateNameSong();
                break;
            case R.id.pause_play:
                // mServiceMusic.clickPausePlayMusic();
                if (ServiceMusic.mMediaPlayer.isPlaying()) {
                    ServiceMusic.mMediaPlayer.pause();
                    mPlayStart.setImageResource(R.drawable.ic_fab_play_btn_normal);
                } else {
                    ServiceMusic.mMediaPlayer.start();
                    mPlayStart.setImageResource(R.drawable.ic_media_pause_dark);
                }
                updateNameSong();
                break;
        }

    }

    public void connectionService() {
        mIntentService = new Intent(getActivity(), ServiceMusic.class);
        getActivity().bindService(mIntentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mboundService = true;
            ServiceMusic.MyBinder binder = (ServiceMusic.MyBinder) iBinder;
            mServiceMusic = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mboundService = false;
        }
    };


}




