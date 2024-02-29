package com.example.babyneeds.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = User.TABLE_NAME, indices = {@Index(value = {"email"}, unique = true)})
public class User {
    public static final String TABLE_NAME = "User";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Name")
    private String name;


    @ColumnInfo(name = "email")
    private String email;


    @ColumnInfo(name = "password")
    private String password;


    @ColumnInfo(name = "mother_name")
    private String motherName;


    @ColumnInfo(name = "mother_dob")
    private String motherDob;


    @ColumnInfo(name = "deliveryDate")
    private String deliveryDate;

    @ColumnInfo(name = "image")
    private String image;

    public User(@NonNull String name, @NonNull String email, @NonNull String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @Nullable
    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(@Nullable String motherName) {
        this.motherName = motherName;
    }

    @Nullable
    public String getMotherDob() {
        return motherDob;
    }

    public void setMotherDob(@Nullable String motherDob) {
        this.motherDob = motherDob;
    }

    @Nullable
    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(@Nullable String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
