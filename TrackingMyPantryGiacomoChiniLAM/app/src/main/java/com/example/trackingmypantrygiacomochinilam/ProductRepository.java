package com.example.trackingmypantrygiacomochinilam;

import android.app.Application;
import java.util.List;

import androidx.lifecycle.LiveData;

public class ProductRepository {

    private ProductDao prodDao;
    private LiveData<List<Product>> listLivedataProducts;

    public ProductRepository(Application app) {
        ProductRoomDatabase dbProd = ProductRoomDatabase.getProductDatabase(app);

        prodDao = dbProd.productDao();
        listLivedataProducts = prodDao.getListOfProducts();
    }

    public LiveData<List<Product>> getListOfProducts() {
        return listLivedataProducts;
    }

    public void insertProduct(Product prod){
        ProductRoomDatabase.databaseWriteExecutor.execute(() -> {
            prodDao.insert(prod);
        });
    }

    public void removeAllProducts(){
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> {
            prodDao.deleteAll();
        });
    }
}
