package com.example.babyneeds.utils;

import android.os.AsyncTask;

import com.example.babyneeds.data.BabyNeedsDao;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.model.User;

public class DeleteAsyncTask extends AsyncTask<Object, Void, Void> {

    private BabyNeedsDao dao;

    public DeleteAsyncTask(BabyNeedsDao dao){
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        Object object = objects[0];

        if(object instanceof User){
            dao.delete((User) object);
        }else if(object instanceof Item){
            dao.delete((Item) object);
        }
        return null;
    }
}
