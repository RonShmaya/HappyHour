package com.example.happyhour.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.happyhour.R;
import com.example.happyhour.tools.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;


public class DialogChangeBarDetails {
    public interface Callback_change_bar {
        void change_text(String newTxt , int objResId);
    }
    private TextInputLayout dialogChange_TIL;
    private TextInputEditText dialogChange_TIETL;

    private TextInputLayout dialogChangeBar_TIL_changeBarType;
    private AutoCompleteTextView dialogChangeBar_ACTV_barTypeChange;

    private MaterialButton dialogAddBar_BTN_change;
    private Callback_change_bar callback_change_bar;


    public DialogChangeBarDetails setCallback_change_bar(Callback_change_bar callback_change_bar) {
        this.callback_change_bar = callback_change_bar;
        return this;
    }

    public void addEditText_show(int resID_TIL, int resID_TIETL , int maxWords, String hintText , String lastText, Context context , int resID_return) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_change_bar_details);
        dialogAddBar_BTN_change = dialog.findViewById(R.id.dialogAddBar_BTN_change);

        dialogChange_TIL = dialog.findViewById(resID_TIL);
        dialogChange_TIL.setVisibility(View.VISIBLE);
        dialogChange_TIL.setCounterMaxLength(maxWords);
        dialogChange_TIL.setHint(hintText);

        dialogChange_TIETL = dialog.findViewById(resID_TIETL);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(maxWords);
        dialogChange_TIETL.setVisibility(View.VISIBLE);
        dialogChange_TIETL.setFilters(filterArray);
        dialogChange_TIETL.setText(lastText);

        dialogAddBar_BTN_change.setOnClickListener(view -> {
            verify(dialogChange_TIETL , dialogChange_TIL , dialog, resID_return);
           });
        dialog.show();
    }
    public void AutoCompleteTextView_show(  Context context,int resID_return) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_change_bar_details);
        dialogAddBar_BTN_change = dialog.findViewById(R.id.dialogAddBar_BTN_change);

        dialogChangeBar_TIL_changeBarType = dialog.findViewById(R.id.dialogChangeBar_TIL_changeBarType);
        dialogChangeBar_ACTV_barTypeChange = dialog.findViewById(R.id.dialogChangeBar_ACTV_barTypeChange);
        dialogChangeBar_TIL_changeBarType.setVisibility(View.VISIBLE);
        dialogChangeBar_ACTV_barTypeChange.setVisibility(View.VISIBLE);

        ArrayList<String> barTypes;
        ArrayAdapter<String> adapter;
        barTypes = DataManager.getDataManager().getBarTypesNames();
        Collections.sort(barTypes);
        adapter = new ArrayAdapter(context, R.layout.list_item, barTypes);
        dialogChangeBar_ACTV_barTypeChange.setAdapter(adapter);

        dialogAddBar_BTN_change.setOnClickListener(view -> {
            verify(dialogChangeBar_ACTV_barTypeChange , dialogChangeBar_TIL_changeBarType , dialog,resID_return);
        });
        dialog.show();
    }

    public void verify(EditText input , TextInputLayout inputError , Dialog dialog, int resID_return){
        inputError.setError("");
        if(input.getText().toString().isEmpty()) {
            inputError.setError("cannot be empty");
        }
        else if(callback_change_bar != null){
            callback_change_bar.change_text(input.getText().toString() , resID_return);
            dialog.dismiss();
        }
    }
}
