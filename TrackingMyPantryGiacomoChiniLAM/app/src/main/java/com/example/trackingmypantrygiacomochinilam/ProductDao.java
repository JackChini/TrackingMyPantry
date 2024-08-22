package com.example.trackingmypantrygiacomochinilam;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product prod);

    @Query("DELETE FROM products")
    void deleteAll();

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getListOfProducts();
}
