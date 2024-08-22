package com.example.trackingmypantrygiacomochinilam;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {

    @PrimaryKey
    @NonNull private String id;

    @ColumnInfo(name = "barcode")
    @NonNull private String barcode;
    @ColumnInfo(name = "name")
    @NonNull private String name;
    @ColumnInfo(name = "description")
    @NonNull private String description;
    @ColumnInfo(name = "date")
    private String date;//private Calendar date;
    @ColumnInfo(name = "img", typeAffinity = ColumnInfo.BLOB)
    private byte[] img;//private Bitmap img;
    @ColumnInfo(name = "expire")
    private String expire;//private Calendar expire;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "quantity")
    private Integer quantity;

    public Item(@NonNull String id, @NonNull String barcode, @NonNull String name, @NonNull String description, String date, byte[] img, String expire, String type, Integer quantity) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.date = date;
        this.img = img;
        this.expire = expire;
        this.type = type;
        this.quantity = quantity;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    @NonNull
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(@NonNull String barcode) {
        this.barcode = barcode;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
