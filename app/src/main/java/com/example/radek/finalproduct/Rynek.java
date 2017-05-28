package com.example.radek.finalproduct;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rynek extends AppCompatActivity {
    ListView list;

    public static final String ID_CLICK = "com.example.radek.finalproduct.ID_CLICK";
    private AlertDialog alert;
    AdapterListy adapter;
    /*
    private String[] fullName;
    private String[] shortName;
    private  Integer[] imageId = {
            R.drawable.btc,
            R.drawable.xrp,
            R.drawable.eth,
            R.drawable.nem,
            R.drawable.ltc,
            R.drawable.dash,
            R.drawable.etc,
            R.drawable.xlm,
            R.drawable.xmr,
            R.drawable.bcn,
            R.drawable.steem,
            R.drawable.gnt,
            R.drawable.rep,
            R.drawable.doge,
            R.drawable.maid,
            R.drawable.gno,
            R.drawable.bts,
            R.drawable.waves,
            R.drawable.game,
            R.drawable.plnc

    };
    */
    private ArrayList<Object> kryptoObekty = new ArrayList<Object>();
    KryptoDAO kryptoDAO;
    private Krypto[] kryptoTab = new Krypto[20];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_walut);
    /*
        Resources res = getResources();
        fullName = res.getStringArray(R.array.krypto_name_full);
        shortName = res.getStringArray(R.array.krypto_name_short);
*/
        setList();
        TextView tv = (TextView) findViewById(R.id.empty);
        if(adapter.isEmpty()) {
            ListView lv = (ListView)findViewById(R.id.list);
            lv.setVisibility(View.GONE);
        }else{
            tv.setVisibility(View.GONE);
        }
    }
    private void setList(){
        kryptoDAO = new KryptoDAO(this);
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);

        String waluta = sp.getString("wybranaWaluta","");

        for(int i = 0; i < kryptoTab.length; i++){
            Krypto krypto = kryptoDAO.getKryptoById(i);
            kryptoTab[i] = krypto;
        }

        final ArrayList<String> itemListName = new ArrayList<String>();
        ArrayList<String> vart = new ArrayList<String>();
        final ArrayList<Integer> icons = new ArrayList<Integer>();
        final ArrayList<Integer> idKrypto = new ArrayList<Integer>();


        for (int i = 0; i < kryptoTab.length; i++) {

            if(kryptoTab[i].getObserwowane().equals("true") || kryptoTab[i].getObserwowane() == "true"){

                itemListName.add(kryptoTab[i].toString());
                if(kryptoTab[i].getAnkualnaCena() <= 0.0 && kryptoTab[i].getZmiana() == 0.0){
                    vart.add("Kliknij aby pobrać aktualne wartości");
                }else {
                    vart.add("Cena: " + kryptoTab[i].getAnkualnaCena() +" "+waluta +"     Zmiana: " + kryptoTab[i].getZmiana()+" "+waluta);
                }
                icons.add(kryptoTab[i].getObrazek());
                idKrypto.add(kryptoTab[i].getId());
            }
        }

        adapter = new AdapterListy(this, itemListName, vart, icons);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(sprawdzPolaczenie()){
                    Intent pokazWiecej = new Intent(Rynek.this, ShowDetailsActivity.class);
                    pokazWiecej.putExtra(ID_CLICK, idKrypto.get(position));
                    startActivity(pokazWiecej);
                }else{
                    Toast.makeText(getApplicationContext(), "Brak połączenia z internetem", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void onResume(){
        super.onResume();
        setList();
    }

    public void addCrypto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        String[] labels = new String[kryptoTab.length];
        final boolean[] checked = new boolean[kryptoTab.length];
        for(int i = 0; i < kryptoTab.length; i++){
            Krypto kr = kryptoTab[i];
            labels[i] = kr.toString();

            if(kr.getObserwowane().equals("true") || kr.getObserwowane() == "true"){
                checked[i] = true;
            }else{
                checked[i] = false;
            }

        }

        // builder.setTitle(R.string.choiceCrypto)
        builder.setTitle("Wybierz obserwowane Kryptowaluty")

                .setMultiChoiceItems(labels, checked,
                        new DialogInterface.OnMultiChoiceClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checked[which] = isChecked;

                            }
                        })

                //R.string.ok - zamiast "ok"
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        for(int i = 0; i < kryptoTab.length; i++) {
                            kryptoTab[i].setObserwowane("" + checked[i]);
                            kryptoDAO.updateKrypto(kryptoTab[i]);
                        }
                        alert.dismiss();
                        recreate();

                    }
                })
                //R.string.cancel - zamiast ""
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        alert.dismiss();

                    }
                });
        alert = builder.create();
        alert.show();
    }

    public boolean sprawdzPolaczenie() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
          connected = true;
        } else {
           connected = false;
        }

        return connected;
    }
}