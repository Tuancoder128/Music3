package com.bkav.demo.music;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bkav.demo.music.SQLiteDatabase.Database;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements FragmentListSong.SendDataToFragmentDetails, SearchView.OnQueryTextListener {

    private static final int MY_PERMISSION_REQUEST = 1;
    private android.app.FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Toolbar toolbar;
    private Bundle mBundle;
    public FragmentDetails mFragmentDetails;
    public FragmentListSong mfragmentListSong;
    private static final String NOTIFICATION = "Bạn hãy nhập vào tên bài hát";
    private static final String NAME_TITLE = "tenbaihat";
    private static final String NAME_ARTIST = "tencasy";
    private static final String ARRAY_LIST_SONG = "mangbaihat";
    private static final String BIT_MAP = "bitmap";
    private static final String SEARCH_VIEW_HINT = "Nhập vào tên bài hát";

    private ArrayList<ThongTinBaiHat> mArrayList;
    public AdapterBaiHat mAdapterBaiHat;
    private SearchView mSearchView;
    public ServiceMusic mServiceMusic;
    public boolean mboundService = false;
    private Intent mIntentService;
    String nameSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        FragmentListSong mFragmentListSong = new FragmentListSong();
        mFragmentTransaction.replace(R.id.frame_main, mFragmentListSong);
        mFragmentTransaction.commit();

        checkPermission();
        serviceConnection();

    }

    public void checkPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    finish();

                }
            }
        }
    }


    @Override
    public void sendData(Bundle dataBundler) {

        mBundle = new Bundle();
        mBundle.putString(NAME_TITLE, dataBundler.getString(NAME_TITLE));
        mBundle.putString(NAME_ARTIST, dataBundler.getString(NAME_ARTIST));
        mBundle.putString(ARRAY_LIST_SONG, dataBundler.getString(ARRAY_LIST_SONG));
        mBundle.putByteArray(BIT_MAP, dataBundler.getByteArray(BIT_MAP));
        mFragmentDetails = new FragmentDetails();
        mFragmentDetails.setArguments(mBundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_main, mFragmentDetails);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        MenuItem search = menu.findItem(R.id.item_search);
        mSearchView = (SearchView) search.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(SEARCH_VIEW_HINT);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_search:
                //  Toast.makeText(this, NOTIFICATION, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        // khi click Enter
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        AdapterBaiHat.arrayList.clear();
        if (newText.length() > 0) {
            String newtext = newText.toLowerCase(Locale.getDefault()).trim();
            String[] mangbh = {
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM
            };

            String query_selection = MediaStore.Audio.Media.TITLE + " LIKE" + "'" + "%" + newtext + "%" + "'"
                    + "OR" + " " + MediaStore.Audio.Media.TITLE + " LIKE" + "'" + newtext + "%" + "'"
                    + "OR" + " " + MediaStore.Audio.Media.TITLE + " LIKE" + "'" + "%" + newtext + "'"
                    + "OR" + " " + MediaStore.Audio.Media.TITLE + " LIKE" + "'" + "%" + " " + newtext + " " + "'";

            String[] query_args = new String[]{newText};

            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mangbh, query_selection, null, MediaStore.Audio.Media.TITLE + " ASC");

            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    nameSong = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String aristSong = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String durationSong = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String albumSong = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                    Log.d("CURSOR", nameSong + " " + aristSong + " " + " " + durationSong + " " + albumSong);
                    Log.d("CURSOR_NAME", nameSong);

                    for (ThongTinBaiHat baihat : AdapterBaiHat.arrayListQuery) {
                        if (baihat.getTenBaiHat().trim().equals(nameSong.trim())) {
                            AdapterBaiHat.arrayList.add(baihat);
                            changeNotification();
                        }
                    }
                }
                cursor.close();
            }

        } else {
            if (newText.length() == 0) {
                AdapterBaiHat.arrayList.addAll(AdapterBaiHat.arrayListQuery);
            }
        }

        return true;
    }

    private void changeNotification() {
        FragmentListSong fragmentListSong = new FragmentListSong();
        fragmentListSong.notifiInformation();
    }


    public void serviceConnection() {
        mIntentService = new Intent(MainActivity.this, ServiceMusic.class);
        bindService(mIntentService, serviceConnection, BIND_AUTO_CREATE);

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

        }
    };

}




















