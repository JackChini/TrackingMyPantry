package com.example.trackingmypantrygiacomochinilam;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class ItemRoomDatabase extends RoomDatabase {

    public abstract ItemDao itemDao();

    private static volatile ItemRoomDatabase INSTANCE;
    private static final int nTHREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(nTHREADS);
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ItemDao dao = INSTANCE.itemDao();

            });
        }
    };

    static ItemRoomDatabase getDatabase(final Context context, String name) {
        if (INSTANCE == null) {
            synchronized (ItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ItemRoomDatabase.class, name+"item_database")
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

}
