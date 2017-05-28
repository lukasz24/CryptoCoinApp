package com.example.radek.finalproduct;

/**
 * Created by Admin on 2017-05-24.
 */

public class Krypto {
    private Integer id;
    private String shortName;
    private String longName;
    private String obserwowane;
    private double ankualnaCena;
    private double zmiana;
    private int obrazek;

    public String getShortName(){
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public Integer getId() {
        return id;
    }

    public String getObserwowane() {
        return obserwowane;
    }

    public double getAnkualnaCena() {
        return ankualnaCena;
    }

    public double getZmiana() {
        return zmiana;
    }

    @Override
    public String toString(){
        return longName + " (" + shortName + ")";
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setObserwowane(String obserwowane) {
        this.obserwowane = obserwowane;
    }

    public void setAnkualnaCena(double ankualnaCena) {
        this.ankualnaCena = ankualnaCena;
    }

    public void setZmiana(double zmiana) {
        this.zmiana = zmiana;
    }

    public int getObrazek() {
        return obrazek;
    }

    public void setObrazek(int obrazek) {
        this.obrazek = obrazek;
    }
}
