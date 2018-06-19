package com.bkav.demo.music;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements FragmentListSong.SendDataToFragmentDetails {

    private static final int MY_PERMISSION_REQUEST = 1;
    private android.app.FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Toolbar toolbar;
    private Bundle mBundle;
    public FragmentDetails mFragmentDetails;
    private static final String NOTIFICATION = "Bạn hãy nhập vào tên bài hát hoặc tên ca sỹ";
    private static final String NAME_TITLE = "tenbaihat";
    private static final String NAME_ARTIST = "tencasy";
    private static final String ARRAY_LIST_SONG = "mangbaihat";
    private static final String BIT_MAP = "bitmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        FragmentListSong mFragmentListSong = new FragmentListSong();
        mFragmentTransaction.replace(R.id.frame_main, mFragmentListSong);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();

        checkPermission();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        MenuItem search = menu.findItem(R.id.item_search);
        //SearchView searchView = (SearchView) search.getActionView();
        //searchView.setOnQueryTextListener(MainActivity.this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_search:
                Toast.makeText(this, NOTIFICATION, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void sendData(Bundle dataBundler) {

        mBundle = new Bundle();
        mBundle.putString(NAME_TITLE, dataBundler.getString(NAME_TITLE));
        mBundle.putString(NAME_ARTIST, dataBundler.getString(NAME_ARTIST));
        mBundle.putString(ARRAY_LIST_SONG, dataBundler.getString(ARRAY_LIST_SONG));
        mBundle.putByteArray(BIT_MAP, dataBundler.getByteArray(BIT_MAP));
        Log.d("AAA", String.valueOf(dataBundler.getByteArray(BIT_MAP)));
        mFragmentDetails = new FragmentDetails();
        mFragmentDetails.setArguments(mBundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, mFragmentDetails);
        mFragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


}




















