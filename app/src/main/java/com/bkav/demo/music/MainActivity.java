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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        FragmentListSong fragmentListSong = new FragmentListSong();
        mFragmentTransaction.replace(R.id.frame_main, fragmentListSong);
        mFragmentTransaction.commit();


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
                    Toast.makeText(this, " No permission grandted", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Bạn hãy nhập vào tên bài hát hoặc tên ca sỹ", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void sendData(Bundle dataBundler) {

        mBundle = new Bundle();
        mBundle.putString("tenbaihat",dataBundler.getString("tenbaihat"));
        mBundle.putString("tencasy",dataBundler.getString("tencasy"));
        mBundle.putString("mangbaihat",dataBundler.getString("mangbaihat"));
        mFragmentDetails = new FragmentDetails();
        mFragmentDetails.setArguments(mBundle);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, mFragmentDetails);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}




















