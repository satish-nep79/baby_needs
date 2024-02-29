package com.example.babyneeds.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.babyneeds.utils.BabyNeedsRepo;

import java.util.List;

public class BabyNeedsViewModel extends AndroidViewModel {

    private BabyNeedsRepo babyNeedsRepo;

    public BabyNeedsViewModel(@NonNull Application application) {
        super(application);
        babyNeedsRepo = new BabyNeedsRepo(application);
    }

    public void insert(User user) {
        babyNeedsRepo.insertUser(user);
    }

    public void insert(Item item) {
        babyNeedsRepo.insertItem(item);
    }

    public  void update(User user){
        babyNeedsRepo.updateUser(user);
    }

    public void update(Item item){
        babyNeedsRepo.updateItem(item);
    }

    public void delete(User user) {
        babyNeedsRepo.deleteUser(user);
    }

    public void delete(Item item) {
        babyNeedsRepo.deleteItem(item);
    }

    public LiveData<User> getUser(String email) {
        return babyNeedsRepo.getUser(email);
    }

    public LiveData<User> getItem(int id){
        return babyNeedsRepo.getItem(id);
    }

    public  LiveData<List<Item>> getUserItems(int usrId){
        return babyNeedsRepo.getUserItems(usrId);
    }

    public  LiveData<List<Item>> getPurchasedItems(int usrId){
        return babyNeedsRepo.getPurchasedItems(usrId);
    }

    public LiveData<Integer> countUserItems(int userId){
        return babyNeedsRepo.countUserItems(userId);
    }

    public LiveData<Integer> countPurchasedItems(int userId){
        return babyNeedsRepo.countPurchasedItems(userId);
    }

    public Boolean isUserExist(String email){
        return babyNeedsRepo.isUserExist(email);
    }

}
