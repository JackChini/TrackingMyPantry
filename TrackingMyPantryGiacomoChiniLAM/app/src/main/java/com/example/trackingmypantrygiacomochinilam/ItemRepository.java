package com.example.trackingmypantrygiacomochinilam;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

import androidx.sqlite.db.SimpleSQLiteQuery;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> listLiveDataItems;

    public ItemRepository(Application app, String name) {
        ItemRoomDatabase dbItem = ItemRoomDatabase.getDatabase(app, name);

        itemDao = dbItem.itemDao();
        listLiveDataItems = itemDao.getListOfItems();
    }

    public LiveData<List<Item>> getListOfItems() { return listLiveDataItems; }

    public void insertItem(Item item){
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> {
            itemDao.insert(item);
        });
    }

    public void addQuantity(String id){
        itemDao.addQuantity(id, Constants.DEFAULT_ADD_OR_REMOVE_QUANTITY);
    }
    public void removeQuantity(String id){
        itemDao.removeQuantity(id, Constants.DEFAULT_ADD_OR_REMOVE_QUANTITY);
    }
    public void deleteItem(String id){
        itemDao.deleteItem(id);
    }

    public void deleteAll(){
        itemDao.deleteAll();
    }

    public LiveData<List<Item>> getFilteredQuery(String queryString, List<Object> arg) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryString, arg.toArray());
        return itemDao.getFilteredSortedList(query);
    }

    public List<Item> getExpireList(String expireDate) {
        return itemDao.getExpireDateList(expireDate);
    }

    public List<Item> getEmptyItemsList(){
        return itemDao.getEmptyItems();
    }

    public boolean existId(String id){
        if(itemDao.getItemById(id) != null){
            return true;
        }
        return false;
    }

    public void updateExpireDate(String id, String newExpire){
        itemDao.changeExpireDate(id, newExpire);
    }

}
