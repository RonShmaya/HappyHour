package com.example.happyhour.activities.business_account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.callbacks.Callback_upload_bar_imgs;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.eBarType;
import com.example.happyhour.objects.eMusicType;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyServices;
import com.example.happyhour.tools.MyStorage;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;


public class Activity_create_bar extends AppCompatActivity {
    private CircleImageView createBar_IMG_barPhoto;
    private FloatingActionButton createBar_FAB_profile_pic;
    private TextInputEditText createBar_TIETL_barName;
    private TextInputLayout createBar_TIL_barName;
    private TextInputEditText createBar_TIETL_HappyHour;
    private TextInputLayout createBar_TIL_HappyHour;
    private TextInputEditText createBar_TIETL_description;
    private TextInputLayout createBar_TIL_description;
    private TextInputLayout createBar_TIL_barType;
    private AutoCompleteTextView createBar_ACTV_barType;
    private ShapeableImageView createBar_IMG_menu;
    private FloatingActionButton createBar_FAB_upload_menu;
    private MaterialButton createBar_BTN_create;
    private ArrayList<String> barTypes;
    private ArrayAdapter<String> adapter;
    private ArrayList<Chip> musicTypesChips;
    private ArrayList<Chip> musicTypesChipsChecked = new ArrayList<>();
    private HashMap<EditText, TextInputLayout> verify_text_inputs = new HashMap<>();
    private MaterialTextView createBar_LBL_musicTypeError;
    private MaterialToolbar toolbar;
    private boolean isAllInputsOk = true;
    private Uri uriBarPhoto;
    private Uri uriMenuPhoto;
    private boolean isUploadBarPhoto;
    private String barID;
    private String urlMenuPhoto;
    private LottieAnimationView loading_animation_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bar);


        init_toolbar();
        findViews();
        MyStorage.getInstance().setCallback_upload_bar_imgs(callback_create_bar_img_upload);
        init_actions();

    }

    private void init_actions() {
        barTypes = DataManager.getDataManager().getBarTypesNames();
        Collections.sort(barTypes);
        adapter = new ArrayAdapter(this, R.layout.list_item, barTypes);
        createBar_ACTV_barType.setAdapter(adapter);

        createBar_BTN_create.setOnClickListener(btn_create_clicked);

        musicTypesChips.forEach(chip -> chip.setOnCheckedChangeListener(chip_clicked));

        createBar_FAB_profile_pic.setOnClickListener(view -> {
            isUploadBarPhoto = true;
            add_photo();
        });
        createBar_FAB_upload_menu.setOnClickListener(view -> {
            isUploadBarPhoto = false;
            add_photo();
        });

    }

    private void init_toolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_bar_account.class);
            startActivity(intent);
            finish();
        });
    }

    private void findViews() {
        createBar_IMG_barPhoto = findViewById(R.id.createBar_IMG_barPhoto);
        createBar_IMG_menu = findViewById(R.id.createBar_IMG_menu);
        createBar_FAB_upload_menu = findViewById(R.id.createBar_FAB_upload_menu);
        createBar_FAB_profile_pic = findViewById(R.id.createBar_FAB_profile_pic);
        createBar_TIETL_barName = findViewById(R.id.createBar_TIETL_barName);
        createBar_TIL_barName = findViewById(R.id.createBar_TIL_barName);
        createBar_TIETL_description = findViewById(R.id.createBar_TIETL_description);
        createBar_TIL_description = findViewById(R.id.createBar_TIL_description);
        createBar_TIL_barType = findViewById(R.id.createBar_TIL_barType);
        createBar_ACTV_barType = findViewById(R.id.createBar_ACTV_barType);
        createBar_BTN_create = findViewById(R.id.createBar_BTN_create);
        createBar_ACTV_barType = findViewById(R.id.createBar_ACTV_barType);
        createBar_TIETL_HappyHour = findViewById(R.id.createBar_TIETL_HappyHour);
        createBar_TIL_HappyHour = findViewById(R.id.createBar_TIL_HappyHour);
        createBar_LBL_musicTypeError = findViewById(R.id.createBar_LBL_musicTypeError);
        loading_animation_view = findViewById(R.id.loading_animation_view);


        musicTypesChips = new ArrayList<>(Arrays.asList(
                findViewById(R.id.chip_Hip_Hop),
                findViewById(R.id.chip_years_90),
                findViewById(R.id.chip_years_80),
                findViewById(R.id.chip_years_70),
                findViewById(R.id.chip_Electronic),
                findViewById(R.id.chip_israeli),
                findViewById(R.id.chip_Rock),
                findViewById(R.id.chip_Soul),
                findViewById(R.id.chip_Funk),
                findViewById(R.id.chip_Reggae),
                findViewById(R.id.chip_Jazz),
                findViewById(R.id.chip_Classic),
                findViewById(R.id.chip_Latin),
                findViewById(R.id.chip_Blues)
        ));

        verify_text_inputs.put(createBar_TIETL_barName, createBar_TIL_barName);
        verify_text_inputs.put(createBar_TIETL_description, createBar_TIL_description);
        verify_text_inputs.put(createBar_TIETL_HappyHour, createBar_TIL_HappyHour);
        verify_text_inputs.put(createBar_ACTV_barType, createBar_TIL_barType);
    }


    private void verifyInput(EditText text, TextInputLayout msgToUser) {
        if (text.getText().toString().isEmpty()) {
            msgToUser.setError("cannot be empty");
            isAllInputsOk = false;
            return;
        }
        msgToUser.setError("");
    }

    private View.OnClickListener btn_create_clicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isAllInputsOk = true;
            if(uriMenuPhoto == null){
                isAllInputsOk = false;
                MyServices.getInstance().makeToast("Please add menu");
            }
            if(uriBarPhoto == null){
                isAllInputsOk = false;
                MyServices.getInstance().makeToast("Please add main photo");
            }
            verify_text_inputs.forEach((k, v) -> verifyInput(k, v));

            if (musicTypesChipsChecked.size() <= 0) {
                isAllInputsOk = false;
                createBar_LBL_musicTypeError.setText("you must pick Music Type!!!");
            } else {
                createBar_LBL_musicTypeError.setText("");
            }
            if (isAllInputsOk) {
                barID = UUID.randomUUID().toString();
                loading_animation_view.setVisibility(View.VISIBLE);
                loading_animation_view.playAnimation();
                MyStorage.getInstance().uploadMenuBar(DataManager.getDataManager().getBusinessAccount().getId(), barID , uriMenuPhoto);
                MyStorage.getInstance().uploadImageBar(DataManager.getDataManager().getBusinessAccount().getId(), barID , uriBarPhoto);
            }

        }
    };

    private void finish_creating_bar(String url) {
        eBarType ebarType = eBarType.valueOf(createBar_ACTV_barType.getText().toString().replace(' ', '_'));
        ArrayList<eMusicType> emusicTypeList = new ArrayList<>();
        musicTypesChipsChecked.forEach(musicType -> emusicTypeList.add(eMusicType.valueOf(musicType.getText().toString().replace(' ', '_'))));
        Bar bar = new Bar()
                .setBarType(ebarType)
                .setDescription(createBar_TIETL_description.getText().toString())
                .setName(createBar_TIETL_barName.getText().toString())
                .setHappy_hour(createBar_TIETL_HappyHour.getText().toString())
                .setMusicTypes(emusicTypeList)
                .setId(barID)
                .setBar_photo(url)
                .setMenu_photo(urlMenuPhoto)
                .setOwner_id(DataManager.getDataManager().getBusinessAccount().getId());
        DataManager.getDataManager().addBusinessAccountBar(bar);
        Bundle bundle = new Bundle();
        bundle.putString(DataManager.EXTRA_BAR , bar.getId());
        Intent intent = new Intent(this, Activity_bar_tables.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private CompoundButton.OnCheckedChangeListener chip_clicked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b == true) {
                musicTypesChipsChecked.add((Chip) compoundButton);
            } else {
                musicTypesChipsChecked.remove((Chip) compoundButton);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            DataManager.getDataManager().logout();
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Intent intent = new Intent(this, Activity_user_connect.class);
        startActivity(intent);
        finish();
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
    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
        if (result.getResultCode() == RESULT_OK) {
            Uri uri = result.getData().getData();
            if(isUploadBarPhoto) {
                createBar_IMG_barPhoto.setImageURI(uri);
                uriBarPhoto = uri;
            }
            else{
                createBar_IMG_menu.setImageURI(uri);
                uriMenuPhoto = uri;
            }
        } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
            MyServices.getInstance().makeToast("image upload failed please, try again");
        }
    });
    private Callback_upload_bar_imgs callback_create_bar_img_upload = new Callback_upload_bar_imgs() {
        @Override
        public void main_img(String url) {
            finish_creating_bar(url);
        }

        @Override
        public void menu_img(String url) {
            urlMenuPhoto = url;
        }

        @Override
        public void post_img(String url) {
        }

        @Override
        public void failed() {
            MyServices.getInstance().makeToast("something went wrong please try again");
        }
    };

}