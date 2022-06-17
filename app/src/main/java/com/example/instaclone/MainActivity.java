package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instaclone.fragments.ComposeFragment;
import com.example.instaclone.fragments.PostsFragment;
import com.example.instaclone.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();

    // adapter
    private static MainActivity.clickDetails cR;

//For each row, need to inflate the layout

    public interface clickDetails
    {
        void onClickReplyReaction(ParseUser parseUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cR = new clickDetails() {
            @Override
            public void onClickReplyReaction(ParseUser parseUser) {

            }
        };

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        Log.i("in here 3", ParseUser.getCurrentUser().toString());
                        MainActivity.cR.onClickReplyReaction(ParseUser.getCurrentUser());
                        fragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
                     //   Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_compose:
                        Log.i("in here 4", ParseUser.getCurrentUser().toString());
                        fragment = new ComposeFragment();
                     //   Toast.makeText(MainActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_home:
                        Log.i("in here 5", ParseUser.getCurrentUser().toString());
                        fragment = new PostsFragment();
                    //    Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_logout:
                        onLogout();
                    default: return true;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    private void toViewFeed() {
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
    }

    private void onLogout() {
        Toast.makeText(MainActivity.this, "logging out", Toast.LENGTH_LONG).show();
      //  InstaClone.getRestClient(this).clearAccessToken();
        // navigate backwards to Login screen
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
        startActivity(i);
        ParseUser.logOutInBackground();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        finish();
    }
}