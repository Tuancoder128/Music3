package tutorial.android.bkav.com.mediaappbkav.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tutorial.android.bkav.com.mediaappbkav.FragmentSongList;
import tutorial.android.bkav.com.mediaappbkav.R;
import tutorial.android.bkav.com.mediaappbkav.SongItem;

/**
 * Created by PHONG on 5/4/2018.
 */

public class OneColumnLayoutController extends LayoutController {
    public OneColumnLayoutController(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mActivity.findViewById(R.id.fragment_container) != null) {
            // Add the fragment to the 'fragment_container' FrameLayout
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mFragmentSongList).commit();
        }
    }

    @Override
    public void onSongClicked(SongItem songItem) {
        mFragmentSongList.playMusic(songItem);
    }
}
