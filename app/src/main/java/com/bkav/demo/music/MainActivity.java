package com.bkav.demo.music;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private ArrayList<ThongTinBaiHat> arrayList;
    private AdapterBaiHat baiHatAdapter;
    public ListView mListBaiHat;
    private static final int MY_PERMISSION_REQUEST = 1;
    private static final int MY_REQUEST_CODE = 111;
    private static final int MY_RESULT_CODE = 000;
    private TextView mCLickTenBaiHat;
    private TextView mCLickCasy;
    private TextView mTime;
    private TextView mNameSong;
    private TextView mNumber;
    private ImageView mHinhAlbum;
    private ImageView mPopupMenu;
    private ImageView mImagePlaySong;
    public Button mClickStart;
    public MediaPlayer mediaPlayer;
    private RelativeLayout mLinearMoveSong;
    public ArrayList<String> mPath;
    private Intent intent;
    private Bundle bundle;
    public boolean iboundService = false;
    public ServiceMusic mserviceMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, ServiceMusic.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);


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
            mediaPlayer = new MediaPlayer();
            init();


        }
        getAllListMusic();
        clickSong();
        clickStartPauseSOng();
        clickMoveSong();


    }

    private void getAllListMusic() {
        mPath = new ArrayList<>();
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music";
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String s = files[i].getName();
            if (s.endsWith(".mp3")) {
                mPath.add(files[i].getAbsolutePath());
            }
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
            int songImage = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);


            do {
                String currentTittle = songCursor.getString(songTittle);
                String currentArist = songCursor.getString(songArtist);
                int currentAlbum = songCursor.getInt(songImage);
                int giay = songCursor.getInt(songTime) / 1000;

                int phut = giay / 60;
                int giayLe = giay - phut * 60;
                String giayle;
                if (giayLe < 10) {
                    giayle = "0" + giayLe;
                } else {
                    giayle = String.valueOf(giayLe);
                }

                arrayList.add(new ThongTinBaiHat(currentTittle, currentArist, i, currentAlbum, phut + ":" + giayle));

                i++;


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
                    }
                } else {
                    Toast.makeText(this, " No permission grandted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void init() {
        mHinhAlbum = (ImageView) findViewById(R.id.hinh_album);
        mPopupMenu = (ImageView) findViewById(R.id.other);
        mImagePlaySong = (ImageView) findViewById(R.id.image_icon_play_song);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTime = (TextView) findViewById(R.id.time);
        mNumber = (TextView) findViewById(R.id.number);
        mListBaiHat = (ListView) findViewById(R.id.list_album);
        mCLickTenBaiHat = (TextView) findViewById(R.id.click_tenbaihat);
        mNameSong = (TextView) findViewById(R.id.name_song);
        mCLickCasy = (TextView) findViewById(R.id.click_casy);
        mClickStart = (Button) findViewById(R.id.click_start);

        mLinearMoveSong = (RelativeLayout) findViewById(R.id.linear_move_song);

        arrayList = new ArrayList<>();
        getMusic();
        baiHatAdapter = new AdapterBaiHat(getApplicationContext(), R.layout.activity_baihat, arrayList);
        mListBaiHat.setAdapter(baiHatAdapter);


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
                Toast.makeText(this, "Bạn hãy nhập vào tên bài hát hoặc tên ca sỹ", Toast.LENGTH_SHORT).show();

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

        return false;
    }


    private void clickSong() {
        mListBaiHat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                mNumber.setVisibility(View.INVISIBLE);
//                mImagePlaySong.setVisibility(View.VISIBLE);
                String local = mPath.get(i);
                mserviceMusic.setLocalSongRunging(i);


                try {
                    mserviceMusic.setLocalSong(local);
                    mClickStart.setBackgroundResource(R.drawable.ic_media_pause_light);


                } catch (IOException e) {
                    e.printStackTrace();
                }


                mHinhAlbum.setImageResource(arrayList.get(i).getHinhAlbum());
                mCLickTenBaiHat.setText(arrayList.get(i).getTenBaiHat());
                mCLickCasy.setText(arrayList.get(i).getTheloai());




                MediaMetadataRetriever retriever = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                    retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(mPath.get(i));

                    String tenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String ten = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                    mCLickTenBaiHat.setText(ten);
                    mCLickCasy.setText(tenAlbum);

                }

                bundle = new Bundle();
                bundle.putInt("vitri", i);
                bundle.putStringArrayList("tenbai", mPath);


            }
        });
    }

    private void clickMoveSong() {
        mLinearMoveSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }

                int time = mediaPlayer.getCurrentPosition();
                bundle.putInt("thoigian", time);

                Intent imovesong = new Intent(MainActivity.this, DetailSongRuningActivity.class);
                imovesong.putExtra("dulieu", bundle);

                startActivityForResult(imovesong, 111);
            }
        });

    }

    private void clickStartPauseSOng() {
        mClickStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mserviceMusic.playpauseSong();
                if (mserviceMusic.mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mClickStart.setBackgroundResource(R.drawable.ic_media_pause_light);
                } else {
                    mediaPlayer.start();
                    mClickStart.setBackgroundResource(R.drawable.ic_play_black);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE && resultCode == MY_RESULT_CODE && data != null) {
            int time = data.getExtras().getInt("time");
            mediaPlayer.seekTo(time);
            mediaPlayer.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iboundService = true;
            ServiceMusic.MyBinder binder = (ServiceMusic.MyBinder) iBinder;
            mserviceMusic = binder.getService();


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iboundService = false;
        }
    };


}












