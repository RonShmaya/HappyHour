package com.example.happyhour.tools;

import com.example.happyhour.objects.Account;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.BusinessAccount;
import com.example.happyhour.objects.MyTime;
import com.example.happyhour.objects.PrivateAccount;
import com.example.happyhour.objects.Table;
import com.example.happyhour.objects.eBarType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DataManager {
    private static DataManager _instance = new DataManager();
    public static String EXTRA_BAR = "EXTRA_BAR";
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
    public void delete_table(Bar bar, Table table) {
        this.businessAccount.getMyBars().get(bar.getId()).removeTable(table.getId());
        MyDB.getInstance().delete_table(businessAccount , bar, table);
    }
    public void add_table(Bar bar, Table table) {
        this.businessAccount.getMyBars().get(bar.getId()).addTable(table.getId(), table);
        MyDB.getInstance().add_table(businessAccount , bar, table);
    }
    public Bar getBar(String barId) {
        return  this.businessAccount.getMyBars().get(barId);
    }

    public void change_bar_description(String barId, String description) {
       Bar bar =  this.businessAccount.getMyBars().get(barId);
        bar.setDescription(description);
        MyDB.getInstance().change_bar_description(this.businessAccount , bar , description);
    }
    public void change_bar_name(String barId, String name) {
        Bar bar =  this.businessAccount.getMyBars().get(barId);
        bar.setName(name);
        MyDB.getInstance().change_bar_name(this.businessAccount , bar , name);
    }
    public void change_bar_happy_hour(String barId, String happy_hour) {
        Bar bar =  this.businessAccount.getMyBars().get(barId);
        bar.setHappy_hour(happy_hour);
        MyDB.getInstance().change_bar_happyHour(this.businessAccount , bar , happy_hour);
    }
    public void change_bar_type(String barId, eBarType barType) {
        Bar bar =  this.businessAccount.getMyBars().get(barId);
        bar.setBarType(barType);
        MyDB.getInstance().change_bar_type(this.businessAccount , bar , barType);
    }
    public void change_bar_time_open(String barId, MyTime time) {
        Bar bar =  this.businessAccount.getMyBars().get(barId);
        bar.setOpenTime(time);
        MyDB.getInstance().change_bar_open_time(this.businessAccount , bar , time);
    }
    public void change_bar_time_close(String barId, MyTime time) {
        Bar bar =  this.businessAccount.getMyBars().get(barId);
        bar.setClosingTime(time);
        MyDB.getInstance().change_bar_close_time(this.businessAccount , bar , time);
    }

}