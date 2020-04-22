package com.example.cashin;

public class myInvestmentsModel {
    private float Price, Quantity, Profitmade;
    private String datebought;

    public myInvestmentsModel(float price, String datebought, float quantity, float profitmade ){
        this.datebought= datebought;
        this.Price = price;
        this.Quantity = quantity;
        this.Profitmade = profitmade;
    }

    public void setProfitmade(float profitmade){
        this.Profitmade = profitmade;
    }
    public float getProfitmade(){
        return this.Profitmade;
    }

    public void setPrice(float price){
        this.Price = price;
    }
    public float getPrice(){
        return this.Price;
    }

    public void setDatebought(String datebought){
        this.datebought = datebought;
    }
    public String getDatebought(){
        return this.datebought;
    }

    public void setQuantity(float quantity){
        this.Quantity = quantity;
    }
    public float getQuantity(){
        return this.Quantity;
    }
}
