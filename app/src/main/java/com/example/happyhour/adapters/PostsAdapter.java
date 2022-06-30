package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.objects.Post;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static enum eList {list_post, list_search }
    private eList elst;

    public interface PostListener {
        void clicked(Post post, int position);
    }

    private Activity activity;
    private PostListener postListener;
    private ArrayList<Post> posts = new ArrayList<>();

    public PostsAdapter(Activity activity, ArrayList<Post> posts, eList elst ){
        this.activity = activity;
        this.posts = posts;
        this.elst = elst;
    }

    public PostsAdapter setPostListener(PostListener postListener) {
        this.postListener = postListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(this.elst == eList.list_post)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post, parent, false);
        else if(this.elst == eList.list_search)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_img, parent, false);

        PostHolder barHolder = new PostHolder(view);
        return barHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final PostHolder holder = (PostHolder) viewHolder;
        Post post = getItem(position);

        Glide.with(activity).load(post.getImg()).placeholder(R.drawable.img_placeholder).into(holder.list_post_IMG);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public Post getItem(int position) {
        return posts.get(position);
    }


    class PostHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView list_post_IMG;
        private LinearLayout list_post_LLT;

        public PostHolder(View itemView) {
            super(itemView);
            list_post_IMG = itemView.findViewById(R.id.list_post_IMG);

            itemView.setOnClickListener(view -> {
                if (postListener != null) {
                    postListener.clicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });

        }

    }
}