package tutorial.android.bkav.com.mediaappbkav;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.net.URI;

/**
 * Created by PHONG on 5/7/2018.
 */

public class MusicsProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "tutorial.android.bkav.com.provider.media";
    public static final String URL = "content://" + PROVIDER_NAME + "/song";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    public static final int SONGS = 1;
    public static final int SONGS_ID = 2;

    public static UriMatcher URI_MATCHER = null;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PROVIDER_NAME, "song", SONGS);
        URI_MATCHER.addURI(PROVIDER_NAME, "song/#", SONGS_ID);

    }

    private SQLiteDatabase mSqLiteDatabase;
    public static final String DATABASE_NAME = "media";
    public static final int VERSION = 1;
    public static final String TABLE_SONG = "song";
    public static final String POSITION_SONG = "position_song";
    public static final String DATA_SONG = "data_song";
    public static final String NAME_SONG = "name_song";
    public static final String DURATION_SONG = "duration_song";
    public static final String SINGER_SONG = "singer_song";
    public static final String THUMABNAIL_SONG = "thumabnail_song";


    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sqlQuery = "CREATE TABLE " + TABLE_SONG + " (" +
                    POSITION_SONG + " integer primary key, " +
                    DATA_SONG + " TEXT, " +
                    NAME_SONG + " TEXT, " +
                    DURATION_SONG + " TEXT, " +
                    SINGER_SONG + " TEXT," +
                    THUMABNAIL_SONG + " TEXT)";
            sqLiteDatabase.execSQL(sqlQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
            onCreate(sqLiteDatabase);
        }
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        mSqLiteDatabase = dbHelper.getWritableDatabase();
        return (mSqLiteDatabase == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_SONG);

        switch (URI_MATCHER.match(uri)){
            case SONGS:{
                break;
            }
            case SONGS_ID:{
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(s1 == null || s1 == ""){
            s1 = NAME_SONG;
        }

        Cursor c = qb.query(mSqLiteDatabase , strings , s , strings1 , null,  null, s1  );

        c.setNotificationUri(getContext().getContentResolver() , uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (URI_MATCHER.match(uri)){
            case SONGS:{
                return "";
            }

            case SONGS_ID :{
                return "";
            }
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long rowID = mSqLiteDatabase.insert(TABLE_SONG ,"", contentValues);

        if(rowID > 0){
            Uri uri_ = ContentUris.withAppendedId(CONTENT_URI , rowID);
            getContext().getContentResolver().notifyChange(uri_ , null);
            return uri_;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int count = 0 ;
        switch (URI_MATCHER.match(uri)){
            case SONGS:{
                count = mSqLiteDatabase.delete(TABLE_SONG , s , strings);
                break;
            }
            case SONGS_ID :{
                String id = uri.getPathSegments().get(1);
                count = mSqLiteDatabase.delete(TABLE_SONG , SONGS_ID +" = " + id +
                        (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings);

            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int count = 0;

        switch (URI_MATCHER.match(uri)){
            case SONGS:
                count = mSqLiteDatabase.update(TABLE_SONG, contentValues, s, strings);
                break;

            case SONGS_ID:
                count = mSqLiteDatabase.update(TABLE_SONG, contentValues, SONGS_ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(s) ? " AND (" +s + ')' : ""), strings);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
