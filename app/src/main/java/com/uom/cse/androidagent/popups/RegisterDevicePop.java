package com.uom.cse.androidagent.popups;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.uom.cse.androidagent.R;

/**
 * Created by Nirushan on 11/2/2015.
 */
public class RegisterDevicePop extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_device_pop);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.4));
    }
}
