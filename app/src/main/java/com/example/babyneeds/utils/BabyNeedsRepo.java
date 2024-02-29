package com.example.babyneeds.utils;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.babyneeds.data.BabyNeedsDao;
import com.example.babyneeds.data.BabyNeedsDb;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.model.User;

import java.util.List;

public class BabyNeedsRepo {

    private final BabyNeedsDao babyNeedsDao;

    public BabyNeedsRepo(Application application) {
        BabyNeedsDb db = BabyNeedsDb.getDatabase(application);

        babyNeedsDao = db.babyNeedsDao();
    }

    public void insertUser(User user) {
        new InsertAsyncTask(babyNeedsDao).execute(user);
    }

    public void insertItem(Item item) {
        new InsertAsyncTask(babyNeedsDao).execute(item);
    }

    public  void updateUser(User user){
        new UpdateAsyncTask(babyNeedsDao).execute(user);
    }

    public  void updateItem(Item item){
        new UpdateAsyncTask(babyNeedsDao).execute(item);
    }

    public void deleteUser(User user) {
        new DeleteAsyncTask(babyNeedsDao).execute(user);
    }

    public void deleteItem(Item item) {
        new DeleteAsyncTask(babyNeedsDao).execute(item);
    }

    public LiveData<User> getUser(String email){
        return babyNeedsDao.getUser(email);
    }

    public LiveData<User> getItem(int id){
        return babyNeedsDao.getItem(id);
    }

    public LiveData<List<Item>> getUserItems(int userId){
        return babyNeedsDao.getUserItems(userId);
    }

    public LiveData<List<Item>> getPurchasedItems(int userId){
        return babyNeedsDao.getPurchasedItems(userId);
    }

    public Boolean isUserExist(String email){
        return babyNeedsDao.isUserExist(email);
    }

    public LiveData<Integer> countUserItems(int userId){
        return babyNeedsDao.countUserItems(userId);
    }

    public LiveData<Integer> countPurchasedItems(int userId){
        return babyNeedsDao.countPurchasedItems(userId);
    }


}
