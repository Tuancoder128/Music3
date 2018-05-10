package tutorial.android.bkav.com.mediaappbkav;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tutorial.android.bkav.com.mediaappbkav.controller.LayoutController;
import tutorial.android.bkav.com.mediaappbkav.controller.OneColumnLayoutController;
import tutorial.android.bkav.com.mediaappbkav.controller.TwoColumeLayoutController;

public class MainActivity extends AppCompatActivity {

    private LayoutController mLayoutController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration configuration = getResources().getConfiguration();

        mLayoutController = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
                ? new OneColumnLayoutController(this)
                : new TwoColumeLayoutController(this);

        mLayoutController.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mLayoutController.onCreateOptionsMenu(menu);
    }
}
