package com.example.happyhour.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happyhour.R;
import com.example.happyhour.activities.business_account.Activity_bar_account;
import com.example.happyhour.activities.private_account.Activity_customer_main_page;
import com.example.happyhour.activities.private_account.Activity_private_account_profile;
import com.example.happyhour.callbacks.Callback_account_creating;
import com.example.happyhour.callbacks.Callback_find_account;
import com.example.happyhour.objects.Account;
import com.example.happyhour.objects.BusinessAccount;
import com.example.happyhour.objects.PrivateAccount;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

// TODO: 23/06/2022 new private to another activity
public class Activity_user_connect extends AppCompatActivity {
    public enum eUserPick {CreatePrivateAccount, CreateBusinessAccount, Login }
    private DataManager.eUserTypes userType;
    private eUserPick userPick;
    private MaterialButton connect_BTN_login;
    private MaterialButton connect_BTN_privateAccount;
    private MaterialButton connect_BTN_businessAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_connect);
        findViews();

        MyDB.getInstance().setCallback_find_user(callback_find_account);
        MyDB.getInstance().setCallback_account_creating(callback_account_creating);

        connect_BTN_login.setOnClickListener(view -> login());
        connect_BTN_privateAccount.setOnClickListener(view -> privateAccount());
        connect_BTN_businessAccount.setOnClickListener(view -> businessAccount());

    }

    private void businessAccount() {
        userPick = eUserPick.CreateBusinessAccount;
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );
        signIn(providers);
    }

    private void privateAccount() {
        userPick = eUserPick.CreatePrivateAccount;
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        signIn(providers);
    }

    private void login() {
        userPick = eUserPick.Login;
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        signIn(providers);
    }
    private void signIn(List<AuthUI.IdpConfig> providers) {
        Intent signInIntent =
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_logo_small)
                        .setTheme(R.style.Theme_HappyHour) //Theme_HappyHour
                        .setTosAndPrivacyPolicyUrls("https://firebase.google.com/docs/auth/android/firebaseui?hl=en&authuser=0", "https://firebase.google.com/docs/auth/android/firebaseui?hl=en&authuser=0")
                        .build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                   connect_to_user();
                }
            }
    );

    private void connect_to_user() {
        userType = DataManager.getDataManager().getUserType();

        if(userType == null) {
            MyServices.getInstance().makeToast("There was a problem in enter details, please try again");
            return;
        }
        MyDB.getInstance().isAccountExists(userType , FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void business_account_next_page(boolean isUserFound , BusinessAccount account) {
        if(isUserFound) {
            MyServices.getInstance().makeToast("the business account already exists, making login");
            DataManager.getDataManager().set_business_account(account);
            go_next(Activity_bar_account.class);
        }
        else {
            account = new BusinessAccount();
            account.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            MyDB.getInstance().create_account(account , userType);
        }

    }

    private void private_account_next_page(boolean isUserFound, PrivateAccount account) {
        if(isUserFound){
            MyServices.getInstance().makeToast("the private account already exists, making login");
            DataManager.getDataManager().set_private_account(account);
            if(account.getFavorite_1() == null)
                go_next(Activity_private_account_profile.class);
            else
                go_next(Activity_customer_main_page.class);
        }
        else{
            account = new PrivateAccount();
            account.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            account.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            MyDB.getInstance().create_account(account , userType);
        }
    }

    private void login_next_page(boolean isUserFound, Account account) {
        if(account instanceof PrivateAccount){
            if(((PrivateAccount)account).getFavorite_1() == null)
                go_next(Activity_private_account_profile.class);
            else
                go_next(Activity_customer_main_page.class);
            DataManager.getDataManager().set_account(account);
        }
        else if(account instanceof BusinessAccount){
            go_next(Activity_bar_account.class);
            DataManager.getDataManager().set_account(account);
        }
        else{
            MyServices.getInstance().makeToast("the account not exists\nplease create account");
        }
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }

    private Callback_find_account callback_find_account = new Callback_find_account() {
        @Override
        public void account_found(Account account) {
            if(userPick == eUserPick.Login){
                login_next_page(true , account);
            }
            else if(userPick == eUserPick.CreatePrivateAccount){
                private_account_next_page(true , ((PrivateAccount)account));
            }
            else if(userPick == eUserPick.CreateBusinessAccount){
                business_account_next_page(true , ((BusinessAccount)account));
            }
        }

        @Override
        public void account_not_found() {
            if(userPick == eUserPick.Login){
                login_next_page(false, null);
            }
            else if(userPick == eUserPick.CreatePrivateAccount){
                private_account_next_page(false, null);
            }
            else if(userPick == eUserPick.CreateBusinessAccount){
                business_account_next_page(false, null);
            }
        }

        @Override
        public void failed() {
            MyServices.getInstance().makeToast("something went wrong please try again...");
        }
    };

    private Callback_account_creating callback_account_creating = new Callback_account_creating() {
        @Override
        public void account_created(Account account) {
            DataManager.getDataManager().set_account(account);

            if(userType == DataManager.eUserTypes.Business)
                go_next(Activity_bar_account.class);
            else if(userType == DataManager.eUserTypes.Private)
                if(((PrivateAccount)account).getFavorite_1() == null)
                    go_next(Activity_private_account_profile.class);
                else
                    go_next(Activity_customer_main_page.class);
        }

        @Override
        public void failed() {
            MyServices.getInstance().makeToast("something went wrong please try again...");
        }
    };

    private void findViews() {
        connect_BTN_login = findViewById(R.id.connect_BTN_login);
        connect_BTN_privateAccount = findViewById(R.id.connect_BTN_privateAccount);
        connect_BTN_businessAccount = findViewById(R.id.connect_BTN_businessAccount);

    }
}