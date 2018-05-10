package com.bkav.demo.music;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vst on 22/04/2018.
 */

public class FragmentDetails extends Fragment implements View.OnClickListener {
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
    private int mTime;
    private static final int MY_RESULT_CODE_1 = 100;
    private static final int MY_RESULT_CODE_5 = 500;
    private static final int MY_RESULT_CODE_1000 = 1000;
    private static int TIME_START = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        mHinhDanhSach = (ImageView) view.findViewById(R.id.danhsach);
        mPopupMenu = (ImageView) view.findViewById(R.id.popup_song);
        mLike = (ImageView) view.findViewById(R.id.like);
        mDisLike = (ImageView) view.findViewById(R.id.dis_like);
        mPrevious = (ImageView) view.findViewById(R.id.previous);
        mPlayStart = (ImageView) view.findViewById(R.id.pause_play);
        mNext = (ImageView) view.findViewById(R.id.next);
        mHinhCaSy = (ImageView) view.findViewById(R.id.hinh_casy);

        mTenCaSy = (TextView) view.findViewById(R.id.tencasy);
        mTenBaiHat = (TextView) view.findViewById(R.id.tenbaihat);
        mTimeStart = (TextView) view.findViewById(R.id.time_start);
        mTimeAll = (TextView) view.findViewById(R.id.time_all);

        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);

        return view;
    }


    @Override
    public void onClick(View view) {
        mLike.setOnClickListener(this);
        mDisLike.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mPlayStart.setOnClickListener(this);
        mNext.setOnClickListener(this);

    }


}

