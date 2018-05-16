package com.bkav.demo.music;


import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentResolver;

import android.content.Context;
import android.content.Intent;

import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.File;

import java.util.ArrayList;


/**
 * Created by vst on 22/04/2018.
 */

public class FragmentListSong extends Fragment {


    public ArrayList<ThongTinBaiHat> arrayList;
    public AdapterBaiHat baiHatAdapter;
    public ListView mListBaiHat;

    private static final int MY_REQUEST_CODE = 111;
    private static final int MY_RESULT_CODE = 000;
    private TextView mCLickTenBaiHat;
    private TextView mTenBaiHat;
    private TextView mCLickCasy;
    private TextView mTime;
    private TextView mNameSong;
    private TextView mNumber;
    private ImageView mHinhAlbum;
    private ImageView mPopupMenu;
    private ImageView mImagePlaySong;
    private Button mClickPasePlay;
    private RelativeLayout mMoveSong;
    public ArrayList<String> mPath;
    public Intent mIntentService;
    public Bundle mBundle;
    public Bundle mBundleService;
    private android.support.v7.widget.Toolbar mToolbar;
    public boolean mboundService = false;
    public ServiceMusic mServiceMusic;
    private SendDataToFragmentDetails mSendDataToFragmentDetails;
    public String mLocal;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionService();


    }

    //    public void passDataToService() {
//
//        mIntentService = new Intent(getActivity(), ServiceMusic.class);
//        mBundleService = new Bundle();
//        mBundleService.putString("dataLocalService", mLocal);
//        mBundleService.putStringArrayList("arrayList",mPath);
//        Log.d("abcd", String.valueOf(mPath));
//        mIntentService.putExtras(mBundleService);
//        getActivity().startService(mIntentService);
//
//
//    }
    public void connectionService() {
        mIntentService = new Intent(getActivity(), ServiceMusic.class);
        getActivity().bindService(mIntentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    //kết nối giữa interface với Activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSendDataToFragmentDetails = (SendDataToFragmentDetails) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        baiHatAdapter.notifyDataSetChanged();


    }

    public interface SendDataToFragmentDetails {
        void sendData(Bundle dataBundler);

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void getMusic() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int i = 1;
            int songTittle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songTime = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songImage = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);


            do {
                String currentTittle = songCursor.getString(songTittle);
                String currentArist = songCursor.getString(songArtist);
                int currentAlbum = songCursor.getInt(songImage);
                int giay = songCursor.getInt(songTime) / 1000;

                int phut = giay / 60;
                int giayLe = giay - phut * 60;
                String giayle;
                if (giayLe < 10) {
                    giayle = "0" + giayLe;
                } else {
                    giayle = String.valueOf(giayLe);
                }

                arrayList.add(new ThongTinBaiHat(currentTittle, currentArist, i, currentAlbum, phut + ":" + giayle));

                i++;


            } while (songCursor.moveToNext());
            songCursor.close();


        }

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_main, container, false);
        mHinhAlbum = (ImageView) view.findViewById(R.id.hinh_album);
        mPopupMenu = (ImageView) view.findViewById(R.id.other);
        mImagePlaySong = (ImageView) view.findViewById(R.id.image_icon_play_song);

        mToolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mTime = (TextView) view.findViewById(R.id.time);
        mNumber = (TextView) view.findViewById(R.id.number);
        mListBaiHat = (ListView) view.findViewById(R.id.list_album);
        mTenBaiHat = (TextView) view.findViewById(R.id.tenbaihat);
        mCLickTenBaiHat = (TextView) view.findViewById(R.id.click_tenbaihat);
        mNameSong = (TextView) view.findViewById(R.id.name_song);
        mCLickCasy = (TextView) view.findViewById(R.id.click_casy);
        mClickPasePlay = (Button) view.findViewById(R.id.click_pause_play_main);
        mMoveSong = (RelativeLayout) view.findViewById(R.id.move_song);

        arrayList = new ArrayList<>();
        getMusic();
        getAllListMusic();
        clickSong();
        changeLayoutFragment();
        baiHatAdapter = new AdapterBaiHat(getActivity(), R.layout.activity_baihat, arrayList);
        mListBaiHat.setAdapter(baiHatAdapter);

        mClickPasePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mServiceMusic.clickPausePlayMusic();
                if (ServiceMusic.mMediaPlayer.isPlaying()) {
                    ServiceMusic.mMediaPlayer.pause();
                    mClickPasePlay.setBackgroundResource(R.drawable.ic_play_black);
                } else {
                    ServiceMusic.mMediaPlayer.start();
                    mClickPasePlay.setBackgroundResource(R.drawable.ic_media_pause_light);
                }

            }
        });
        return view;




    }

    public void clickSong() {
        mListBaiHat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mClickPasePlay.setBackgroundResource(R.drawable.ic_media_pause_light);
                String mLocaltion = mPath.get(i);

                Log.d("abc", mLocaltion);
                mCLickTenBaiHat.setText(arrayList.get(i).getTenBaiHat());
                mCLickCasy.setText(arrayList.get(i).getTheloai());
                String tenbaihat = arrayList.get(i).getTenBaiHat();
                String tencasy = arrayList.get(i).getTheloai();

                mBundle = new Bundle();
                mBundle.putString("tenbaihat", tenbaihat);
                mBundle.putString("tencasy", tencasy);
                mBundle.putStringArrayList("arrayListSong", mPath);
                mBundle.putString("localSong", mLocal);

                mServiceMusic.playMusic(mLocaltion);
                mServiceMusic.getLocaltionSong(i);
                mServiceMusic.getArrayListSong(mPath);

            }
        });
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

    public void changeLayoutFragment() {

        mMoveSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSendDataToFragmentDetails.sendData(mBundle);
            }
        });
    }


}






























