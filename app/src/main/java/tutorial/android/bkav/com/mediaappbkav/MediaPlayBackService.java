package tutorial.android.bkav.com.mediaappbkav;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by PHONG on 5/1/2018.
 */

public class MediaPlayBackService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public static final String UPDATE_CURRENT_SONG_ACTION = "MedlaPlaybackService.UPDATE_CURRENT_SONG_ACTION";

    private MediaPlayer mPlayer;
    private Database mDatabase;
    private List<SongItem> mPlayingSongs;

    private int mPosition;
    private boolean mCheckPlaySong;
    private boolean mCheckShuffer  = false;

    private IAudioPlayer mIAudioPlayer;

    public void setOnComplete(IAudioPlayer iAudioPlayer){
        this.mIAudioPlayer = iAudioPlayer;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = new Database(getApplicationContext());
        // Bkav QuangLH Review 20180507: can duoc truyen tu Fragment xuong, thong qua luc
        // goi play 1 bai khi duyet giao dien.
        mPlayingSongs = mDatabase.getAllSongItem();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void play(SongItem songItem) {

        mCheckPlaySong = true;
        mPosition = songItem.getmPositionItem();
        try {
            // Bkav QuangLH Review 20180507: khong tao lai MediaPlayer moi lan play. Tai sao phải the?
            createNotification();
            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // Mo nhac tu bo nho
            mPlayer.setDataSource(songItem.getmData());
            // HAT XONG THI VAO PHUONG THUC NAY
            mPlayer.setOnCompletionListener(this);

            // khi Prepare xong thi vao phuong thuc nay
            mPlayer.setOnPreparedListener(this);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNotification() {

        // Bkav QuangLH Review 20180507: tham khao them ve Media Style Notification o day.
        // https://www.binpress.com/tutorial/using-android-media-style-notifications-with-media-session-controls/165
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_music));
        notification.setContentTitle(getString(R.string.title));
        notification.setContentText(getString(R.string.title));
        notification.setContentIntent(pendingIntent);
        notification.setSmallIcon(R.drawable.ic_music);
        //startForeground(1, notification.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void previourSong() {
        mCheckPlaySong = true;
        if (mPosition == 0) {
            play(mPlayingSongs.get(mPlayingSongs.size() - 1));
        } else {
            play(mPlayingSongs.get((mPosition - 1) % mPlayingSongs.size()));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void nextSong() {
        mCheckPlaySong = true;
        play(mPlayingSongs.get((mPosition + 1) % mPlayingSongs.size()));

        Intent intent = new Intent(UPDATE_CURRENT_SONG_ACTION);
        sendBroadcast(intent);
    }

    public boolean checkPlaySong(){
        return mCheckPlaySong;
    }

    public void pause() {

        if (mCheckPlaySong) {
            mPlayer.pause();
            mCheckPlaySong = false;
        } else {
            mPlayer.start();
            mCheckPlaySong = true;
        }
    }

    public void seek(int position) {
        if (position < mPlayer.getDuration()) {
            mPlayer.seekTo(position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void repeat(){
        if(mCheckPlaySong){
            mPlayer.pause();
            Random random = new Random();
            int numberSong = random.nextInt(mPlayingSongs.size() -1);
            play(mPlayingSongs.get(numberSong));
        } if(mCheckPlaySong){
            mPlayer.pause();
            Random random = new Random();
            int numberSong = random.nextInt(mPlayingSongs.size() -1);
            play(mPlayingSongs.get(numberSong));
        }

    }

    public void shuffer(){
        mCheckShuffer = true;
        mPlayer.setOnCompletionListener(this);
    }

    public double getCurrentDuartion(){
        return mPlayer.getCurrentPosition();
    }
    public long getDuragion() {
        return mPlayer.getDuration();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(mCheckShuffer){
            mIAudioPlayer.complete(mPlayingSongs.get(mPosition));
        }else {
            mCheckShuffer = false;
            mIAudioPlayer.complete(mPlayingSongs.get((mPosition + 1) % mPlayingSongs.size()));
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public class MyBinder extends Binder {
        MediaPlayBackService getService() {
            return MediaPlayBackService.this;
        }
    }

    private void createNotification(SongItem songItem) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        String longText = "Bài hát này được coi như la  mottj tác phẩm của " + songItem.getmSingle()
                + "Mong mong nguoi nghe và cho lời nhận xét .";
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Song name :" + songItem.getmName())
                .setContentText("icon :").setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pIntent)
                .setStyle(new Notification.BigTextStyle().bigText(longText))
                .addAction(R.drawable.ic_music, "open", pIntent)
                .addAction(R.drawable.ic_music, "detail", pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, noti);
    }
}
