package com.example.babyneeds.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.babyneeds.model.Item;
import com.example.babyneeds.model.User;

import java.util.List;

@Dao
public interface BabyNeedsDao {

    @Insert
    void insert(User user);

    @Insert
    void insert(Item item);

    @Update
    void update(User user);

    @Update
    void update(Item item);

    @Delete
    void delete(User user);

    @Delete
    void delete(Item item);


    @Query("SELECT * FROM " + User.TABLE_NAME + " WHERE email=:email")
    LiveData<User> getUser(String email);

    @Query("SELECT * FROM " + Item.TABLE_NAME + " WHERE id=:id")
    LiveData<User> getItem(int id);

    @Query("SELECT * FROM " + Item.TABLE_NAME + " WHERE userId=:userId and isPurchased=0")
    LiveData<List<Item>> getUserItems(int userId);

    @Query("SELECT COUNT(Name) FROM " + Item.TABLE_NAME + " WHERE userId=:userId")
    LiveData<Integer> countUserItems(int userId);

    @Query("SELECT COUNT(Name) FROM " + Item.TABLE_NAME + " WHERE userId=:userId and isPurchased=1")
    LiveData<Integer> countPurchasedItems(int userId);

    @Query("SELECT * FROM " + Item.TABLE_NAME + " WHERE userId=:userId and isPurchased=1")
    LiveData<List<Item>> getPurchasedItems(int userId);

    @Query("SELECT EXISTS(SELECT * FROM "+User.TABLE_NAME+" WHERE email=:email)")
    Boolean isUserExist(String email);

}
