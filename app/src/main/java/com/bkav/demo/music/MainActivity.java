package com.bkav.demo.music;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private ArrayList<ThongTinBaiHat> arrayList;
    private AdapterBaiHat baiHatAdapter;
    private ListView mListBaiHat;
    private static final int MY_PERMISSION_REQUEST = 1;
    private TextView mCLickTenBaiHat;
    private TextView mCLickTheLoai;
    private TextView mTime;
    private ImageView mImageAlbum;
    private Button mClickStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkReadStorage();
        init();
        clickStart();


    }

    private void checkReadStorage() {
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


        } else {

            init();

        }
    }


    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int i = 1;
            int songTittle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songTime = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);


            do {
                String currentTittle = songCursor.getString(songTittle);
                String currentArist = songCursor.getString(songArtist);
                int giay = songCursor.getInt(songTime) / 1000;

                int phut = giay / 60;
                int giayLe = giay - phut * 60;
                String giay1e;
                if (giayLe < 10) {
                    giay1e = "0" + giayLe;
                } else {
                    giay1e = giayLe + "";
                }
                arrayList.add(new ThongTinBaiHat(currentTittle, currentArist, i, phut + ":" + giay1e));
                i++;

                Log.d("tuan", "giay" + giay);


            } while (songCursor.moveToNext());
            songCursor.close();


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "permission grandted", Toast.LENGTH_SHORT).show();


                    }


                } else {
                    Toast.makeText(this, " No permission grandted", Toast.LENGTH_SHORT).show();
                    finish();

                }

            }

        }
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListBaiHat = (ListView) findViewById(R.id.list_album);
        mCLickTenBaiHat = (TextView) findViewById(R.id.click_tenbaihat);
        mTime = (TextView) findViewById(R.id.time);
        mCLickTheLoai = (TextView) findViewById(R.id.click_theloai);
        mClickStart = (Button) findViewById(R.id.click_start);


        arrayList = new ArrayList<>();
        getMusic();
        baiHatAdapter = new AdapterBaiHat(getApplicationContext(), R.layout.activity_baihat, arrayList);
        mListBaiHat.setAdapter(baiHatAdapter);


        mListBaiHat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("abc", "" + i);
                Toast.makeText(MainActivity.this, "click" + i, Toast.LENGTH_SHORT).show();
                mCLickTenBaiHat.setText(arrayList.get(i).getTenBaiHat());
                mCLickTheLoai.setText(arrayList.get(i).getTheloai());
                Log.d("list", " list" + arrayList.size());


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        MenuItem search = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(MainActivity.this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_search:
                // do some thing
                Toast.makeText(this, "this is SearchView", Toast.LENGTH_SHORT).show();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();
        return false;
    }

    private void clickStart() {
        mClickStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Song Start", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
