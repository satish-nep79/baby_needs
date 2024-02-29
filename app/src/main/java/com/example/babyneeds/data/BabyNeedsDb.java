package com.example.babyneeds.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.babyneeds.model.Item;
import com.example.babyneeds.model.User;

@Database(entities = {
        User.class,
        Item.class
},
        version = 1
)
public abstract class BabyNeedsDb extends RoomDatabase {

    public static final String DATABASE_NAME = "Baby_Needs_DB";
    public static volatile BabyNeedsDb INSTANCE;

    public abstract BabyNeedsDao babyNeedsDao();

    //Singleton for roomDatabase
    public static BabyNeedsDb getDatabase(final Context context) {
        if(INSTANCE == null){
            synchronized (BabyNeedsDb.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            BabyNeedsDb.class,
                            DATABASE_NAME
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

}
