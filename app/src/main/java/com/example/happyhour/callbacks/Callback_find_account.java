package com.example.happyhour.callbacks;

import com.example.happyhour.objects.Account;

public interface Callback_find_account extends Callback_DB {
   void account_found(Account account);
   void account_not_found();
}
