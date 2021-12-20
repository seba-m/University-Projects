package com.seba.inventariado.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Users {

    @SerializedName("email")
    public String Email;
    @SerializedName("id")
    public String userID;
    @SerializedName("firstName")
    public String FirstName;
    @SerializedName("lastName")
    public String LastName;
    @SerializedName("photo")
    public String Photo;
    @SerializedName("token")
    public String token;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return Email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getToken() {
        return token;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public String getPhoto() {
        return Photo;
    }

    @NotNull
    @Override
    public String toString() {
        return "Users{" +
                "Email='" + Email + '\'' +
                ", userID='" + userID + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Photo='" + Photo + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
