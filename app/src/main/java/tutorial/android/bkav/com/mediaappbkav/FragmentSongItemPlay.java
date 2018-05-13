package tutorial.android.bkav.com.mediaappbkav;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by PHONG on 4/27/2018.
 */

@SuppressLint("ValidFragment")
public class FragmentSongItemPlay extends Fragment implements View.OnClickListener, IAudioPlayer {

    private ImageView mImageSong;
    private ImageView mImageBackground;
    private SeekBar mChangeDuration;
    private TextView mNameSong;
    private TextView mNameSingle;
    private TextView mMaxTimeSong;
    private TextView mMinTimeSong;
    private Button mComeBack;
    private Button mPlaySong;
    private Button mPreviourSong;
    private Button mNextSong;
    private Button mShuffer ;
    private Button mRepeat ;

    private Handler mMyHandler = new Handler();
    private Database mDatabase;
    private Bundle mArgs;

    private final String SONG_ITEM_DATA = "songItem";
    private double mStartTime = 0;
    private double mFinalTime = 0;
    private int mCurrentPosition = -1;
    private List<SongItem> mSongItemList;

    private MediaPlayBackService mMediaPlayBackService;

    public FragmentSongItemPlay() {
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MediaPlayBackService.UPDATE_CURRENT_SONG_ACTION.equals(intent.getAction())) {

            }
        }
    };

    public FragmentSongItemPlay(MediaPlayBackService mMediaPlayBackService) {
        this.mMediaPlayBackService = mMediaPlayBackService;
        mMediaPlayBackService.setOnComplete(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MediaPlayBackService.UPDATE_CURRENT_SONG_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(mReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_song_item_play, container, false);
        mDatabase = new Database(getActivity().getApplicationContext());
        mSongItemList = mDatabase.getAllSongItem();

        init(view);

        setOnSeekbar();

        mArgs = getArguments();
        if (mArgs != null) {
            SongItem songItem = (SongItem) mArgs.getSerializable(SONG_ITEM_DATA);
            displayDataSong(songItem);
            mCurrentPosition = songItem.getmPositionItem();
        }

        boolean checkPlaySong = mMediaPlayBackService != null && mMediaPlayBackService.checkPlaySong();
        if (checkPlaySong) {
            mPlaySong.setBackgroundResource(R.drawable.ic_media_pause_light);
        } else {
            mPlaySong.setBackgroundResource(R.drawable.ic_play_black);
        }

        setOnClickView();
        return view;
    }

    private void setOnSeekbar() {
        if (mMediaPlayBackService != null) {
            mStartTime = mMediaPlayBackService.getCurrentDuartion();
            mFinalTime = mMediaPlayBackService.getDuragion();
            mChangeDuration.setMax((int) mFinalTime);
            mMaxTimeSong.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) mFinalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) mFinalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mFinalTime)))
            );

            mMinTimeSong.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) mStartTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) mStartTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mStartTime)))
            );
            mChangeDuration.setProgress((int) mStartTime);
            mMyHandler.postDelayed(updateDuration, 100);

            mChangeDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mMediaPlayBackService.seek(mChangeDuration.getProgress());
                }
            });
        }
    }

    private void setOnClickView() {
        mPlaySong.setOnClickListener(this);
        mPreviourSong.setOnClickListener(this);
        mNextSong.setOnClickListener(this);
        mComeBack.setOnClickListener(this);
        mShuffer.setOnClickListener(this);
        mRepeat.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.play_song_button: {
                boolean checkPlaySong = mMediaPlayBackService.checkPlaySong();
                if (checkPlaySong) {
                    mPlaySong.setBackgroundResource(R.drawable.ic_play_black);
                } else {
                    mPlaySong.setBackgroundResource(R.drawable.ic_media_pause_light);
                }
                mMediaPlayBackService.pause();
                break;
            }
            case R.id.previour_song_button: {
                if (mCurrentPosition == -1 || mCurrentPosition == 0) {
                    mCurrentPosition = mSongItemList.size() - 1;

                } else {
                    mCurrentPosition = (mCurrentPosition - 1) % mSongItemList.size();
                }
                mPlaySong.setBackgroundResource(R.drawable.ic_pause_black_large);
                SongItem songItem = mSongItemList.get(mCurrentPosition);
                displayDataSong(songItem);
                mMediaPlayBackService.previourSong();
                break;
            }

            case R.id.next_song_button: {
                mCurrentPosition = (mCurrentPosition + 1) % mSongItemList.size();
                SongItem songItem = mSongItemList.get(mCurrentPosition);
                mPlaySong.setBackgroundResource(R.drawable.ic_pause_black_large);
                displayDataSong(songItem);
                mMediaPlayBackService.nextSong();
                break;
            }
            case R.id.come_back_button: {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(this);
                transaction.commit();
                break;
            }

            case R.id.shuffer_button:{
                mMediaPlayBackService.shuffer();
                break;
            }

            case R.id.repeat_button :{
                mMediaPlayBackService.repeat();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void displayDataSong(SongItem songItem) {
        if (songItem != null) {
            mNameSong.setText(songItem.getmName());
            mNameSingle.setText(songItem.getmSingle());
            mImageSong.setImageURI(Uri.parse(songItem.getmUriThumabnail()));
            mImageSong.setBackground(null);
            mImageBackground.setImageURI(Uri.parse(songItem.getmUriThumabnail()));
            mImageBackground.setBackgroundColor(Color.GRAY);
            mMaxTimeSong.setText(songItem.setCorrectDuration(songItem.getmDuration()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration configuration = getResources().getConfiguration();
        if (mArgs != null && configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mArgs != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
    }

    private void init(View view) {
        mImageBackground = (ImageView) view.findViewById(R.id.image_background_song);
        mImageSong = (ImageView) view.findViewById(R.id.imageview_play_song);
        mChangeDuration = (SeekBar) view.findViewById(R.id.change_duration);
        mNameSingle = (TextView) view.findViewById(R.id.textview_name_single);
        mNameSong = (TextView) view.findViewById(R.id.textview_name_song);
        mMaxTimeSong = (TextView) view.findViewById(R.id.max_time_song);
        mMinTimeSong = (TextView) view.findViewById(R.id.min_time_song);
        mComeBack = (Button) view.findViewById(R.id.come_back_button);
        mPlaySong = (Button) view.findViewById(R.id.play_song_button);
        mPreviourSong = (Button) view.findViewById(R.id.previour_song_button);
        mNextSong = (Button) view.findViewById(R.id.next_song_button);
        mShuffer = (Button) view.findViewById(R.id.shuffer_button);
        mRepeat = (Button) view.findViewById(R.id.repeat_button);

    }

    private Runnable updateDuration = new Runnable() {
        @Override
        public void run() {
            mStartTime = mMediaPlayBackService.getCurrentDuartion();
            mMinTimeSong.setText(String.format("%02d:%02d",

                    TimeUnit.MILLISECONDS.toMinutes((long) mStartTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) mStartTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) mStartTime))));

            mChangeDuration.setProgress((int) mStartTime);
            mMyHandler.postDelayed(this, 100);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void complete(SongItem songItem) {
        mMediaPlayBackService.play(songItem);
        displayDataSong(songItem);
    }
}
