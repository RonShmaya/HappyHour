package com.example.happyhour.objects;

import java.util.HashMap;

public class BusinessAccounts {
    private HashMap<String,BusinessAccount> accounts = new HashMap<>();

    public BusinessAccounts() {
    }

    public HashMap<String, BusinessAccount> getAccounts() {
        return accounts;
    }

    public BusinessAccounts setAccounts(HashMap<String, BusinessAccount> accounts) {
        this.accounts = accounts;
        return this;
    }

    public void addUser(String id, BusinessAccount account){
        accounts.put(id , account);
    }
}
