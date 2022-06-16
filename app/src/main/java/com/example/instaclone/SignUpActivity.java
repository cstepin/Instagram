package com.example.instaclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;
import java.util.concurrent.Future;

public class SignUpActivity extends AppCompatActivity {

    EditText etPassword;
    EditText etUsername;
    Button btnSignUp;
    final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();

                if(!queryUsers(username)){
                    Toast.makeText(SignUpActivity.this, "Username already taken!", Toast.LENGTH_LONG).show();
                    return;
                }

                ParseQuery<User> query = ParseQuery.getQuery(User.class);
                ParseUser.getQuery();

                String password = etPassword.getText().toString();
                signUpUser(username, password);
            }
        });
    }

    private boolean queryUsers(String username) {
        // specify what type of data we want to query - Post.class
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        // include data referred by user key
        query.include(User.KEY_USERNAME);
        final boolean[] to_return = {true};
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting users", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (User user : users) {
                    if(user.getUsername().equals(username)){
                        to_return[0] = false;
                    }
                }

                // save received posts to list and notify adapter of new data
            }
        });
        return to_return[0];
    }

    private void signUpUser(String username, String password) {
        ParseUser user = new ParseUser();
// Set fields for the user to be created
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(i);
                    // Hooray! Let them use the app now.
                } else {
                    Log.e(TAG, "sign in unsuccesful", e);
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    /*
    new Handler().postDelayed(new Runnable(){
        @Override
        public void run(){
            Intent ...
        }
    }, 2000);
     */
}