package tutorial.android.bkav.com.mediaappbkav.controller;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tutorial.android.bkav.com.mediaappbkav.Database;
import tutorial.android.bkav.com.mediaappbkav.FragmentSongItemPlay;
import tutorial.android.bkav.com.mediaappbkav.FragmentSongList;
import tutorial.android.bkav.com.mediaappbkav.IOnSongClickListener;
import tutorial.android.bkav.com.mediaappbkav.R;
import tutorial.android.bkav.com.mediaappbkav.SongItem;

/**
 * Created by PHONG on 5/4/2018.
 */

public abstract class LayoutController implements IOnSongClickListener {

    protected AppCompatActivity mActivity;
    protected FragmentSongList mFragmentSongList;
    protected FragmentSongItemPlay mFragmentSongItemPlay;

    private Toolbar mToolbar;
    private Database mDatabase;

    public LayoutController(AppCompatActivity activity) {
        mActivity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) mActivity.findViewById(R.id.tool_bar);

        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDatabase = new Database(mActivity);

        mFragmentSongList = new FragmentSongList();
        mFragmentSongList.setOnSongClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        mActivity.getMenuInflater().inflate(R.menu.menu_main, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final List<SongItem> songItemList = mDatabase.getAllSongItem();

            SearchManager manager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.song_search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(mActivity.getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    List<SongItem> list = new ArrayList<>();
                    for (int i = 0; i < songItemList.size(); i++) {
                        if (songItemList.get(i).getmName().contains(s.trim())) {
                            list.add(songItemList.get(i));
                            Toast.makeText(mActivity, "Lala", Toast.LENGTH_LONG).show();
                        }
                    }
                    return false;
                }
            });

        }

        return true;
    }

    @Override
    public void hideInfo() {
        // Khong lam gi
    }
}
