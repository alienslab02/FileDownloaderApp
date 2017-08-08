package com.bilal.filedownloaderapp;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by applepc on 05/08/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public static String TAG = "DownloaderApp";

    public void LOG(String msg){
        Log.d(TAG, msg);
    }
}
