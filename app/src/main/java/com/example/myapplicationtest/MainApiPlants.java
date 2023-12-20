package com.example.myapplicationtest;

public class MainApiPlants {
    String comname, sciname, symbol, waterusage;


    MainApiPlants(){

    }
    public MainApiPlants(String comname, String sciname, String symbol, String waterusage) {
        this.comname = comname;
        this.sciname = sciname;
        this.symbol = symbol;
        this.waterusage = waterusage;
    }


    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public String getSciname() {
        return sciname;
    }

    public void setSciname(String sciname) {
        this.sciname = sciname;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getWaterusage() {
        return waterusage;
    }

    public void setWaterusage(String waterusage) {
        this.waterusage = waterusage;
    }
}
