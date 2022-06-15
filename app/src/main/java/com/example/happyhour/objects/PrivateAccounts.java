package com.example.happyhour.objects;

import java.util.HashMap;

public class PrivateAccounts {
    private HashMap<String,PrivateAccount> accounts = new HashMap<>();

    public PrivateAccounts() {
    }

    public HashMap<String, PrivateAccount> getAccounts() {
        return accounts;
    }

    public PrivateAccounts setAccounts(HashMap<String, PrivateAccount> accounts) {
        this.accounts = accounts;
        return this;
    }
    public void addUser(String id, PrivateAccount account){
        accounts.put(id,account);
    }
}
