package com.example.trackingmypantrygiacomochinilam;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Query("DELETE FROM items")
    void deleteAll();

    @Query("SELECT * FROM items")
    LiveData<List<Item>> getListOfItems();

    //controlla se l'id passato come parametro esiste già
    @Query("SELECT * FROM items WHERE id = :searchId")
    Item getItemById(String searchId);

    //aggiunge quantità all'item con id passato in input
    @Query("UPDATE items SET quantity = quantity + :value WHERE id = :id")
    void addQuantity(String id, int value);

    //rimuovere quantità all'item con id passato in input
    @Query("UPDATE items SET quantity = quantity - :value WHERE id = :id")
    void removeQuantity(String id, int value);

    //elimina un item dal db
    @Query("DELETE FROM items WHERE id = :id")
    void deleteItem(String id);

    //filtraggio per tipo e numero di elementi nel campo quantità
    @RawQuery(observedEntities = Item.class)
    LiveData<List<Item>> getFilteredSortedList(SupportSQLiteQuery query);

    //filtraggio per prodotti scaduti con quantità almeno maggiore di 0
    @Query("SELECT * FROM items WHERE expire == :search AND quantity > 0")
    List<Item> getExpireDateList(String search);

    //filtraggio per prodotti esauriti
    @Query("SELECT * FROM items WHERE quantity == 0")
    List<Item> getEmptyItems();

    //rimuovere quantità all'item con id passato in input
    @Query("UPDATE items SET expire = :value WHERE id = :id")
    void changeExpireDate(String id, String value);


}
