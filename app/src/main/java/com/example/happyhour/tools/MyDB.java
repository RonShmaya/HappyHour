package com.example.happyhour.tools;

import androidx.annotation.NonNull;

import com.example.happyhour.callbacks.Callback_account_creating;
import com.example.happyhour.callbacks.Callback_find_account;
import com.example.happyhour.objects.Account;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.BusinessAccount;
import com.example.happyhour.objects.BusinessAccounts;
import com.example.happyhour.objects.PrivateAccount;
import com.example.happyhour.objects.PrivateAccounts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyDB {
    public static final String BUSINESS_ACCOUNTS = "BUSINESS_ACCOUNTS";
    public static final String PRIVATE_ACCOUNTS = "PRIVATE_ACCOUNTS";
    public static final String BARS = "BARS";

    private static MyDB _instance = new MyDB();
    private FirebaseDatabase database;
    private DatabaseReference refBusinessAccounts;
    private DatabaseReference refPrivateAccounts;
    private DatabaseReference refBars;
    private Callback_find_account callback_find_user;
    private Callback_account_creating callback_account_creating;

    private MyDB() {
        database = FirebaseDatabase.getInstance();
        refBusinessAccounts = database.getReference(BUSINESS_ACCOUNTS);
        refPrivateAccounts = database.getReference(PRIVATE_ACCOUNTS);
        refBars = database.getReference(BARS);
    }

    public static MyDB getInstance() {
        return _instance;
    }

    public void setCallback_find_user(Callback_find_account callback_find_user) {
        this.callback_find_user = callback_find_user;
    }
    public void setCallback_account_creating(Callback_account_creating callback_account_creating) {
        this.callback_account_creating = callback_account_creating;
    }

    public void isAccountExists(DataManager.eUserTypes userType, String accountID) {
        if(this.callback_find_user != null) {
            DatabaseReference ref = userType == DataManager.eUserTypes.Business ? refBusinessAccounts : refPrivateAccounts;
            ref.child(accountID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Account account = null;
                    if(userType == DataManager.eUserTypes.Business){
                         account = dataSnapshot.getValue(BusinessAccount.class);
                    }
                    else if(userType == DataManager.eUserTypes.Private){
                         account = dataSnapshot.getValue(PrivateAccount.class);
                    }
                    if (account == null) {
                        callback_find_user.account_not_found();
                        return;
                    }
                    callback_find_user.account_found(account);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback_find_user.failed();
                }
            });
        }
    }


    public void create_account(Account account , DataManager.eUserTypes userType) {
        if (this.callback_account_creating != null) {
            DatabaseReference ref = userType == DataManager.eUserTypes.Business ? refBusinessAccounts : refPrivateAccounts;
            ref.child(account.getId()).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        callback_account_creating.account_created(account);
                        return;
                    }
                    callback_account_creating.failed();
                }
            });
        }
    }

    public void update_business_account(BusinessAccount businessAccount) {
        refBusinessAccounts.child(businessAccount.getId()).setValue(businessAccount);
    }
    public void add_business_account_bar(BusinessAccount businessAccount, String barID , Bar bar) {
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(barID).setValue(bar);
    }

    public void logout() {
         callback_find_user = null;
         callback_account_creating = null;
    }

    public void add_bar(String id, Bar bar) {
        refBars.child(id).setValue(bar);
    }

    public void delete_bar(BusinessAccount businessAccount, Bar bar) {
        refBars.child(bar.getId()).removeValue();
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).removeValue();
    }
}
