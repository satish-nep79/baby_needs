package com.example.babyneeds.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = Item.TABLE_NAME,
        foreignKeys = {@ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId"
        )}
)
public class Item {
    public static final String TABLE_NAME = "Item";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Name")
    private String itemName;

    @ColumnInfo(name = "Desc")
    private String itemDesc;

    @ColumnInfo(name = "Price")
    private double itemPrice;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "isPurchased")
    private boolean isPurchased;

    @ColumnInfo(name = "userId")
    private int userId;

    public Item(String itemName, String itemDesc, double itemPrice, String image, int userId) {
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemPrice = itemPrice;
        this.image = image;
        this.isPurchased = false;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
