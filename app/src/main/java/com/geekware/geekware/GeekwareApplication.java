package com.geekware.geekware;

import android.app.Application;

import com.parse.Parse;

/**
 * This class will initialise the parse APIs
 * It is instantiated once by android OS when our app is launched.
 */
public class GeekwareApplication extends Application {
    private static final String APP_ID = "1i9zwapMoXmMkxByhj0XRGX8plvfWmGpf0VvxfFf";
    private static final String CLIENT_KEY = "mmLshv8ixM0uYKloYj1Q7SWcfIP9PRequw0aMzZv";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, APP_ID, CLIENT_KEY);
    }
}
