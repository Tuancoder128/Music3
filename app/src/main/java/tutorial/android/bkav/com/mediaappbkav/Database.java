package tutorial.android.bkav.com.mediaappbkav;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PHONG on 5/5/2018.
 */

public class Database extends SQLiteOpenHelper {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Context mContext;
    public static final String DATABASE_NAME = "media";
    public static final int VERSION = 1;
    public static final String TABLE_SONG = "song";
    public static final String POSITION_SONG = "position_song";
    public static final String DATA_SONG = "data_song";
    public static final String NAME_SONG = "name_song";
    public static final String DURATION_SONG = "duration_song";
    public static final String SINGER_SONG = "singer_song";
    public static final String THUMABNAIL_SONG = "thumabnail_song";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_SONG + " (" +
                POSITION_SONG + " integer primary key, " +
                DATA_SONG + " TEXT, " +
                NAME_SONG + " TEXT, " +
                DURATION_SONG + " TEXT, " +
                SINGER_SONG + " TEXT," +
                THUMABNAIL_SONG + " TEXT)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        onCreate(db);
    }

    public void deleteAllSong() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG, null, null);
        db.close();
    }

    public void addSong() {
        Cursor mCursor = null;
        String[] information = new String[]{
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.COMPOSER, // Người soạn nhạc
                MediaStore.Audio.Media.ALBUM_ID,
        };

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            mCursor = mContext.getContentResolver().query(uri, information, null, null, MediaStore.Audio.Media.TITLE);
        }

        if (mCursor == null) {
            return;
        }

        int indexData = mCursor.getColumnIndex(information[0]);
        int indexName = mCursor.getColumnIndex(information[1]);
        int indexDuration = mCursor.getColumnIndex(information[2]);
        int indexSinger = mCursor.getColumnIndex(information[3]);
        int indexAlbumId = mCursor.getColumnIndex(information[4]);

        mCursor.moveToFirst();

        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");

        int count = 0;
        while (!mCursor.isAfterLast()) {
            count++;
            String data = mCursor.getString(indexData);
            String name = mCursor.getString(indexName);
            String duaration = mCursor.getString(indexDuration);
            String singer = mCursor.getString(indexSinger);
            long idAlbum = mCursor.getLong(indexAlbumId);
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, idAlbum);

            SongItem songItem = new SongItem(count, data, name, duaration, singer, albumArtUri.toString());
            insert(songItem);
            mCursor.moveToNext();
        }
        mCursor.close();

    }

    private void insert(SongItem songItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POSITION_SONG, songItem.getmPositionItem());
        values.put(DATA_SONG, songItem.getmData());
        values.put(NAME_SONG, songItem.getmName());
        values.put(DURATION_SONG, songItem.getmDuration());
        values.put(SINGER_SONG, songItem.getmSingle());
        values.put(THUMABNAIL_SONG, songItem.getmUriThumabnail());
        db.insert(TABLE_SONG, null, values);
        db.close();
    }

    public List<SongItem> getAllSongItem() {
        List<SongItem> listSongItem = new ArrayList<SongItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SongItem songItem = new SongItem();
                songItem.setmPositionItem(cursor.getInt(0));
                songItem.setmData(cursor.getString(1));
                songItem.setmName(cursor.getString(2));
                songItem.setmDuration(cursor.getString(3));
                songItem.setmSingle(cursor.getString(4));
                songItem.setmUriThumabnail(cursor.getString(5));
                listSongItem.add(songItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listSongItem;
    }
}
