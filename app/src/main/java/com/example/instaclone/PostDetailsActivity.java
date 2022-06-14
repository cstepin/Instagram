package com.example.instaclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.Date;

public class PostDetailsActivity extends AppCompatActivity {

    TextView tvUsername;
    TextView tvDescription;
    TextView tvTimeAgo;
    ImageView ivImage;

    // the movie to display
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        tvUsername = findViewById(R.id.tvUsername);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimeAgo = findViewById(R.id.tvTimeAgo);
        ivImage = findViewById(R.id.ivImage);

        // unwrap the movie passed in via intent, using its simple name as a key
        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostDetailsActivity", "post successfully in details activity");

        // set the title and overview
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        if (post.getImage() != null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivImage);
        }

        Date createdAt = post.getCreatedAt();
        //Log.i();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        tvTimeAgo.setText(timeAgo);
    }
}