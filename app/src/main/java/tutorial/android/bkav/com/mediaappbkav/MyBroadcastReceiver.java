package tutorial.android.bkav.com.mediaappbkav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by PHONG on 5/6/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private Database mDatabase ;
    @Override
    public void onReceive(Context context, Intent intent) {
        mDatabase = new Database(context);
        mDatabase.deleteAllSong();
        mDatabase.addSong();
    }
}
