package com.example.marcos.chatexample.Models;

public class Chat {

    private String Message, Sender, Receiver, Time, Date;

    public Chat(){

    }

    public Chat(String Message, String Sender, String Receiver, String Time, String Date){
        this.Message = Message;
        this.Sender = Sender;
        this.Receiver = Receiver;
        this.Time = Time;
        this.Date = Date;
    }

    public String getDate() {
        return Date;
    }

    public String getMessage() {
        return Message;
    }

    public String getReceiver() {
        return Receiver;
    }

    public String getSender() {
        return Sender;
    }

    public String getTime() {
        return Time;
    }
}
