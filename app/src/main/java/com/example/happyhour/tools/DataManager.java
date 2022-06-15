package com.example.happyhour.tools;

import com.example.happyhour.objects.Account;
import com.example.happyhour.objects.BusinessAccount;
import com.example.happyhour.objects.PrivateAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DataManager {
    private static DataManager _instance = new DataManager();

    public enum eUserTypes {Private, Business}
    private PrivateAccount privateAccount;
    private BusinessAccount businessAccount;

    private DataManager() {
    }


    public static DataManager getDataManager() {
        return _instance;
    }

    public void set_business_account(BusinessAccount account) {
        this.businessAccount = account;
    }

    public void set_private_account(PrivateAccount account) {
        this.privateAccount = account;
    }

    public PrivateAccount getPrivateAccount() {
        return privateAccount;
    }
    public BusinessAccount getBusinessAccount() {
        return businessAccount;
    }

    public boolean isUserConnected(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public eUserTypes getUserType(){
        if(isUserConnected()){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user.getEmail() != null && !user.getEmail().isEmpty()){
                MyServices.getInstance().toLog(user.getEmail().toString());
                return eUserTypes.Private;

            }
            else if(user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()){
                return eUserTypes.Business;
            }
        }
        return null;
    }

    public void set_account(Account account) {
        if(account instanceof BusinessAccount){
            set_business_account((BusinessAccount) account);
        }
        else if(account instanceof PrivateAccount){
            set_private_account((PrivateAccount)account);
        }
    }

}