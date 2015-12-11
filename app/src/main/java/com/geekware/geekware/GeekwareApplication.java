package com.geekware.geekware;

import android.app.Application;

import com.parse.Parse;

/**
 * This class will initialise the parse APIs
 * It is created once by android OS when our app is launched.
 */
public class GeekwareApplication extends Application {
    //TODO: these will be filled after creating the app on parse.com
    private static final String APP_ID = "";
    private static final String CLIENT_KEY = "";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, APP_ID, CLIENT_KEY);
    }
}
