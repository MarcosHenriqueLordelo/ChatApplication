package com.example.marcos.chatexample.Models;

public class User {
    private String Name, PhotoUrl, Uid, Status;


    public User(){

    }

    public User(String Name, String PhotoUrl, String Uid, String Status){
        this.Name = Name;
        this.PhotoUrl = PhotoUrl;
        this.Uid = Uid;
        this.Status = Status;
    }

    public String getName(){
        return Name;
    }

    public String getPhotoUrl(){
        return PhotoUrl;
    }

    public String getUid(){
        return Uid;
    }

    public String getStatus() {
        return Status;
    }
}
