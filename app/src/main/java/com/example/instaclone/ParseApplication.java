package com.example.instaclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("8wd9RclL63tV3cAiSOdsJDQQ3cmA656wonbHPzC7")
                .clientKey("3pkViAtVJ4Fn8SRuCUVBOWgJnBfNojRjvUie3XY1")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
