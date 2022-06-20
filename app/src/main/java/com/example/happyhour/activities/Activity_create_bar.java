package com.example.happyhour.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.happyhour.R;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.eBarType;
import com.example.happyhour.objects.eMusicType;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Activity_create_bar extends AppCompatActivity {
    private TextInputEditText createBar_TIETL_barName;
    private TextInputLayout createBar_TIL_barName;
    private TextInputEditText createBar_TIETL_description;
    private TextInputLayout createBar_TIL_description;
    private TextInputLayout createBar_TIL_barType;
    private AutoCompleteTextView createBar_ACTV_barType;
    private MaterialButton createBar_BTN_create;
    private ArrayList<String> barTypes;
    private ArrayAdapter<String> adapter;
    private ArrayList<Chip> musicTypesChips;
    private ArrayList<Chip> musicTypesChipsChecked = new ArrayList<>();
    private HashMap<EditText, TextInputLayout> verify_text_inputs = new HashMap<>();
    private MaterialTextView createBar_LBL_musicTypeError;
    private MaterialToolbar toolbar;
    private boolean isAllInputsOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bar);

        init_toolbar();
        findViews();
        init_actions();

    }

    private void init_actions() {
        barTypes = DataManager.getDataManager().getBarTypesNames();
        Collections.sort(barTypes);
        adapter = new ArrayAdapter(this, R.layout.list_item, barTypes);
        createBar_ACTV_barType.setAdapter(adapter);

        createBar_BTN_create.setOnClickListener(btn_create_clicked);

        musicTypesChips.forEach(chip -> chip.setOnCheckedChangeListener(chip_clicked));

    }

    private void init_toolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
    private void findViews() {
        createBar_TIETL_barName = findViewById(R.id.createBar_TIETL_barName);
        createBar_TIL_barName = findViewById(R.id.createBar_TIL_barName);
        createBar_TIETL_description = findViewById(R.id.createBar_TIETL_description);
        createBar_TIL_description = findViewById(R.id.createBar_TIL_description);
        createBar_TIL_barType = findViewById(R.id.createBar_TIL_barType);
        createBar_ACTV_barType = findViewById(R.id.createBar_ACTV_barType);
        createBar_BTN_create = findViewById(R.id.createBar_BTN_create);
        createBar_ACTV_barType = findViewById(R.id.createBar_ACTV_barType);
        createBar_LBL_musicTypeError = findViewById(R.id.createBar_LBL_musicTypeError);

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

        verify_text_inputs.put(createBar_TIETL_barName , createBar_TIL_barName);
        verify_text_inputs.put(createBar_TIETL_description , createBar_TIL_description);
        verify_text_inputs.put(createBar_ACTV_barType , createBar_TIL_barType);
    }


    private void verifyInput(EditText text, TextInputLayout msgToUser) {
        if(text.getText().toString().isEmpty()){
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
            verify_text_inputs.forEach( (k , v) -> verifyInput(k , v));

            if(musicTypesChipsChecked.size() <= 0){
                isAllInputsOk = false;
                createBar_LBL_musicTypeError.setText("you must pick Music Type!!!");
            }
            else{
                createBar_LBL_musicTypeError.setText("");
            }
            if(isAllInputsOk){
                finish_creating_bar();
            }

        }
    };

    private void finish_creating_bar() {
        eBarType ebarType = eBarType.valueOf(createBar_ACTV_barType.getText().toString().replace(' ','_'));
        ArrayList<eMusicType> emusicTypeList = new ArrayList<>();
        musicTypesChipsChecked.forEach(musicType -> emusicTypeList.add(eMusicType.valueOf(musicType.getText().toString().replace(' ','_'))));
        Bar bar = new Bar()
                .setBarType(ebarType)
                .setDescription(createBar_TIETL_description.getText().toString())
                .setName(createBar_TIETL_barName.getText().toString())
                .setMusicTypes(emusicTypeList)
                .setId(UUID.randomUUID().toString())
                .setOwner_id(DataManager.getDataManager().getBusinessAccount().getId());
        DataManager.getDataManager().addBusinessAccountBar(bar);
        Intent intent = new Intent(this, Activity_bar_account.class);
        startActivity(intent);
        finish();
    }

    private CompoundButton.OnCheckedChangeListener chip_clicked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b == true){
                musicTypesChipsChecked.add((Chip) compoundButton);
            }
            else{
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
}