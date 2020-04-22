package com.example.cashin;

public class myrefereeModel {
    private String Username,Email;

    public myrefereeModel(String username, String email){
        this.Username= username;
        this.Email = email;
    }

    public void setUsername(String username){
        this.Username = username;
    }
    public String getUsername(){
        return this.Username;
    }

    public void setEmail(String email){
        this.Email = email;
    }
    public String getEmail(){
        return this.Email;
    }

}
