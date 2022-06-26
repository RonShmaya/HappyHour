package com.example.happyhour.activities.private_account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.callbacks.Callback_upload_profile_img;
import com.example.happyhour.objects.eBarType;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyServices;
import com.example.happyhour.tools.MyStorage;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;


public class Activity_private_account_profile extends AppCompatActivity {


    private MaterialToolbar toolbar;
    private CircleImageView profile_IMG_photo;
    private FloatingActionButton profile_FAB_upload_pic;
    private MaterialTextView profile_LBL_name;
    private TextInputLayout profile_TIL_favorite1;
    private AutoCompleteTextView profile_ACTV_favorite1;
    private TextInputLayout profile_TIL_favorite2;
    private AutoCompleteTextView profile_ACTV_favorite2;
    private MaterialButton profile_BTN_create;
    private boolean is_first_time_user = false;
    private ArrayList<String> barTypes_fav_1;
    private ArrayList<String> barTypes_fav_2;
    private FloatingActionButton fab_search;
    private BottomNavigationView nav_view;
    private String imgUri = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_account_profile);

        init_toolbar();
        findViews();
        MyStorage.getInstance().setCallback_upload_img(callback_upload_img);

    }

    private void findViews() {
        profile_IMG_photo = findViewById(R.id.profile_IMG_photo);
        profile_FAB_upload_pic = findViewById(R.id.profile_FAB_upload_pic);
        profile_LBL_name = findViewById(R.id.profile_LBL_name);
        profile_TIL_favorite1 = findViewById(R.id.profile_TIL_favorite1);
        profile_ACTV_favorite1 = findViewById(R.id.profile_ACTV_favorite1);
        profile_TIL_favorite2 = findViewById(R.id.profile_TIL_favorite2);
        profile_ACTV_favorite2 = findViewById(R.id.profile_ACTV_favorite2);
        profile_BTN_create = findViewById(R.id.profile_BTN_create);

        profile_LBL_name.setText("Name: "+DataManager.getDataManager().getPrivateAccount().getName());
        is_first_time_user = DataManager.getDataManager().getPrivateAccount().getFavorite_1() == null;
        init_favorite(profile_ACTV_favorite1);
        init_favorite(profile_ACTV_favorite2);

        String userUri = DataManager.getDataManager().getPrivateAccount().getImgUri();
        if(!userUri.isEmpty())
            Glide.with(this).load(userUri).placeholder(R.drawable.ic_user).into(profile_IMG_photo);

        profile_BTN_create.setOnClickListener(view -> save_clicked());
        profile_FAB_upload_pic.setOnClickListener(view -> {
            add_photo();
        });

    }

    private void add_photo() {
        ImagePicker.Companion.with(this)
                .crop()
                .cropOval()
                .maxResultSize(512, 512, true)
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog((Function1) (new Function1() {
                    public Object invoke(Object var1) {
                        this.invoke((Intent) var1);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(@NotNull Intent it) {
                        Intrinsics.checkNotNullParameter(it, "it");
                        launcher.launch(it);
                    }
                }));
    }

    private void save_clicked() {
        //photo is not must
        profile_TIL_favorite1.setError("");
        profile_TIL_favorite2.setError("");
        boolean input_ok = true;
        if (profile_ACTV_favorite1.getText().toString().isEmpty()) {
            profile_TIL_favorite1.setError("Cannot Be Empty");
            input_ok = false;
        }
        if (profile_ACTV_favorite2.getText().toString().isEmpty()) {
            profile_TIL_favorite2.setError("Cannot Be Empty");
            input_ok = false;
        }
        if (!input_ok)
            return;
        if (profile_ACTV_favorite2.getText().toString().equals(profile_ACTV_favorite1.getText().toString())) {
            MyServices.getInstance().makeToast("please pick different favorites");
            return;
        }

        eBarType fav_1 = eBarType.valueOf(profile_ACTV_favorite1.getText().toString().replace(' ', '_'));
        eBarType fav_2 = eBarType.valueOf(profile_ACTV_favorite2.getText().toString().replace(' ', '_'));
        if(imgUri.isEmpty()){
            imgUri =  DataManager.getDataManager().getPrivateAccount().getImgUri();
        }
        DataManager.getDataManager().set_private_account_details(fav_1, fav_2,imgUri);
        if(is_first_time_user)
            go_next(Activity_customer_main_page.class);
        MyServices.getInstance().makeToast("Saved...");
    }

    private void init_favorite(AutoCompleteTextView actv_to_show) {
        ArrayList<String> fav_list = DataManager.getDataManager().getBarTypesNames();
        Collections.sort(fav_list);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.list_item, fav_list);
        actv_to_show.setAdapter(adapter);

    }


    private void init_toolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("");

        nav_view = findViewById(R.id.nav_view);


        nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(is_first_time_user){
                    MyServices.getInstance().makeToast("Please Enter Favorites First");
                }
                else if(item.getItemId() == R.id.action_home){
                    go_next(Activity_customer_main_page.class);
                }
                else if(item.getItemId() == R.id.action_follow){
                    go_next(Activity_follow_bar.class);
                }
                else if(item.getItemId() == R.id.action_order){
                    go_next(Activity_my_reservations.class);
                }
                else if(item.getItemId() == R.id.action_profile){
                    //stay here
                }
                return false;
            }
        });
        fab_search = findViewById(R.id.fab_search);
        fab_search.setOnClickListener(view -> {
            go_next(Activity_customer_main_page.class); //todo change it!
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        MyServices.getInstance().makeToast("logout...");
        DataManager.getDataManager().logout();
        Intent intent = new Intent(this, Activity_user_connect.class);
        startActivity(intent);
        finish();
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
        if (result.getResultCode() == RESULT_OK) {
            Uri uri = result.getData().getData();
            profile_IMG_photo.setImageURI(uri);
            MyStorage.getInstance().uploadImageProfile(DataManager.getDataManager().getPrivateAccount().getId() , uri);
        } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
            MyServices.getInstance().makeToast("image upload failed please, try again");
        }
    });
    private Callback_upload_profile_img callback_upload_img = new Callback_upload_profile_img() {
        @Override
        public void img_uploaded(String url) {
            imgUri = url;
        }
        @Override
        public void failed() {
            MyServices.getInstance().makeToast("image upload failed please, try again");
        }
    };
}