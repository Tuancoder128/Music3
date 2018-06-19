package com.bkav.demo.music;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ScaleDrawable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
    private ImageView mPrevious;
    private ImageView mPlayStart;
    private ImageView mNext;
    private ImageView mRepeat;
    private ImageView mShuffle;
    public ImageView mHinhCaSy;
    private ImageView mBackgroundHinhAlbum;
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
    private static final int MY_RESULT_CODE_SEND_MESSAGE = 1000;
    public boolean mboundService = false;
    public ServiceMusic mServiceMusic;
    private Bitmap mBitmap;
    private Handler mHandler;
    private FrameLayout mFrameLayout;
    private android.app.FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    public Bundle mBundler;
    private MyBroastReceiver mMyBroastReceiver;
    private Intent mIntentBroadCast;
    public static final String VALUE_DATA_INTENT = "ValueDataIntent";
    private static final String BIT_MAP = "bitmap";
    private static final String BACK_STACK_FRAGMENT_LIST_SONG = "back_fragmentListSong";
    private static final String NAME_TITLE = "tenbaihat";
    private static final String NAME_ARTIST = "tencasy";
    private static final String NAME_ARTIST_ = "tenalbum";
    private static final String DATA_BUNDLER = "dataBunder";
    private static final String NOTIFICATION_SHUFFLE = "Nghe bài hát ngẫu nhiên ";
    private static final String NOTIFICATION_REPEAT = "Lặp lại bài hát hiện tại ";
    private IntentFilter mIntentFilter;
    private FragmentDetails mFragmentDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyBroastReceiver = new MyBroastReceiver();
        mIntentFilter = new IntentFilter();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    mBundle = intent.getBundleExtra(DATA_BUNDLER);
                    mTenAlbum.setText(mBundle.getString(NAME_TITLE));
                    mTenBaiHat.setText(mBundle.getString(NAME_ARTIST_));
                    Log.d("VVV", mBundle.getString(NAME_TITLE));
                    Log.d("VVV", mBundle.getString(NAME_ARTIST_));
                    getImageSong();
                    managerSeekBar();
                    allTimeSong();
                    zoomText();

                }

            }
        }, new IntentFilter(ServiceMusic.VALUE_DATA_INTENT));
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    updateNameSong();
//                    mIntentBroadCast = new Intent();
//                    mBundler = new Bundle();
//                    mBundler.putString(NAME_ARTIST_, mTenAlbumBack);
//                    mBundler.putString(NAME_TITLE, mTenBaiHatBack);
//                    mIntentBroadCast.putExtra(DATA_BUNDLER, mBundler);
//                    mIntentBroadCast.setAction(VALUE_DATA_INTENT);
//                    getActivity().sendBroadcast(mIntentBroadCast);
//                    Toast.makeText(mServiceMusic, "YOU CLICKED FRAGMENT", Toast.LENGTH_SHORT).show();
//                    mFragmentListSong = new FragmentListSong();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.frame_main, mFragmentListSong);
//                    fragmentTransaction.commit();
//                    return true;
//                }
//
//                return false;
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        connectionService();

        mFrameLayout = (FrameLayout)view.findViewById(R.id.frame_detais);
        mHinhDanhSach = (ImageView) view.findViewById(R.id.danhsach);
        mPausePlay = (ImageView) view.findViewById(R.id.pause_play);
        mPrevious = (ImageView) view.findViewById(R.id.previous);
        mPlayStart = (ImageView) view.findViewById(R.id.pause_play);
        mNext = (ImageView) view.findViewById(R.id.next);
        mRepeat = (ImageView) view.findViewById(R.id.repeat);
        mShuffle = (ImageView) view.findViewById(R.id.shuffle);
        mHinhCaSy = (ImageView) view.findViewById(R.id.hinh_casy);
        mBackgroundHinhAlbum = (ImageView) view.findViewById(R.id.background_hinhalbum);

        mPrevious.setOnClickListener(this);
        mPlayStart.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mRepeat.setOnClickListener(this);
        mShuffle.setOnClickListener(this);

        mTenAlbum = (TextView) view.findViewById(R.id.tencasy);
        mTenBaiHat = (TextView) view.findViewById(R.id.tenbaihat);
        mTimeStart = (TextView) view.findViewById(R.id.time_start);
        mTimeAll = (TextView) view.findViewById(R.id.time_all);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);

        mBundle = getArguments();
        mTenAlbum.setText(mBundle.getString(NAME_TITLE).toString());
        mTenBaiHat.setText(mBundle.getString(NAME_ARTIST).toString());
        mPlayStart.setImageResource(R.drawable.ic_media_pause_light);
        mRepeat.setImageResource(R.drawable.ic_repeat_dark);
        mShuffle.setImageResource(R.drawable.ic_shuffle_dark);

        mFragmentDetails = new FragmentDetails();
        backFragment();
        getBipMap();
        managerSeekBar();
        zoomText();
        animImageSong();
        return view;

    }


    private void backFragment() {
        mHinhDanhSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                mFragmentListSong = new FragmentListSong();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main,mFragmentListSong);
                //mFragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void getBipMap() {
        byte[] mBipmap = getArguments().getByteArray(BIT_MAP);
        if (mBipmap != null) {
            mBitmap = BitmapFactory.decodeByteArray(mBipmap, 0, mBipmap.length);
            mHinhCaSy.setImageBitmap(mBitmap);
            mBackgroundHinhAlbum.setImageBitmap(mBitmap);
        } else {
            mHinhCaSy.setImageResource(R.drawable.ghita);
            mBackgroundHinhAlbum.setImageResource(R.drawable.ghita);
        }
    }





    public void getImageSong() {
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

            mHinhCaSy.setImageBitmap(mBitmap);
            mBackgroundHinhAlbum.setImageBitmap(mBitmap);

        } else {

            mHinhCaSy.setImageResource(R.drawable.ghita);
            mBackgroundHinhAlbum.setImageResource(R.drawable.ghita);
        }


    }

    public void getImageSongSuffle() {
        MediaMetadataRetriever mGetImage = new MediaMetadataRetriever();
        byte[] mRawArt;
        mBitmap = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        mGetImage.setDataSource(mServiceMusic.mArrayListSong.get(mServiceMusic.mSongIndex));
        mRawArt = mGetImage.getEmbeddedPicture();

        if (null != mRawArt) {
            mBitmap = BitmapFactory.decodeByteArray(mRawArt, 0, mRawArt.length, bfo);
            mBundle.putByteArray(BIT_MAP, mRawArt);
        }
        if (mBitmap != null) {
            mHinhCaSy.setImageBitmap(mBitmap);
            mBackgroundHinhAlbum.setImageBitmap(mBitmap);
        } else {

            mHinhCaSy.setImageResource(R.drawable.ghita);
        }


    }

    public void getImageSongRepeat() {
        MediaMetadataRetriever mGetImage = new MediaMetadataRetriever();
        byte[] mRawArt;
        mBitmap = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        mGetImage.setDataSource(mServiceMusic.mArrayListSong.get(mServiceMusic.mLocalSong -1));
        mRawArt = mGetImage.getEmbeddedPicture();

        if (null != mRawArt) {
            mBitmap = BitmapFactory.decodeByteArray(mRawArt, 0, mRawArt.length, bfo);
            mBundle.putByteArray(BIT_MAP, mRawArt);
        }
        if (mBitmap != null) {

            mHinhCaSy.setImageBitmap(mBitmap);
            mBackgroundHinhAlbum.setImageBitmap(mBitmap);
        } else {

            mHinhCaSy.setImageResource(R.drawable.ghita);
        }


    }

    private void updateNameSong() {

        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mServiceMusic.mArrayListSong.get(mServiceMusic.mLocalSong));
            mTenAlbumBack = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            mTenBaiHatBack = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            mTenBaiHat.setText(mTenAlbumBack);
            mTenAlbum.setText(mTenBaiHatBack);
            zoomText();

        }

    }

    private void updateNameSongRepeat() {

        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mServiceMusic.mArrayListSong.get(mServiceMusic.mLocalSong -1));
            mTenAlbumBack = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            mTenBaiHatBack = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            mTenBaiHat.setText(mTenAlbumBack);
            mTenAlbum.setText(mTenBaiHatBack);
            zoomText();

        }

    }

    public void updateNameSongShuffle() {
        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mServiceMusic.mArrayListSong.get(mServiceMusic.mSongIndex));
            String mTenAlbumBackShuffle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String mTenBaiHatBackShuffle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            mTenBaiHat.setText(mTenAlbumBackShuffle);
            mTenAlbum.setText(mTenBaiHatBackShuffle);
            Log.d("SSSS", mTenAlbumBackShuffle);
            Log.d("SSSS", mTenBaiHatBackShuffle);
            zoomText();

        }
    }

    public void managerSeekBar() {
        sendMessage();
        timeStart();
        allTimeSong();
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // trả về giá trị của seekbar , vd như khi kéo thả seekbar
                // ServiceMusic.mMediaPlayer.seekTo(i);

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


    }

    public void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ServiceMusic.mMediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = ServiceMusic.mMediaPlayer.getCurrentPosition();
                        Log.d("AAA", String.valueOf(ServiceMusic.mMediaPlayer.getCurrentPosition()));
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
                mSeekBar.setProgress(msg.what);
            }
        };

    }

    private void timeStart() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                mTimeStart.setText(simpleDateFormat.format(ServiceMusic.mMediaPlayer.getCurrentPosition()));
                mSeekBar.setProgress(ServiceMusic.mMediaPlayer.getCurrentPosition());
                mHandler.postDelayed(this, MY_RESULT_CODE_SEND_MESSAGE);

            }
        }, MY_RESULT_CODE_SEND_MESSAGE);
    }

    private void allTimeSong() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        mTimeAll.setText(simpleDateFormat.format(ServiceMusic.mMediaPlayer.getDuration()));
        mSeekBar.setMax(ServiceMusic.mMediaPlayer.getDuration());


    }

    public void zoomText() {
        Animation mZoom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom);
        mZoom.reset();
        mTenAlbum.clearAnimation();
        mTenBaiHat.clearAnimation();
        mTenAlbum.startAnimation(mZoom);
        mTenBaiHat.startAnimation(mZoom);
    }

    public void animImageSong(){
        Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_image_s);
        mAnimation.reset();
        mBackgroundHinhAlbum.clearAnimation();
        mBackgroundHinhAlbum.startAnimation(mAnimation);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous:
                mServiceMusic.previousSong();
                mPlayStart.setImageResource(R.drawable.ic_media_pause_light);
                managerSeekBar();
                updateNameSong();
                getImageSong();
                animImageSong();

                break;


            case R.id.next:
                mServiceMusic.nextSong();
                mPlayStart.setImageResource(R.drawable.ic_media_pause_light);
                managerSeekBar();
                updateNameSong();
                getImageSong();
                animImageSong();

                break;
            case R.id.pause_play:
                if (ServiceMusic.mMediaPlayer.isPlaying()) {
                    ServiceMusic.mMediaPlayer.pause();
                    mPlayStart.setImageResource(R.drawable.ic_play_black);
                } else {
                    ServiceMusic.mMediaPlayer.start();
                    mPlayStart.setImageResource(R.drawable.ic_media_pause_light);
                }
                break;

            case R.id.repeat:
                Toast.makeText(mServiceMusic, NOTIFICATION_REPEAT, Toast.LENGTH_SHORT).show();
                mShuffle.setImageResource(R.drawable.ic_shuffle_dark);
                mRepeat.setImageResource(R.drawable.ic_repeat_one_song_dark);
                getActivity().registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        ServiceMusic.mMediaPlayer.release();
                        ServiceMusic.mMediaPlayer = null;
                        mServiceMusic.repeatSong();
                        updateNameSongRepeat();
                        getImageSongRepeat();
                        managerSeekBar();
                        allTimeSong();
                        animImageSong();
                    }
                }, new IntentFilter(ServiceMusic.VALUE_DATA_INTENT_REPEAT));
                break;


            case R.id.shuffle:
                Toast.makeText(mServiceMusic, NOTIFICATION_SHUFFLE, Toast.LENGTH_SHORT).show();
                mShuffle.setImageResource(R.drawable.ic_play_shuffle_orange_noshadow);
                mRepeat.setImageResource(R.drawable.ic_repeat_dark);
                getActivity().registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        ServiceMusic.mMediaPlayer.release();
                        ServiceMusic.mMediaPlayer = null;
                        mServiceMusic.suffleSong();
                        managerSeekBar();
                        allTimeSong();
                        updateNameSongShuffle();
                        getImageSongSuffle();
                        animImageSong();
                    }
                }, new IntentFilter(ServiceMusic.VALUE_DATA_INTENT_SUFFLE));

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






















