package tutorial.android.bkav.com.mediaappbkav;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by PHONG on 4/27/2018.
 */

public class FragmentSongList extends Fragment implements IOnSongClickListener {

    private RecyclerView mListSongRecyclerview;
    private LinearLayout mLinearLayout;
    private ImageView mSongImageView;
    private TextView mNameSongTextview;
    private TextView mDurationSongTextview;
    private Button mPauseButton;

    private String mDuration;
    private final static String SONG_ITEM = "songItem";
    private List<SongItem> mListSong = new ArrayList<>();
    private Database mDatabase;
    private AdapterSong mAdapterItemSong;
    private MediaPlayBackService mMediaPlayBackService;

    private IOnSongClickListener mOnSongClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = new Database(getContext());
        bindServiceMediaPlayer();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        init(view);
        mListSong = mDatabase.getAllSongItem();
        adapterRecyclerview(mListSong);

        return view;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMediaPlayBackService = ((MediaPlayBackService.MyBinder) service).getService();
            mAdapterItemSong.notifyDataSetChanged();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void bindServiceMediaPlayer() {
        Intent intent = new Intent();
        intent.setClass(getContext(), MediaPlayBackService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }


    public void adapterRecyclerview(List<SongItem> mListSong) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < mListSong.size(); i++) {
            if (i == 0
                    || (mListSong.get(i).getmName().charAt(0) != mListSong.get(i - 1).getmName().charAt(0))) {

                CharSequence alphabetDisplay = mListSong.get(i).getmName()
                        .subSequence(0,
                                1);
                objectList.add(alphabetDisplay);
            }
            objectList.add(mListSong.get(i));
        }
        mAdapterItemSong = new AdapterSong(getContext(), objectList, this);
        mListSongRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mListSongRecyclerview.setAdapter(mAdapterItemSong);
    }


    @Override
    public void onDestroy() {
        getActivity().unbindService(connection);
        super.onDestroy();
    }

    private void init(View view) {
        mListSongRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerview_list_song);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
        mSongImageView = (ImageView) view.findViewById(R.id.imageview_song);
        mDurationSongTextview = (TextView) view.findViewById(R.id.textview_duration_song);
        mPauseButton = (Button) view.findViewById(R.id.button_pause);
        mNameSongTextview = (TextView) view.findViewById(R.id.textview_name_song);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void playMusic(final SongItem songItem) {
        mMediaPlayBackService.play(songItem);
        mLinearLayout.setVisibility(View.VISIBLE);
        mNameSongTextview.setText(songItem.getmName());
        mSongImageView.setImageURI(Uri.parse(songItem.getmUriThumabnail()));
        mSongImageView.setBackground(null);
        mDurationSongTextview.setText(songItem.setCorrectDuration(songItem.getmDuration()));

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkPlaySong = mMediaPlayBackService.checkPlaySong();
                if (checkPlaySong) {
                    mPauseButton.setBackgroundResource(R.drawable.ic_play_black);
                } else {
                    mPauseButton.setBackgroundResource(R.drawable.ic_media_pause_light);
                }
                mMediaPlayBackService.pause();
            }
        });

        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment newFragment = new FragmentSongItemPlay(mMediaPlayBackService);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(SONG_ITEM, songItem);
//                newFragment.setArguments(bundle);
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_song, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });
    }

    @Override
    public void onSongClicked(SongItem songItem) {
        if (mOnSongClickListener != null) {
            mOnSongClickListener.onSongClicked(songItem);
        }
    }

    @Override
    public void hideInfo() {
        mLinearLayout.setVisibility(View.GONE);
    }

    public void setOnSongClickListener(IOnSongClickListener listener) {
        mOnSongClickListener = listener;
    }
}
