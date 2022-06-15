package com.example.happyhour.callbacks;

import com.example.happyhour.objects.Account;

public interface Callback_account_creating extends Callback_DB{
    void account_created(Account account);
}
