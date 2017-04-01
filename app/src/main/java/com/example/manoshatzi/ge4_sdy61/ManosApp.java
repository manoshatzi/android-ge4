package com.example.manoshatzi.ge4_sdy61;

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

/**
 * Created by manoshatzi on 18/03/2017.
 */

public class ManosApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2));
    }
}
