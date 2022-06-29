package com.example.happyhour.tools;

import android.net.Uri;

import com.example.happyhour.callbacks.Callback_create_bar_img_upload;
import com.example.happyhour.callbacks.Callback_upload_profile_img;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyStorage {
    public static final String BUSINESS_ACCOUNTS = "BUSINESS_ACCOUNTS";
    public static final String PRIVATE_ACCOUNTS = "PRIVATE_ACCOUNTS";
    public static final String MENU = "MENU";
    public static final String MY_BARS = "MY_BARS";

    private static MyStorage _instance = new MyStorage();
    private Callback_upload_profile_img callback_upload_img;
    private Callback_create_bar_img_upload callback_create_bar_img_upload;
    private FirebaseStorage myStorage;
    private StorageReference ref_private_account;
    private StorageReference ref_business_account;


    private MyStorage() {
        myStorage = FirebaseStorage.getInstance();
        ref_private_account = myStorage.getReference(PRIVATE_ACCOUNTS);
        ref_business_account = myStorage.getReference(BUSINESS_ACCOUNTS);
    }

    public MyStorage setCallback_upload_img(Callback_upload_profile_img callback_upload_img) {
        this.callback_upload_img = callback_upload_img;
        return this;
    }
    public MyStorage setCallback_create_bar_img_upload(Callback_create_bar_img_upload callback_create_bar_img_upload) {
        this.callback_create_bar_img_upload = callback_create_bar_img_upload;
        return this;
    }

    public void uploadImageProfile(String photoId, Uri resultUri) {
        if (resultUri != null) {
            ref_private_account.child(photoId).putFile(resultUri)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ref_private_account.child(photoId).getDownloadUrl().addOnSuccessListener(uri -> {
                                if (callback_upload_img != null) {
                                    callback_upload_img.img_uploaded(uri.toString());
                                }
                            });
                        } else if (callback_upload_img != null) {
                            callback_upload_img.failed();
                        }
                    });
        }
    }


    public static MyStorage getInstance() {
        return _instance;
    }

    public void uploadImageBar(String accountId, String barID, Uri resultUri) {
        if (resultUri != null) {
            ref_business_account.child(accountId).child(MY_BARS).child(barID).putFile(resultUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ref_business_account.child(accountId).child(MY_BARS).child(barID).getDownloadUrl().addOnSuccessListener(uri -> {
                        if (callback_create_bar_img_upload != null) {
                            callback_create_bar_img_upload.main_img(uri.toString());
                        }
                    });
                } else if (callback_create_bar_img_upload != null) {
                    callback_create_bar_img_upload.failed();
                }
            });
        }
    }

    public void uploadMenuBar(String accountId, String barID, Uri uriMenuPhoto) {
        if (uriMenuPhoto != null) {
            ref_business_account.child(accountId).child(MY_BARS).child(MENU).child(barID).putFile(uriMenuPhoto).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ref_business_account.child(accountId).child(MY_BARS).child(MENU).child(barID).getDownloadUrl().addOnSuccessListener(uri -> {
                        if (callback_create_bar_img_upload != null) {
                            callback_create_bar_img_upload.menu_img(uri.toString());
                        }
                    });
                } else if (callback_create_bar_img_upload != null) {
                    callback_create_bar_img_upload.failed();
                }
            });
        }
    }
}
