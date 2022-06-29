package com.example.happyhour.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;


public class DialogMenu {

    private AppCompatImageView menu_IMG;




    public void show(Context context, String url){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_menu);
        findViews(dialog,url,context);
        dialog.show();
    }


    private void findViews(Dialog dialog, String url, Context context) {
        menu_IMG = dialog.findViewById(R.id.menu_IMG);
        Glide.with(context).load(url).placeholder(R.drawable.img_menu).into(menu_IMG);
    }
}
