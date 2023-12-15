package com.example.myapplicationtest;

public class MainUserPlants {
    String name, comname, sciname, symbol, waterusage, surl;


    MainUserPlants(){

    }
    public MainUserPlants(String name, String comname, String sciname, String symbol, String waterusage, String surl) {
        this.name = name;
        this.comname = comname;
        this.sciname = sciname;
        this.symbol = symbol;
        this.waterusage = waterusage;
        this.surl = surl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }
}
