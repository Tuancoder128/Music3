package tutorial.android.bkav.com.mediaappbkav;

import java.io.Serializable;

/**
 * Created by PHONG on 4/9/2018.
 */

public class SongItem implements Serializable {
    private int mPositionItem;
    private String mData;
    private String mName;
    private String mDuration;
    private String mSingle;
    private String mUriThumabnail;

    public SongItem(){

    }

    public SongItem(int mPositionItem, String mData, String mName, String mDuration, String mSingle, String mUriThumabnail) {
        this.mPositionItem = mPositionItem;
        this.mData = mData;
        this.mName = mName;
        this.mDuration = mDuration;
        this.mSingle = mSingle;
        this.mUriThumabnail = mUriThumabnail;
    }

    public int getmPositionItem() {
        return mPositionItem;
    }

    public void setmPositionItem(int mPositionItem) {
        this.mPositionItem = mPositionItem;
    }

    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmSingle() {
        return mSingle;
    }

    public void setmSingle(String mSingle) {
        this.mSingle = mSingle;
    }

    public String getmUriThumabnail() {
        return mUriThumabnail;
    }

    public void setmUriThumabnail(String mUriThumabnail) {
        this.mUriThumabnail = mUriThumabnail;
    }

    public String setCorrectDuration(String songs_duration) {

        if (Integer.valueOf(songs_duration) != null) {
            int time = Integer.valueOf(songs_duration);

            int seconds = time / 1000;
            int minutes = seconds / 60;
            seconds = seconds % 60;

            if (seconds < 10) {
                songs_duration = String.valueOf(minutes) + ":0" + String.valueOf(seconds);
            } else {

                songs_duration = String.valueOf(minutes) + ":" + String.valueOf(seconds);
            }
            return songs_duration;
        }
        return null;

    }


}
