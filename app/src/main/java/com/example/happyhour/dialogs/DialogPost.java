package com.example.happyhour.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.adapters.PostsAdapter;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.Post;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class DialogPost {
    public interface Callback_clicked_post_enter_bar {
        void post_clicked(Bar bar);
    }

    private MaterialTextView post_LBL_barName;
    private CircleImageView post_IMG_barImg;
    private AppCompatImageView post_IMG;
    private MaterialTextView post_LBL_likes;
    private MaterialButton post_BTN_follow_or_remove;
    private DataManager.eUserTypes userType;
    private Callback_clicked_post_enter_bar callback_clicked_post;
    private Context context;
    private Bar bar;
    private Post post;
    private ArrayList posts;
    private PostsAdapter postsAdapter;

    public DialogPost setCallback_clicked_post(Callback_clicked_post_enter_bar callback_clicked_post) {
        this.callback_clicked_post = callback_clicked_post;
        return this;
    }

    public void show(Context context, Bar bar, Post post , ArrayList posts , PostsAdapter postsAdapter) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_post);
        this.context = context;
        this.bar = bar;
        this.post = post;
        this.posts = posts;
        this.postsAdapter = postsAdapter;
        findViews(dialog);
        dialog.show();
    }

    private void findViews(Dialog dialog) {
        post_LBL_barName = dialog.findViewById(R.id.post_LBL_barName);
        post_LBL_barName.setText(bar.getName());

        post_IMG_barImg = dialog.findViewById(R.id.post_IMG_barImg);
        Glide.with(context).load(bar.getBar_photo()).placeholder(R.drawable.img_placeholder).into(post_IMG_barImg);

        post_IMG = dialog.findViewById(R.id.post_IMG);
        Glide.with(context).load(post.getImg()).placeholder(R.drawable.img_bar_basic).into(post_IMG);

        post_LBL_likes = dialog.findViewById(R.id.post_LBL_likes);
        post_LBL_likes.setText("Likes: "+post.getLikes().size());

        post_BTN_follow_or_remove = dialog.findViewById(R.id.post_BTN_follow_or_remove);
        userType = DataManager.getDataManager().getUserType();

        if(userType == DataManager.eUserTypes.Business){
            init_business_account(dialog);
        }
        else if(userType == DataManager.eUserTypes.Private){
            init_private_account(dialog);
        }

    }

    private void init_private_account(Dialog dialog) {
        if(isLike()){
            set_btn_icon(context.getDrawable(R.drawable.ic_like));
        }
        else{
            set_btn_icon(context.getDrawable(R.drawable.ic_not_like));
        }
        post_BTN_follow_or_remove.setOnClickListener(like_btn_clicked);
        post_LBL_barName.setOnClickListener(post_clicked);
        post_IMG_barImg.setOnClickListener(post_clicked);
        post_IMG.setOnClickListener(post_clicked);
    }

    private void init_business_account(Dialog dialog) {
        set_btn_icon(context.getDrawable(R.drawable.ic_minus));
        post_BTN_follow_or_remove.setOnClickListener(view -> {
            posts.remove(post);
            postsAdapter.notifyDataSetChanged();
            bar.getPosts().remove(post.getPost_id());
            MyDB.getInstance().remove_bar_post(bar , post);
            dialog.dismiss();
        });
    }
    public boolean isLike(){
        return post.getLikes().containsKey(DataManager.getDataManager().getPrivateAccount().getId());
    }
    public void set_btn_icon(Drawable drawable){
        post_BTN_follow_or_remove.setIcon(drawable);
    }
    private View.OnClickListener like_btn_clicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String userId = DataManager.getDataManager().getPrivateAccount().getId();
            String userName = DataManager.getDataManager().getPrivateAccount().getName();
            if(isLike()){
                post.removeLikes(userId);
                set_btn_icon(context.getDrawable(R.drawable.ic_not_like));
                post_LBL_likes.setText("Likes: "+post.getLikes().size());
                MyDB.getInstance().unLike(bar , post, userId);
            }
            else{
                post.addLikes(userId , userName);
                set_btn_icon(context.getDrawable(R.drawable.ic_like));
                post_LBL_likes.setText("Likes: "+post.getLikes().size());
                MyDB.getInstance().like(bar , post, userId, userName);
            }
        }
    };
    private View.OnClickListener post_clicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(callback_clicked_post != null){
                callback_clicked_post.post_clicked(bar);
            }
        }
    };
}
