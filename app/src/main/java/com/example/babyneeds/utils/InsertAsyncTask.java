package com.example.babyneeds.utils;

import android.os.AsyncTask;

import com.example.babyneeds.data.BabyNeedsDao;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.model.User;

public class InsertAsyncTask extends AsyncTask<Object, Void, Void> {

    private BabyNeedsDao dao;

    public InsertAsyncTask(BabyNeedsDao dao){
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        Object object = objects[0];

        if(object instanceof User){
            try{
                dao.insert((User) object);
            }catch(Exception e){
                return null;
            }
        }else if(object instanceof Item){
            dao.insert((Item) object);
        }
        return null;
    }
}
