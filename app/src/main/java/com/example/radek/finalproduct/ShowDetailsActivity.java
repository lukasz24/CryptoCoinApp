package com.example.radek.finalproduct;

import android.app.ProgressDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;



public class ShowDetailsActivity extends AppCompatActivity {
    KryptoDAO kryptoDAO;
    Krypto krypto;
    String waluta = "usd";
    double staraCena = 0;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        Intent intent = getIntent();
        int id = intent.getIntExtra(Rynek.ID_CLICK, 0);

        TextView tv = (TextView) findViewById(R.id.opis);
        Resources res = getResources();
        String[] opisWaluty = res.getStringArray(R.array.krypto_description);
        tv.setText(opisWaluty[id]);

        kryptoDAO = new KryptoDAO(this);
        krypto = kryptoDAO.getKryptoById(id);
        staraCena = krypto.getAnkualnaCena();
        pobierzDane(id);




    }

    private void pobierzDane(int id){
        sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        String api = "https://api.cryptonator.com/api/ticker/";
        waluta = sp.getString("wybranaWaluta", "usd");
        kryptoDAO = new KryptoDAO(this);

        krypto = kryptoDAO.getKryptoById(id);
        api += krypto.getShortName().toLowerCase() + "-" + waluta.toLowerCase();
        new WebServiceHandler().execute(api);
    }
    private void refreshLayout(){
        sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        waluta = sp.getString("wybranaWaluta", "usd");

        TextView tv = (TextView)findViewById(R.id.pelnaNazwa);
        tv.setText(krypto.toString());

        ImageView iv = (ImageView)findViewById(R.id.logoDetails);
        iv.setImageResource(krypto.getObrazek());


        tv = (TextView)findViewById(R.id.usdPrice);
        tv.setText("Aktualna cena jednostki: " + krypto.getAnkualnaCena() + " " + waluta.toUpperCase());
        tv = (TextView)findViewById(R.id.usdChange);
        if(krypto.getZmiana() > 0){
            tv.setTextColor(Color.rgb(0, 255, 0));
        }else if (krypto.getZmiana() < 0){
            tv.setTextColor(Color.rgb(255, 0, 0));
        }
        tv.setText("Zmiana na rynku: " + krypto.getZmiana() + " " + waluta.toUpperCase());
        tv = (TextView)findViewById(R.id.lastVisit);

        double lVisit = showWarning();
        if(lVisit < 0){
            tv.setTextColor(Color.rgb(255, 0, 0));
        }else if (lVisit > 0){
            tv.setTextColor(Color.rgb(0, 255, 0));
        }
        tv.setText("Zmiana od ostatniej wizyty: " + lVisit + " " + waluta.toUpperCase());


    }
    private double showWarning(){
        String wybranyProcent = sp.getString("powiadomienia", "10%");
        String[] spliter = wybranyProcent.split("%");

        double wProcent = Double.valueOf(spliter[0])/100;
        double nowaCena = krypto.getAnkualnaCena();
        long wynik = Math.round((nowaCena - staraCena)*1000);
        double w = (double)wynik;
        w = w/1000;
        double procent = w/staraCena;
        if(procent < 0){
            procent = -procent;
        }
        if(wProcent < procent) {
            Toast.makeText(this, "OSTRZEŻENIE: cena waluty przekroczyła dopuszcalny próg: " + wProcent*100 + "%" , Toast.LENGTH_LONG).show();
        }
        return w;
    }

    class WebServiceHandler extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(ShowDetailsActivity.this);


        @Override
        protected void onPreExecute() {
            dialog.setTitle("Pobieranie danych z sieci");
            dialog.setMessage("Czekaj...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                InputStream in = new BufferedInputStream(
                        connection.getInputStream());

                return streamToString(in);

            } catch (Exception e) {

                Log.d(MainActivity.class.getSimpleName(), e.toString());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jObiect;
            JSONObject nodeStats;
            Double sPrice;
            Double sChange;
            String skrot;
            dialog.dismiss();

            try {

                KryptoDAO kryptoDAO = new KryptoDAO(ShowDetailsActivity.this);

                 jObiect  = new JSONObject(result);
                 nodeStats = jObiect.getJSONObject("ticker");
                 sPrice = nodeStats.getDouble("price");
                 sChange = nodeStats.getDouble("change");
                 skrot = nodeStats.getString("base");

                int poPrzecinku = 1000;
                krypto = kryptoDAO.getKryptoByShort(skrot);
                long konvert = Math.round(sPrice*poPrzecinku);
                sPrice = (double)konvert;
                konvert = Math.round(sChange*poPrzecinku);
                sChange = (double)konvert;

                krypto.setAnkualnaCena(sPrice/poPrzecinku);
                krypto.setZmiana(sChange/poPrzecinku);
                kryptoDAO.updateKrypto(krypto);
            } catch (Exception e) {
            }
            refreshLayout();
        }
        public String streamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            try {

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                reader.close();

            } catch (IOException e) {


            }

            return stringBuilder.toString();
        }

    }

}
