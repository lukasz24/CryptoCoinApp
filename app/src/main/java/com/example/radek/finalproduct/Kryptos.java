package com.example.radek.finalproduct;

/**
 * Created by Admin on 2017-05-24.
 */

public interface Kryptos {
    String TABLE_CRYPTO = "kryptowaluty";

    interface Columns {
        String KEY_ID = "id";
        String KEY_SHORT = "short";
        String KEY_NAME = "kryptowaluty";
        String KEY_WATCH = "obserwowane";
        String KEY_CURRENT = "obecnaWartosc";
        String KEY_CHANGE = "zmiana";
        String KEY_IMAGE = "obrazek";
    }

}
