package tutorial.android.bkav.com.mediaappbkav.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tutorial.android.bkav.com.mediaappbkav.FragmentSongItemPlay;
import tutorial.android.bkav.com.mediaappbkav.R;
import tutorial.android.bkav.com.mediaappbkav.SongItem;

/**
 * Created by quanglh on 07/05/2018.
 */

public class TwoColumeLayoutController extends LayoutController {
    public TwoColumeLayoutController(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mActivity.findViewById(R.id.fragment_container2) != null) {
            mFragmentSongItemPlay = new FragmentSongItemPlay();
//                Bundle args = new Bundle();
//                args.putString(LAST_ITEM_TITLE_EXTRA, currentItemTitle);
//                mFragmentSongItemPlay.setArguments(args);

            // Create a new Fragment to be placed in the activity layout

            //mFragmentSongList.setLoadCallback(mNewContentFragment);

            // Add the fragment to the 'fragment_container' FrameLayout
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.news_list_fragment_container, mFragmentSongList)
                    .replace(R.id.new_content_fragment_container, mFragmentSongItemPlay)
                    .commit();
        }
    }

    @Override
    public void onSongClicked(SongItem songItem) {
        mFragmentSongItemPlay.displayDataSong(songItem);
    }
}
