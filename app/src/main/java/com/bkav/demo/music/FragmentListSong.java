package com.bkav.demo.music;


import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;

import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

import com.bkav.demo.music.SQLiteDatabase.Database;


/**
 * Created by vst on 22/04/2018.
 */

public class FragmentListSong extends Fragment {


    public static ArrayList<ThongTinBaiHat> arrayList;
    public static ArrayList<String> mPath;
    public static AdapterBaiHat baiHatAdapter;
    public static ListView mListBaiHat;
    private TextView mCLickTenBaiHat;
    private TextView mTenBaiHat;
    public TextView mCLickCasy;
    private TextView mTime;
    private TextView mNameSong;
    private TextView mNumber;
    private ImageView mHinhAlbum;
    private ImageView mPopupMenu;
    private ImageView mImagePlaySong;
    private Button mClickPasePlay;
    private RelativeLayout mMoveSong;
    private SeekBar mSeekBarListMain;
    private Intent mIntentService;
    public Bundle mBundle;
    private Bundle mBundleService;
    private android.support.v7.widget.Toolbar mToolbar;
    public boolean mboundService = false;
    public ServiceMusic mServiceMusic;
    private SendDataToFragmentDetails mSendDataToFragmentDetails;
    public String mLocal;
    public Bitmap mBitmap;
    private Handler mHandler;
    private static final String NAME_TITLE = "tenbaihat";
    private static final String NAME_ARTIST = "tencasy";
    private static final String NAME_ARTIST_ = "tenalbum";
    private static final String ARRAY_LIST_SONG = "arrayListSong";
    private static final String LOCALTION = "localSong";
    private static final String BIT_MAP = "bitmap";
    private static final String DATA_BUNDLER = "dataBunder";
    private static final int MY_RESULT_CODE_SEND_MESSAGE = 1000;
    private Database mDatabase;

    String currentTittle;
    String currentArist;
    String currentDuration;
    int currentAlbum;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void connectionService() {
        mIntentService = new Intent(getActivity(), ServiceMusic.class);
        getActivity().bindService(mIntentService, serviceConnection, Context.BIND_AUTO_CREATE);

    }


    //kết nối giữa interface với Activity khi Fragment được gắn vào Activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSendDataToFragmentDetails = (SendDataToFragmentDetails) context;

        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    mBundle = intent.getBundleExtra(DATA_BUNDLER);
                    mCLickTenBaiHat.setText(mBundle.getString(NAME_TITLE));
                    mCLickCasy.setText(mBundle.getString(NAME_ARTIST_));
                    getImageSong();
                    setItemNormal();
                }
            }
        }, new IntentFilter(ServiceMusic.VALUE_DATA_INTENT));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        baiHatAdapter.notifyDataSetChanged();
    }

    public void notifiInformation() {
        baiHatAdapter.notifyDataSetChanged();
    }


    public interface SendDataToFragmentDetails {
        void sendData(Bundle dataBundler);

    }


    @Override
    public void onResume() {
        super.onResume();
        clickSong();
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
                currentTittle = songCursor.getString(songTittle);
                currentArist = songCursor.getString(songArtist);
                currentDuration = songCursor.getString(songTime);
                currentAlbum = songCursor.getInt(songImage);

                int giay = songCursor.getInt(songTime) / 1000;
                int phut = giay / 60;
                int giayLe = giay - phut * 60;
                String giayle;
                if (giayLe < 10) {
                    giayle = "0" + giayLe;
                } else {
                    giayle = String.valueOf(giayLe);
                }

                mDatabase = new Database(getActivity().getApplicationContext());

                if (mDatabase == null) {
                    mDatabase.addMusic(new ThongTinBaiHat(currentTittle, currentArist, currentDuration, currentAlbum));
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

        mSeekBarListMain = (SeekBar) view.findViewById(R.id.seekbar_listmain);
        mNumber = (TextView) view.findViewById(R.id.number);
        mListBaiHat = (ListView) view.findViewById(R.id.list_album);
        mTenBaiHat = (TextView) view.findViewById(R.id.tenbaihat);
        mCLickTenBaiHat = (TextView) view.findViewById(R.id.click_tenbaihat);
        mCLickCasy = (TextView) view.findViewById(R.id.click_casy);
        mNameSong = (TextView) view.findViewById(R.id.name_song);
        mTime = (TextView) view.findViewById(R.id.time);
        mClickPasePlay = (Button) view.findViewById(R.id.click_pause_play_main);
        mMoveSong = (RelativeLayout) view.findViewById(R.id.move_song);

        arrayList = new ArrayList<>();
        getMusic();
        getAllListMusic();
        changeLayoutFragment();
        baiHatAdapter = new AdapterBaiHat(getActivity(), R.layout.activity_baihat, arrayList);
        mListBaiHat.setAdapter(baiHatAdapter);
        clickSong();
        if (mServiceMusic != null) {
            mClickPasePlay.setBackgroundResource(R.drawable.ic_media_pause_light);
            getImageSong();
            MediaMetadataRetriever retriever = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                retriever = new MediaMetadataRetriever();
                retriever.setDataSource(mServiceMusic.mArrayListSong.get(mServiceMusic.mLocalSong));
                String mTenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String mTenBaiHat = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                managerSeekBar();
                mCLickCasy.setText(mTenAlbum);
                mCLickTenBaiHat.setText(mTenBaiHat);

            }

        } else {
            connectionService();
        }

        mClickPasePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ServiceMusic.mMediaPlayer == null) {
                    mClickPasePlay.setBackgroundResource(R.drawable.ic_play_black);
                } else {
                    if (ServiceMusic.mMediaPlayer.isPlaying()) {
                        ServiceMusic.mMediaPlayer.pause();
                        mClickPasePlay.setBackgroundResource(R.drawable.ic_play_black);
                    } else {
                        ServiceMusic.mMediaPlayer.start();
                        mClickPasePlay.setBackgroundResource(R.drawable.ic_media_pause_light);
                    }
                }


            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("RUN", "onPause");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("RUN", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("RUN", "onDestroy");
    }

    private void animationText() {
        TranslateAnimation animation = new TranslateAnimation(500.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(800);
        animation.setRepeatCount(0);
        animation.setFillAfter(false);
        mCLickTenBaiHat.startAnimation(animation);
        mCLickCasy.startAnimation(animation);

    }

    private void setItemNormal() {
        for (int i = 0; i < mListBaiHat.getChildCount(); i++) {
            View v = mListBaiHat.getChildAt(i);
            TextView mTime = (TextView) v.findViewById(R.id.time);
            TextView mNumber = (TextView) v.findViewById(R.id.number);
            TextView mNameSong = ((TextView) v.findViewById(R.id.name_song));
            mNameSong.setTextColor(Color.BLACK);
            mTime.setTextColor(Color.BLACK);
            mNumber.setTextColor(Color.BLACK);


        }
    }

    private void setItemSelected(View view) {
        View rowView = view;
        TextView mNameSong = (TextView) rowView.findViewById(R.id.name_song);
        TextView mTime = (TextView) rowView.findViewById(R.id.time);
        TextView mNumber = (TextView) rowView.findViewById(R.id.number);
        mNameSong.setTextColor(Color.RED);
        mTime.setTextColor(Color.RED);
        mNumber.setTextColor(Color.RED);

    }


    private void clickSong() {
        mListBaiHat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mClickPasePlay.setBackgroundResource(R.drawable.ic_media_pause_light);
                String mLocaltion = mPath.get(i);
                mCLickTenBaiHat.setTextColor(Color.RED);
                mCLickCasy.setTextColor(Color.RED);
                mCLickTenBaiHat.setText(arrayList.get(i).getTenBaiHat());
                mCLickCasy.setText(arrayList.get(i).getTheloai());

                setItemNormal();
                View rowView = view;
                setItemSelected(rowView);
                animationText();

                String tenbaihat = arrayList.get(i).getTenBaiHat();
                String tencasy = arrayList.get(i).getTheloai();

                mBundle = new Bundle();
                mBundle.putString(NAME_TITLE, tenbaihat);
                mBundle.putString(NAME_ARTIST, tencasy);
                mBundle.putStringArrayList(ARRAY_LIST_SONG, mPath);
                mBundle.putString(LOCALTION, mLocal);

                mServiceMusic.playMusic(mLocaltion);
                mServiceMusic.getLocaltionSong(i);
                mServiceMusic.getArrayListSong(mPath);

                managerSeekBar();
                getImageSong();


            }
        });
    }


    private void getImageSong() {
        MediaMetadataRetriever mGetImage = new MediaMetadataRetriever();
        byte[] mRawArt;
        mBitmap = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        mGetImage.setDataSource(mServiceMusic.mArrayListSong.get(mServiceMusic.mLocalSong));
        mRawArt = mGetImage.getEmbeddedPicture();

        if (null != mRawArt) {
            mBitmap = BitmapFactory.decodeByteArray(mRawArt, 0, mRawArt.length, bfo);
            mBundle.putByteArray(BIT_MAP, mRawArt);
        }
        if (mBitmap != null) {

            mHinhAlbum.setImageBitmap(mBitmap);

        } else {

            mHinhAlbum.setImageResource(R.drawable.ghita);
        }
    }

    private void managerSeekBar() {
        sendMessage();
        mSeekBarListMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // trả về giá trị của seekbar , vd như khi kéo thả seekbar


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // sự kiện khi chạm vào seekbar
                ServiceMusic.mMediaPlayer.seekTo(seekBar.getProgress());
                // Log.d("AAAaa", String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //sự kiên khi nhả seekbar
                ServiceMusic.mMediaPlayer.seekTo(seekBar.getProgress());

            }
        });


    }

    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ServiceMusic.mMediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = ServiceMusic.mMediaPlayer.getCurrentPosition();
                        mHandler.sendMessage(msg);
                        Thread.sleep(MY_RESULT_CODE_SEND_MESSAGE);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mSeekBarListMain.setProgress(msg.what);
                mSeekBarListMain.setMax(ServiceMusic.mMediaPlayer.getDuration());
            }
        };

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