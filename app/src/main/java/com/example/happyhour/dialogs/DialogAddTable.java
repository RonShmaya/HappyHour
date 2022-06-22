package com.example.happyhour.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.example.happyhour.R;
import com.example.happyhour.objects.Table;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.UUID;


public class DialogAddTable{
    public interface Callback_add_table {
        void table_to_add(Table table);
    }
    private TextInputLayout dialogAddBar_TIL_tableName;
    private TextInputEditText dialogAddBar_TIETL_tableName;
    private TextInputLayout dialogAddBar_TIL_description;
    private TextInputEditText dialogAddBar_TIETL_description;
    private TextInputLayout dialogAddBar_TIL_places;
    private TextInputEditText dialogAddBar_TIETL_places;
    private MaterialButton dialogAddBar_BTN_create;
    private Callback_add_table callback_add_table;

    public void show(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_table);
        findViews(dialog);
        dialog.show();
    }

    public DialogAddTable setCallback_add_table(Callback_add_table callback_add_table) {
        this.callback_add_table = callback_add_table;
        return this;
    }

    private void findViews(Dialog dialog) {
        dialogAddBar_TIL_tableName = dialog.findViewById(R.id.dialogAddBar_TIL_tableName);
        dialogAddBar_TIETL_tableName = dialog.findViewById(R.id.dialogAddBar_TIETL_tableName);
        dialogAddBar_TIL_description = dialog.findViewById(R.id.dialogAddBar_TIL_description);
        dialogAddBar_TIETL_description = dialog.findViewById(R.id.dialogAddBar_TIETL_description);
        dialogAddBar_TIL_places = dialog.findViewById(R.id.dialogAddBar_TIL_places);
        dialogAddBar_TIETL_places = dialog.findViewById(R.id.dialogAddBar_TIETL_places);
        dialogAddBar_BTN_create = dialog.findViewById(R.id.dialogAddBar_BTN_create);

        dialogAddBar_BTN_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInputOK = true;
                isInputOK = verifyInput(dialogAddBar_TIETL_tableName, dialogAddBar_TIL_tableName) && isInputOK;
                isInputOK = verifyInput(dialogAddBar_TIETL_description, dialogAddBar_TIL_description) && isInputOK;
                isInputOK = verifyInput(dialogAddBar_TIETL_places, dialogAddBar_TIL_places) && isInputOK;

                if (isInputOK && callback_add_table != null) {
                    Table table = new Table()
                            .setId(UUID.randomUUID().toString())
                            .setName(dialogAddBar_TIETL_tableName.getText().toString())
                            .setDescription(dialogAddBar_TIETL_description.getText().toString())
                            .setNumOfPlaces(Integer.parseInt(dialogAddBar_TIETL_places.getText().toString()));
                    callback_add_table.table_to_add(table);
                    dialog.dismiss();
                }

            }
        });
    }



    private boolean verifyInput(TextInputEditText input, TextInputLayout inputError) {
        inputError.setError("");
        if (input.getText().toString().isEmpty()) {
            inputError.setError("cannot be empty");
            return false;
        }
        return true;
    }
}
