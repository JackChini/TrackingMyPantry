package com.example.trackingmypantrygiacomochinilam;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductRoomDatabase extends RoomDatabase{

    public abstract ProductDao productDao();

    private static volatile ProductRoomDatabase INSTANCE;
    private static final int nTHREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(nTHREADS);
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ProductDao dao = INSTANCE.productDao();
                dao.deleteAll();
            });
        }
    };

    static ProductRoomDatabase getProductDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProductRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProductRoomDatabase.class, "product_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }
}
