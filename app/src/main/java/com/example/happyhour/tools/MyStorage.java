package com.example.happyhour.tools;

import android.net.Uri;

import com.example.happyhour.callbacks.Callback_upload_bar_imgs;
import com.example.happyhour.callbacks.Callback_upload_profile_img;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyStorage {
    public static final String BUSINESS_ACCOUNTS = "BUSINESS_ACCOUNTS";
    public static final String PRIVATE_ACCOUNTS = "PRIVATE_ACCOUNTS";
    public static final String MENU = "MENU";
    public static final String POSTS = "POSTS";
    public static final String MY_BARS = "MY_BARS";
    public static final String BAR_PHOTO = "BAR_PHOTO";

    private static MyStorage _instance = new MyStorage();
    private Callback_upload_profile_img callback_upload_profile_img;
    private Callback_upload_bar_imgs callback_upload_bar_imgs;
    private FirebaseStorage myStorage;
    private StorageReference ref_private_account;
    private StorageReference ref_business_account;


    private MyStorage() {
        myStorage = FirebaseStorage.getInstance();
        ref_private_account = myStorage.getReference(PRIVATE_ACCOUNTS);
        ref_business_account = myStorage.getReference(BUSINESS_ACCOUNTS);
    }

    public MyStorage setCallback_upload_profile_img(Callback_upload_profile_img callback_upload_profile_img) {
        this.callback_upload_profile_img = callback_upload_profile_img;
        return this;
    }

    public MyStorage setCallback_upload_bar_imgs(Callback_upload_bar_imgs callback_upload_bar_imgs) {
        this.callback_upload_bar_imgs = callback_upload_bar_imgs;
        return this;
    }

    public void uploadImageProfile(String photoId, Uri resultUri) {
        if (resultUri != null) {
            ref_private_account.child(photoId).putFile(resultUri)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ref_private_account.child(photoId).getDownloadUrl().addOnSuccessListener(uri -> {
                                if (callback_upload_profile_img != null) {
                                    callback_upload_profile_img.img_uploaded(uri.toString());
                                }
                            });
                        } else if (callback_upload_profile_img != null) {
                            callback_upload_profile_img.failed();
                        }
                    });
        }
    }


    public static MyStorage getInstance() {
        return _instance;
    }

    public void uploadImageBar(String accountId, String barID, Uri resultUri) {
        if (resultUri != null) {
            ref_business_account.child(accountId).child(MY_BARS).child(barID).child(BAR_PHOTO).putFile(resultUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ref_business_account.child(accountId).child(MY_BARS).child(barID).child(BAR_PHOTO).getDownloadUrl().addOnSuccessListener(uri -> {
                        if (callback_upload_bar_imgs != null) {
                            callback_upload_bar_imgs.main_img(uri.toString());
                        }
                    });
                } else if (callback_upload_bar_imgs != null) {
                    callback_upload_bar_imgs.failed();
                }
            });
        }
    }

    public void uploadMenuBar(String accountId, String barID, Uri uriMenuPhoto) {
        if (uriMenuPhoto != null) {
            ref_business_account.child(accountId).child(MY_BARS).child(barID).child(MENU).putFile(uriMenuPhoto).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ref_business_account.child(accountId).child(MY_BARS).child(barID).child(MENU).getDownloadUrl().addOnSuccessListener(uri -> {
                        if (callback_upload_bar_imgs != null) {
                            callback_upload_bar_imgs.menu_img(uri.toString());
                        }
                    });
                } else if (callback_upload_bar_imgs != null) {
                    callback_upload_bar_imgs.failed();
                }
            });
        }
    }

    public void uploadPost(String accountId, String barId, String postId, Uri postUri) {
        if (postUri != null) {
            ref_business_account.child(accountId).child(MY_BARS).child(barId).child(POSTS).child(postId).putFile(postUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ref_business_account.child(accountId).child(MY_BARS).child(barId).child(POSTS).child(postId).getDownloadUrl().addOnSuccessListener(uri -> {
                        if (callback_upload_bar_imgs != null) {
                            callback_upload_bar_imgs.post_img(uri.toString());
                        }
                    });
                } else if (callback_upload_bar_imgs != null) {
                    callback_upload_bar_imgs.failed();
                }
            });
        }
    }

    public void delete_post(String accountId, String barId, String postId) {
        ref_business_account.child(accountId).child(MY_BARS).child(barId).child(POSTS).child(postId).delete();
    }
    public void delete_bar(String accountId, String barId) {
        ref_business_account.child(accountId).child(MY_BARS).child(barId).child(MENU).delete();
        ref_business_account.child(accountId).child(MY_BARS).child(barId).child(BAR_PHOTO).delete();
    }


}
