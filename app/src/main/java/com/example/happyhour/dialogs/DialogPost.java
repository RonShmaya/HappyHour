package com.example.happyhour.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.objects.Post;


public class DialogPost {
//    public interface Callback_add_review {
//        void review_to_add(Review review);
//    }

//    private TextInputEditText dialogAddReview_TIETL_description;
//    private MaterialButton dialogAddReview_BTN_create;
//    private RatingBar dialogAddReview_RAB_rating;
//    private Callback_add_review callback_add_review;
       private AppCompatImageView list_post_IMG;





    public void show(Context context, Post post){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_post);
        findViews(dialog,post,context);
        dialog.show();
    }

//    public DialogPost setCallback_add_table(Callback_add_review callback_add_review) {
//        this.callback_add_review = callback_add_review;
//        return this;
//    }

    private void findViews(Dialog dialog, Post post, Context context) {
//        dialogAddReview_TIETL_description = dialog.findViewById(R.id.dialogAddReview_TIETL_description);
//        dialogAddReview_BTN_create = dialog.findViewById(R.id.dialogAddReview_BTN_create);
//        dialogAddReview_RAB_rating = dialog.findViewById(R.id.dialogAddReview_RAB_rating);
//
//        dialogAddReview_BTN_create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (callback_add_review != null) {
//                    Review review = new Review()
//                            .setReviewer_name(DataManager.getDataManager().getPrivateAccount().getName())
//                            .setDescription(dialogAddReview_TIETL_description.getText().toString())
//                            .setId(UUID.randomUUID().toString())
//                            .setImg(DataManager.getDataManager().getPrivateAccount().getImgUri())
//                            .setStars_rating(dialogAddReview_RAB_rating.getRating());
//
//                    callback_add_review.review_to_add(review);
//                    dialog.dismiss();
//                }
//
//            }
//        });
        list_post_IMG = dialog.findViewById(R.id.list_post_IMG);
        Glide.with(context).load(post.getImg()).placeholder(R.drawable.img_bar_basic).into(list_post_IMG);
    }
}
