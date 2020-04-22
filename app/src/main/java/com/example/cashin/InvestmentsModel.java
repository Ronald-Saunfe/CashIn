package com.example.cashin;

public class InvestmentsModel {
    private float Price, Commission, Quantity;

    public InvestmentsModel(float price, float commission, float quantity ){
        this.Commission= commission;
        this.Price = price;
        this.Quantity = quantity;
    }

    public void setPrice(float price){
        this.Price = price;
    }
    public float getPrice(){
        return this.Price;
    }

    public void setCommission(float commission){
        this.Commission = commission;
    }
    public float getCommission(){
        return this.Commission;
    }

    public void setQuantity(float quantity){
        this.Quantity = quantity;
    }
    public float getQuantity(){
        return this.Quantity;
    }
}
