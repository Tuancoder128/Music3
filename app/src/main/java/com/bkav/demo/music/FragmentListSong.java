package com.bkav.demo.music;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by vst on 22/04/2018.
 */

public class FragmentListSong extends Fragment {

    private Toolbar toolbar;
    public ArrayList<ThongTinBaiHat> arrayList;
    public AdapterBaiHat baiHatAdapter;
    public ListView mListBaiHat;
    private static final int MY_PERMISSION_REQUEST = 1;
    private static final int MY_REQUEST_CODE = 111;
    private static final int MY_RESULT_CODE = 000;
    private TextView mCLickTenBaiHat;
    private TextView mCLickCasy;
    private TextView mTime;
    private TextView mNameSong;
    private TextView mNumber;
    private ImageView mHinhAlbum;
    private ImageView mPopupMenu;
    private ImageView mImagePlaySong;
    private Button mClickStart;
    private RelativeLayout mMoveSong;
    private ArrayList<String> mPath;
    private Intent intent;
    private Bundle bundle;
    public boolean iboundService = false;
    public ServiceMusic mServiceMusic;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public  void getMusic() {
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

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mTime = (TextView) view.findViewById(R.id.time);
        mNumber = (TextView) view.findViewById(R.id.number);
        mListBaiHat = (ListView) view.findViewById(R.id.list_album);
        mCLickTenBaiHat = (TextView) view.findViewById(R.id.click_tenbaihat);
        mNameSong = (TextView) view.findViewById(R.id.name_song);
        mCLickCasy = (TextView) view.findViewById(R.id.click_casy);
        mClickStart = (Button) view.findViewById(R.id.click_start);
        mMoveSong = (RelativeLayout) view.findViewById(R.id.move_song);


        arrayList = new ArrayList<>();
        baiHatAdapter = new AdapterBaiHat(getActivity(), R.layout.activity_baihat, arrayList);
        mListBaiHat.setAdapter(baiHatAdapter);

        return view;
    }

    private void clickSong() {
        mListBaiHat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                mNumber.setVisibility(View.INVISIBLE);
//                mImagePlaySong.setVisibility(View.VISIBLE);
                String local = mPath.get(i);


                try {
                    mServiceMusic.setLocalSong(local);
                    mClickStart.setBackgroundResource(R.drawable.ic_media_pause_light);


                } catch (IOException e) {
                    e.printStackTrace();
                }


                mHinhAlbum.setImageResource(arrayList.get(i).getHinhAlbum());
                mCLickTenBaiHat.setText(arrayList.get(i).getTenBaiHat());
                mCLickCasy.setText(arrayList.get(i).getTheloai());


                MediaMetadataRetriever retriever = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                    retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(mPath.get(i));

                    String tenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String ten = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                    mCLickTenBaiHat.setText(ten);
                    mCLickCasy.setText(tenAlbum);

                }

//                bundle = new Bundle();
//                bundle.putInt("vitri", i);
//                bundle.putStringArrayList("tenbai", mPath);


            }
        });
    }


}
















