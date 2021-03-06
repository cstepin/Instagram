package com.example.instaclone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instaclone.fragments.ProfileFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    // adapter
    private static clickDetails cR;

//For each row, need to inflate the layout

    public interface clickDetails
    {
        void onClickReplyReaction(ParseUser parseUser);
    }

    public PostsAdapter(Context context, List<Post> posts, clickDetails cR) {
        this.context = context;
        this.posts = posts;
        this.cR = cR;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUsername;
        TextView tvDescription;
        ImageView ivImage;
        ImageView ivLikeButton;
        View itemView;
        TextView tvNumLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivLikeButton = itemView.findViewById(R.id.ivLikeButton);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvNumLikes = itemView.findViewById(R.id.tvNumLikes);
        }

        public void bind(Post post) throws JSONException {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();

            //Need to check if the user is in the list of liked by the post
            JSONArray likeArray2 = post.getJSONArray("likes");
            assert likeArray2 != null;
            Log.i("tagtag", String.valueOf(likeArray2.length()));
            tvNumLikes.setText(String.valueOf(likeArray2.length()));
            if(ParseUser.getCurrentUser() == null){
                return;
            }
         //   Log.i("objectId", ParseUser.getCurrentUser().getObjectId().toString() + " and array: " + likeArray2.toString());
            if(jsonHasString(likeArray2, ParseUser.getCurrentUser().getObjectId()) == -1) {
                Glide.with(context).load(R.drawable.ufi_heart).into(ivLikeButton);
            }
            else{
                Glide.with(context).load(R.drawable.ufi_heart_active).into(ivLikeButton);
            }

            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("postsadapter", "in username on click");
                    onProfileClick(post);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment newFragment = ProfileFragment.newInstance(post.getUser());
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, newFragment).addToBackStack(null).commit();
                    PostsAdapter.cR.onClickReplyReaction(post.getUser());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                    // show the activity
                    context.startActivity(intent);
                }
            });

            ivLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        clickLike(post);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {

                Log.i("postsadapter", "in if onclick");
                // get the movie at the position, this won't work if the class is static
                Post post = posts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, PostDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                // show the activity
                context.startActivity(intent);
            }
        }

        public void clickLike(Post post) throws JSONException {
            JSONArray likeArray = post.getJSONArray("likes");
            assert likeArray != null;
            int position = jsonHasString(likeArray, ParseUser.getCurrentUser().getObjectId());
       //     Log.i("Postsadapter likeArray", likeArray.toString());
            // First examine the case where the image has not been liked
            if(position == -1){
                //Need to add the thing to the list
                likeArray.put(post.getUser().getObjectId());
        //        Log.i("Postsadapter likeArray2", likeArray.toString());
                Glide.with(context).load(R.drawable.ufi_heart_active).into(ivLikeButton);
            }
            //Else, we can find the user in the list of liked people, so we unlike the post
            else{
         //       Log.i("Postsadapter likeArray2", "in dislike");
                likeArray.remove(position);
                Glide.with(context).load(R.drawable.ufi_heart).into(ivLikeButton);
            }
            tvNumLikes.setText(String.valueOf(likeArray.length()));
            post.put("likes", likeArray);
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        Log.e("postsadapter", "error saving likes", e);
                    }
                    else{
                        Log.i("postsadapter", "liked successfully");
                    }
                }
            });
        }

        private int jsonHasString(JSONArray likeArray, String objectId) throws JSONException {
            for(int i = 0; i < likeArray.length(); i++){
                if(likeArray.get(i).toString().equals(objectId)){
                    return i;
                }
            }
            return -1;
        }
    }

    private void onProfileClick(Post post) {
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
