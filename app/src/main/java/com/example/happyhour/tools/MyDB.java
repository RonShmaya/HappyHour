package com.example.happyhour.tools;

import androidx.annotation.NonNull;

import com.example.happyhour.callbacks.Callback_account_creating;
import com.example.happyhour.callbacks.Callback_find_account;
import com.example.happyhour.callbacks.Callback_get_bars;
import com.example.happyhour.objects.Account;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.BusinessAccount;
import com.example.happyhour.objects.Follower;
import com.example.happyhour.objects.MyTime;
import com.example.happyhour.objects.Order;
import com.example.happyhour.objects.Post;
import com.example.happyhour.objects.PrivateAccount;
import com.example.happyhour.objects.Review;
import com.example.happyhour.objects.Table;
import com.example.happyhour.objects.eBarType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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
    private Callback_get_bars callback_get_bars;
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
    public void setCallback_get_bars(Callback_get_bars callback_get_bars) {
        this.callback_get_bars = callback_get_bars;
    }

    public void isAccountExists(DataManager.eUserTypes userType, String accountID) {
        if (this.callback_find_user != null) {
            DatabaseReference ref = userType == DataManager.eUserTypes.Business ? refBusinessAccounts : refPrivateAccounts;
            ref.child(accountID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Account account = null;
                    if (userType == DataManager.eUserTypes.Business) {
                        account = dataSnapshot.getValue(BusinessAccount.class);
                    } else if (userType == DataManager.eUserTypes.Private) {
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


    public void create_account(Account account, DataManager.eUserTypes userType) {
        if (this.callback_account_creating != null) {
            DatabaseReference ref = userType == DataManager.eUserTypes.Business ? refBusinessAccounts : refPrivateAccounts;
            ref.child(account.getId()).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        callback_account_creating.account_created(account);
                        return;
                    }
                    callback_account_creating.failed();
                }
            });
        }
    }
    public void get_bars() {
        if (this.callback_get_bars != null) {
            refBars.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<HashMap<String,Bar>> dataType = new GenericTypeIndicator<HashMap<String, Bar>>() {};
                    HashMap<String, Bar> bars = dataSnapshot.getValue(dataType);
                    callback_get_bars.get_bars(bars);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback_get_bars.failed();
                }
            });
        }
    }


    public void add_business_account_bar(BusinessAccount businessAccount, String barID, Bar bar) {
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(barID).setValue(bar);
    }

    public void logout() {
        callback_find_user = null;
        callback_account_creating = null;
        callback_get_bars = null;

    }

    public void add_bar(String id, Bar bar) {
        refBars.child(id).setValue(bar);
    }

    public void delete_bar(BusinessAccount businessAccount, Bar bar) {
        refBars.child(bar.getId()).removeValue();
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).removeValue();
    }

    public void delete_table(BusinessAccount businessAccount, Bar bar, Table table) {
        refBars.child(bar.getId()).child("tables").child(table.getId()).removeValue();
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).child("tables").child(table.getId()).removeValue();

        table.getOrders().forEach( (order_id , order) -> {
            refPrivateAccounts.child(order.getUser_id()).child("orders").child(order.getOrder_id()).removeValue();
        });

    }

    public void add_table(BusinessAccount businessAccount, Bar bar, Table table) {
        refBars.child(bar.getId()).child("tables").child(table.getId()).setValue(table);
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).child("tables").child(table.getId()).setValue(table);
    }

    public void change_bar_description(BusinessAccount businessAccount, Bar bar, String description) {
        refBars.child(bar.getId()).child("description").setValue(description);
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).child("description").setValue(description);
    }

    public void change_bar_name(BusinessAccount businessAccount, Bar bar, String name) {
        refBars.child(bar.getId()).child("name").setValue(name);
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).child("name").setValue(name);
    }

    public void change_bar_happyHour(BusinessAccount businessAccount, Bar bar, String happyHour) {
        refBars.child(bar.getId()).child("happy_hour").setValue(happyHour);
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).child("happy_hour").setValue(happyHour);
    }

    public void change_bar_type(BusinessAccount businessAccount, Bar bar, eBarType barType) {
        refBars.child(bar.getId()).child("barType").setValue(barType);
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).child("barType").setValue(barType);
    }

    public void change_bar_open_time(BusinessAccount businessAccount, Bar bar, MyTime time) {
        refBars.child(bar.getId()).child("openTime").setValue(time);
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).child("openTime").setValue(time);
    }

    public void change_bar_close_time(BusinessAccount businessAccount, Bar bar, MyTime time) {
        refBars.child(bar.getId()).child("closingTime").setValue(time);
        refBusinessAccounts.child(businessAccount.getId()).child("myBars").child(bar.getId()).child("closingTime").setValue(time);
    }

    public void remove_follower_private(Bar bar, String userId) {
        refPrivateAccounts.child(userId).child("follow_bars").child(bar.getId()).removeValue();
    }
    public void remove_follower_business(Bar bar, String userId) {
        refBars.child(bar.getId()).child("followers").child(userId).removeValue();
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("followers").child(userId).removeValue();
    }

    public void add_follower(Bar bar, String userId, Follower follower) {
        refBars.child(bar.getId()).child("followers").child(userId).setValue(follower);
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("followers").child(userId).setValue(follower);
        refPrivateAccounts.child(userId).child("follow_bars").child(bar.getId()).setValue(bar.getName());
    }

    public void get_bar(String barId) {
        if (this.callback_get_bars != null) {
            refBars.child(barId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Bar bar = dataSnapshot.getValue(Bar.class);
                    callback_get_bars.get_bar(bar);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback_find_user.failed();
                }
            });
        }
    }
    public void add_review(Bar bar, Review review) {
        refBars.child(bar.getId()).child("reviews").child(review.getId()).setValue(review);
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("reviews").child(review.getId()).setValue(review);
    }

    public void add_reservation(Bar bar, String tableId, Order order) {
        refBars.child(bar.getId()).child("tables").child(tableId).child("orders").child(order.getOrder_id()).setValue(order);

        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId())
                .child("tables").child(tableId).child("orders")
                .child(order.getOrder_id()).setValue(order);

        refPrivateAccounts.child(order.getUser_id()).child("orders").child(order.getOrder_id()).setValue(order);
    }

    public void remove_order(Bar bar, String tableId, Order order) {
        refBars.child(bar.getId()).child("tables").child(tableId).child("orders").child(order.getOrder_id()).removeValue();

        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId())
                .child("tables").child(tableId).child("orders")
                .child(order.getOrder_id()).removeValue();

        refPrivateAccounts.child(order.getUser_id()).child("orders").child(order.getOrder_id()).removeValue();
    }

    public void add_private_account_details(String userId , eBarType fav_1, eBarType fav_2, String uri) {
        refPrivateAccounts.child(userId).child("favorite_1").setValue(fav_1);
        refPrivateAccounts.child(userId).child("favorite_2").setValue(fav_2);
        refPrivateAccounts.child(userId).child("imgUri").setValue(uri);
    }

    public void update_bar_photo(Bar bar, String url) {
        refBars.child(bar.getId()).child("bar_photo").setValue(url);
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("bar_photo").setValue(url);
    }
    public void update_bar_menu_photo(Bar bar, String url) {
        refBars.child(bar.getId()).child("menu_photo").setValue(url);
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("menu_photo").setValue(url);
    }
    public void remove_bar_post(Bar bar, Post post) {
        refBars.child(bar.getId()).child("posts").child(post.getPost_id()).removeValue();
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("posts").child(post.getPost_id()).removeValue();
    }
    public void add_bar_post(Bar bar, Post post) {
        refBars.child(bar.getId()).child("posts").child(post.getPost_id()).setValue(post);
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("posts").child(post.getPost_id()).setValue(post);
    }

    public void unLike(Bar bar, Post post, String userId) {
        refBars.child(bar.getId()).child("posts").child(post.getPost_id()).child("likes").child(userId).removeValue();
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("posts").child(post.getPost_id()).child("likes").child(userId).removeValue();
    }
    public void like(Bar bar, Post post, String userId,String userName) {
        refBars.child(bar.getId()).child("posts").child(post.getPost_id()).child("likes").child(userId).setValue(userName);
        refBusinessAccounts.child(bar.getOwner_id()).child("myBars").child(bar.getId()).child("posts").child(post.getPost_id()).child("likes").child(userId).setValue(userName);
    }
}

