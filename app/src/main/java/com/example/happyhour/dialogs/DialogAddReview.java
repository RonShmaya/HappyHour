package com.example.happyhour.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RatingBar;

import com.example.happyhour.R;
import com.example.happyhour.objects.Review;
import com.example.happyhour.tools.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;


public class DialogAddReview {
    public interface Callback_add_review {
        void review_to_add(Review review);
    }

    private TextInputEditText dialogAddReview_TIETL_description;
    private MaterialButton dialogAddReview_BTN_create;
    private RatingBar dialogAddReview_RAB_rating;
    private Callback_add_review callback_add_review;




    public void show(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_review);
        findViews(dialog);
        dialog.show();
    }

    public DialogAddReview setCallback_add_table(Callback_add_review callback_add_review) {
        this.callback_add_review = callback_add_review;
        return this;
    }

    private void findViews(Dialog dialog) {
        dialogAddReview_TIETL_description = dialog.findViewById(R.id.dialogAddReview_TIETL_description);
        dialogAddReview_BTN_create = dialog.findViewById(R.id.dialogAddReview_BTN_create);
        dialogAddReview_RAB_rating = dialog.findViewById(R.id.dialogAddReview_RAB_rating);

        dialogAddReview_BTN_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (callback_add_review != null) {
                    Review review = new Review()
                            .setReviewer_name(DataManager.getDataManager().getPrivateAccount().getName())
                            .setDescription(dialogAddReview_TIETL_description.getText().toString())
                            .setId(UUID.randomUUID().toString())
                            .setImg(DataManager.getDataManager().getPrivateAccount().getImgUri())
                            .setStars_rating(dialogAddReview_RAB_rating.getRating());

                    callback_add_review.review_to_add(review);
                    dialog.dismiss();
                }

            }
        });
    }
}
