package com.example.instaclone;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.parceler.Parcel;

@ParseClassName("User")
@Parcel(analyze = User.class)
public class User extends ParseObject {

    public static final String KEY_USERNAME = "username";

    public User() {}

    public String getUsername(){
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username){
        put(KEY_USERNAME, username);
    }
}
