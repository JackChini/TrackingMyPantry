package com.example.trackingmypantrygiacomochinilam;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey
    @NonNull private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "barcode")
    private String barcode;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name="image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Product(String id, String name, String barcode, String description, byte[] image){
        this.id=id;
        this.name=name;
        this.barcode=barcode;
        this.description = description;
        this.image = image;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String desc){
        this.description =desc;
    }

    public byte[] getImage(){
        return image;
    }
    public void setImage(byte[] img){
        this.image = img;
    }

}
