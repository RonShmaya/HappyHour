package com.example.happyhour.tools;

import com.example.happyhour.objects.Account;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.BusinessAccount;
import com.example.happyhour.objects.PrivateAccount;
import com.example.happyhour.objects.eBarType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

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
    public ArrayList<String> getBarTypesNames() {
        ArrayList<String> barsType_name = new ArrayList<>();

        for (eBarType barType: eBarType.values()) {
            barsType_name.add(barType.toString().replace('_',' '));
        }
        return barsType_name;
    }


    public void addBusinessAccountBar(Bar bar) {
        businessAccount.addBar(bar , bar.getId());
        MyDB.getInstance().add_business_account_bar(businessAccount, bar.getId() ,bar);
        MyDB.getInstance().add_bar(bar.getId() ,bar);
    }
    public void logout() {
         privateAccount = null;
         businessAccount = null;
        FirebaseAuth.getInstance().signOut();
        MyDB.getInstance().logout();
    }
    public void delete_bar(Bar bar, int position) {
        this.businessAccount.getMyBars().remove(bar.getId(),bar);
        MyDB.getInstance().delete_bar(businessAccount , bar);
    }



}