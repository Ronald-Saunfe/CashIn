package com.example.cashin;

public class purchaseInvestmentModel {
    private String Username,Email,Investments;

    public purchaseInvestmentModel(String username, String email, String investments ){
        this.Username= username;
        this.Email = email;
        this.Investments = investments;
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

    public void setInvestments(String investments){
        this.Investments = investments;
    }
    public String getInvestments(){
        return this.Investments;
    }

}
