package com.bkav.demo.music;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


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
        mPlayStart = (ImageView) findViewById(R.id.pause_play);
        mNext = (ImageView) findViewById(R.id.next);
        mHinhCaSy = (ImageView) findViewById(R.id.hinh_casy);
        mTenCaSy = (TextView) findViewById(R.id.tencasy);
        mTenBaiHat = (TextView) findViewById(R.id.tenbaihat);


    }
}
