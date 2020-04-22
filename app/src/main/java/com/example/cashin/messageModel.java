package com.example.cashin;

public class messageModel {
    private String Date, Message, Pic;

    public messageModel(String date, String Message, String Pic ){
        this.Date= date;
        this.Message = Message;
        this.Pic = Pic;
    }

    public void setDate(String date){
        this.Date = date;
    }
    public String getDate(){
        return this.Date;
    }

    public void setMessage(String Message){
        this.Message = Message;
    }
    public String getMessage(){
        return this.Message;
    }

    public void setPic(String Pic){
        this.Pic = Pic;
    }
    public String getPic(){
        return this.Pic;
    }

}
