package com.bkav.demo.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by vst on 16/05/2018.
 */

public class MyBroastReceiver extends BroadcastReceiver {

    private Bundle mBundle;
    private static final String NAME_TITLE = "tenbaihat";
    private static final String NAME_ARTIST = "tenalbum";
    private static final String DATA_BUNDLER = "dataBunder";

    public static IgetDataFromBroadCast mIgetDataFromBroadCast;

    public interface IgetDataFromBroadCast {
        void sendDataFromBroadCast(Bundle mBundle);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//
//        mBundle = intent.getBundleExtra(DATA_BUNDLER);
//        Log.d("BBB", String.valueOf(DATA_BUNDLER));
//        String mTenBaiHat = mBundle.getString(NAME_TITLE);
//        String mTenAlbum = mBundle.getString(NAME_ARTIST);
//        Toast.makeText(context, " " + mTenBaiHat + mTenAlbum, Toast.LENGTH_SHORT).show();
//        mIgetDataFromBroadCast.sendDataFromBroadCast(mBundle);
    }

//    public void registerCallback(IgetDataFromBroadCast mIgetDataFromBroadCast) {
//        this.mIgetDataFromBroadCast = mIgetDataFromBroadCast;
//    }


}












