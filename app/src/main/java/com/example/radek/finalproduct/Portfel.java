package com.example.radek.finalproduct;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Portfel extends AppCompatActivity {
    private String file = "listaPortfela", kryptoWpisana, krypto;
    private EditText ilosc, iloscOd;
    private TextView sumaPortf,  TVNazwa, TVSztuki, TVWartosc;
    private Spinner kryptowaluta, kryptowalutaOd;
    private double sztuki, suma, sztukiWisane, obecnaSuma, zapisanaSuma, zmianaProcentowaSumy, zmianaSumy;
    private TableLayout tab;
    private TableRow row1;
    private ArrayList<String> krypWal = new ArrayList<>();
    private ArrayList<String> wszystkieStringi = new ArrayList<>();
    private ArrayList<Double> sztukiDouble = new ArrayList<>();
    private LinearLayout lin, linnowy;
    private int ileLini = 0;
    KryptoDAO krypDAO;
    Krypto kryptoO;

    int czyJuz=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfel_layout);

        boolean polaczony = sprawdzPolaczenie();
        if(polaczony)
            pobierzDane();
        else
        {

            new AlertDialog.Builder(Portfel.this)
                    .setTitle("Uwaga!")
                    .setMessage("Nie ma połączenia z Internetem, więc dane mogą być nieaktualne.")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    }).show();
            refreshLayout();
        }
    }

        private void pobierzDane() {
            SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
            String waluta = sp.getString("wybranaWaluta", "");
            for (int i = 0; i < 20; i++) {
                String api = "https://api.cryptonator.com/api/ticker/";
                krypDAO = new KryptoDAO(this);
                kryptoO = krypDAO.getKryptoById(i);
                api = api + kryptoO.getShortName().toLowerCase() + "-" + waluta.toLowerCase(); //Tu trzeba dodać jeszcze według jakiej walut ma pobierać
                new AllDataGetter().execute(api);
            }
        }

        private void refreshLayout() {
            try {
                odczytajListe();
            } catch (FileNotFoundException e) {}
            przeliczWartosc();
            wyswietlacz(ileLini);
            ustawSume();
            DecimalFormat df = new DecimalFormat("#.##");
            zmianaSumy = porownajSume();
            String zmianaSumySt = df.format(zmianaSumy);
            if(zmianaSumySt.equals("0"))
            {}
            else if (zmianaSumy > 0 && zmianaSumy < 99)
                Toast.makeText(Portfel.this, "Od ostatniej wizyty wartość Twojego portfela wzrosła o " + zmianaSumySt + "%", Toast.LENGTH_LONG).show();
            else if (zmianaSumy < 0 && zmianaSumy > -99)
                Toast.makeText(Portfel.this, "Od ostatniej wizyty wartość Twojego portfela spadła o " + zmianaSumySt + "%", Toast.LENGTH_LONG).show();
        }

        class AllDataGetter extends AsyncTask<String, Void, String> {
            private ProgressDialog dialog = new ProgressDialog(Portfel.this);
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
                String target;
                dialog.dismiss();

                try {

                    KryptoDAO kryptoDAO = new KryptoDAO(Portfel.this);

                    jObiect = new JSONObject(result);
                    nodeStats = jObiect.getJSONObject("ticker");
                    sPrice = nodeStats.getDouble("price");
                    sChange = nodeStats.getDouble("change");
                    skrot = nodeStats.getString("base");
                    target = nodeStats.getString("target");
                    kryptoO = kryptoDAO.getKryptoByShort(skrot);
                    long konvert = Math.round(sPrice * 1000);
                    sPrice = (double) konvert;
                    konvert = Math.round(sChange * 1000);
                    sChange = (double) konvert;
                    kryptoO.setAnkualnaCena(sPrice / 1000);
                    kryptoO.setZmiana(sChange / 1000);
                    kryptoDAO.updateKrypto(kryptoO);
                } catch (Exception e) {
                }
                czyJuz++;
                if(czyJuz==20) {
                    refreshLayout();
                    czyJuz=0;
                }
                else{}
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
                } catch (IOException e) {}
                return stringBuilder.toString();
            }
        }

    public void usun()
    {
        for(int i=0; i<wszystkieStringi.size(); i++) {
            lin = (LinearLayout) findViewById(R.id.linear2);
            final LinearLayout childlin = (LinearLayout) lin.findViewById(R.id.tenLay);
            final TableLayout childtab = (TableLayout) childlin.findViewById(R.id.table);
            final TableRow childtabrow = (TableRow) childtab.findViewById(R.id.row);
            final TextView childtext = (TextView) childtabrow.findViewById(R.id.txt1);
            childtext.setId(R.id.txt2);
            childtab.setId(R.id.table2);
            childtabrow.setId(R.id.row2);
            childtabrow.removeAllViews();
            childtab.removeAllViews();
        }
    }

    public void onResume()
    {
        super.onResume();
        usun();
        przeliczWartosc();
        wyswietlacz(ileLini);
    }

    public void onPause()
    {
        zapisz();
        super.onPause();
   }
    public double sumaPortfela()
    {
        suma = 0.0;
        for(int i =0; i < wszystkieStringi.size(); i++)
        {
            Double iloscKasy;
            String ciag, czescWalutowa;
            ciag = wszystkieStringi.get(i);
            String[] parts = ciag.split(" ");
            czescWalutowa = parts[2];
            iloscKasy = Double.parseDouble(czescWalutowa);
            suma += iloscKasy;
        }
        return suma;

    }

    public void przechowajSume()
    {
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        DecimalFormat df = new DecimalFormat("#.##");
        obecnaSuma = sumaPortfela();
        String sumaKasyWPortfelu;
        sumaKasyWPortfelu = df.format(obecnaSuma);
        obecnaSuma = Double.parseDouble(sumaKasyWPortfelu);
        editor.putString("ostatniaSuma",obecnaSuma+"");
        editor.commit();
    }

    public double porownajSume()
    {
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        DecimalFormat df = new DecimalFormat("#.##");
        obecnaSuma = sumaPortfela();
        String sumaKasyWPortfelu;
        sumaKasyWPortfelu = df.format(obecnaSuma);
        obecnaSuma = Double.parseDouble(sumaKasyWPortfelu);
        if(sp.getString("ostatniaSuma","").isEmpty())
        {
            editor.putString("ostatniaSuma",obecnaSuma+"");
            editor.commit();
        }
        else
        {
            zapisanaSuma = Double.parseDouble(sp.getString("ostatniaSuma",""));
            zmianaProcentowaSumy = ((obecnaSuma - zapisanaSuma) / zapisanaSuma ) * 100;
        }
        return zmianaProcentowaSumy;
    }

    public void zapisz()
    {
        przechowajSume();
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(file, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter out = new PrintWriter(fos);
        for(int i=0; i<wszystkieStringi.size();i++)
        {
            if(i==0) {
                String cos = wszystkieStringi.get(i);
                out.print(cos);
                out.flush();
            }
            else
            {
                String cos = wszystkieStringi.get(i);
                out.print("\r\n"+cos);
                out.flush();
            }
        }
        out.close();
    }

    public void odczytajListe() throws FileNotFoundException {
        FileInputStream fis = openFileInput(file);
        Scanner in = new Scanner(fis);
        while(in.hasNext())
        {
            String line = in.nextLine();
            wszystkieStringi.add(line);
            ileLini++;
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void przeliczWartosc()
    {
        String linia="", czescNazwy="", czescSztuk="", wartoscSt="";
        Double kurs=0.0, wartosc = 0.0;
        for(int i=0; i<wszystkieStringi.size(); i++)
        {
            krypDAO = new KryptoDAO(this);
            linia = wszystkieStringi.get(i);
            String[] parts = linia.split(" ");
            czescNazwy = parts[0];
            if(czescNazwy.equals("NewEconomyMovement"))
                czescNazwy = "New Economy Movement";
            if(czescNazwy.equals("EthereumClassic"))
                czescNazwy = "Ethereum Classic";
            if(czescNazwy.equals("StellarLumens"))
                czescNazwy = "Stellar Lumens";
            czescSztuk = parts[1];
            Krypto kryptoO = krypDAO.getKryptoByName(czescNazwy);
            kurs = kryptoO.getAnkualnaCena();
            wartosc = Double.parseDouble(czescSztuk) * kurs;
            DecimalFormat df = new DecimalFormat("#.##");
            wartoscSt = df.format(wartosc);
            if(czescNazwy.equals("New Economy Movement"))
                czescNazwy = "NewEconomyMovement";
            if(czescNazwy.equals("Ethereum Classic"))
                czescNazwy = "EthereumClassic";
            if(czescNazwy.equals("Stellar Lumens"))
                czescNazwy = "StellarLumens";
            String superString = czescNazwy +" " +czescSztuk +" " +wartoscSt;
            wszystkieStringi.set(i,superString);
        }
    }

    public void dodajDoTablicy(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Waluty");
        builder.setMessage("Wybierz walutę, którą chcesz dodać do portfela:");
        builder.setCancelable(false);
        AlertDialog alert;
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.waluty_lista, null);
        builder.setView(dialogView);
        ilosc = (EditText)dialogView.findViewById(R.id.ilosc);
        kryptowaluta = (Spinner)dialogView.findViewById(R.id.nazwa);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                usun();
                String sztukiString = ilosc.getText().toString();
                krypto = kryptowaluta.getSelectedItem().toString();
                if(krypto.equals("New Economy Movement"))
                    krypto = "NewEconomyMovement";
                if(krypto.equals("Ethereum Classic"))
                    krypto = "EthereumClassic";
                if(krypto.equals("Stellar Lumens"))
                    krypto = "StellarLumens";
                if(sztukiString.matches(""))
                    Toast.makeText(Portfel.this, "Proszę coś wpisać.", Toast.LENGTH_LONG).show();
                else {
                    sztuki = Double.parseDouble(sztukiString);
                    if (sztuki < 0)
                        Toast.makeText(Portfel.this, "Proszę podać liczbę dodatnią.", Toast.LENGTH_LONG).show();
                    else {
                        krypWal.add(krypto);
                        sztukiDouble.add(sztuki);
                        int wiel = krypWal.size();
                        String poprzedni = "";
                        String superString;
                        int ktory = 0;
                        String czescNazwy = "", czescSztuk = "";
                        Double sztuki = 0.0;
                        boolean byl = false;
                        for (int x = 0; x < wszystkieStringi.size(); x++) {
                            poprzedni = wszystkieStringi.get(x);
                            String[] parts = poprzedni.split(" ");
                            czescNazwy = parts[0];
                            czescSztuk = parts[1];
                            if (krypWal.get(wiel - 1).equals(czescNazwy)) {
                                byl = true;
                                ktory = x;
                                break;
                            }
                        }
                        if (!byl) {
                            superString = krypWal.get(wiel - 1) + " " + sztukiDouble.get(wiel - 1);
                            wszystkieStringi.add(superString);
                            ileLini++;
                        } else if (byl) {
                            sztuki = Double.parseDouble(czescSztuk);
                            sztuki += sztukiDouble.get(wiel - 1);
                            DecimalFormat df = new DecimalFormat("#.########");
                            String sztukiSt = df.format(sztuki);
                            superString = krypWal.get(wiel - 1) + " " + sztukiSt;
                            wszystkieStringi.set(ktory, superString);
                        }
                    }
                }
                przeliczWartosc();
                ustawSume();
                wyswietlacz(wszystkieStringi.size());
            }
        });
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alert = builder.create();
        alert.show();

    }

    public void odejmijOdTablicy(View vie)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Waluty");
        builder.setMessage("Wybierz walutę, którą wydałeś:");
        builder.setCancelable(false);
        AlertDialog alert;
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.waluty_lista, null);
        builder.setView(dialogView);
        iloscOd = (EditText)dialogView.findViewById(R.id.ilosc);
        kryptowalutaOd = (Spinner)dialogView.findViewById(R.id.nazwa);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                usun();
                kryptoWpisana = kryptowalutaOd.getSelectedItem().toString();
                String sztukiWpisaneString = iloscOd.getText().toString();
                if(sztukiWpisaneString.matches(""))
                    Toast.makeText(Portfel.this, "Proszę coś wpisać.", Toast.LENGTH_LONG).show();
                else {
                    sztukiWisane = Double.parseDouble(iloscOd.getText().toString());
                    if (sztukiWisane < 0)
                        Toast.makeText(Portfel.this, "Proszę podać liczbę dodatnią.", Toast.LENGTH_LONG).show();
                    else {
                        String poprzedni = "";
                        String superString;
                        int ktory = 0;
                        String czescNazwy = "", czescSztuk = "";
                        Double sztukiByly = 0.0;
                        boolean byl = false;
                        for (int x = 0; x < wszystkieStringi.size(); x++) {
                            poprzedni = wszystkieStringi.get(x);
                            String[] parts = poprzedni.split(" ");
                            czescNazwy = parts[0];
                            if (czescNazwy.equals("NewEconomyMovement"))
                                czescNazwy = "New Economy Movement";
                            if (czescNazwy.equals("EthereumClassic"))
                                czescNazwy = "Ethereum Classic";
                            if (czescNazwy.equals("StellarLumens"))
                                czescNazwy = "Stellar Lumens";
                            czescSztuk = parts[1];
                            if (kryptoWpisana.equals(czescNazwy)) {
                                byl = true;
                                ktory = x;
                                break;
                            }
                        }
                        if (!byl) {
                            Toast.makeText(Portfel.this, "Nie możesz odjąć czegoś czego nie masz", Toast.LENGTH_LONG).show();
                        } else if (byl) {
                            if (czescNazwy.equals("New Economy Movement"))
                                czescNazwy = "NewEconomyMovement";
                            if (czescNazwy.equals("Ethereum Classic"))
                                czescNazwy = "EthereumClassic";
                            if (czescNazwy.equals("Stellar Lumens"))
                                czescNazwy = "StellarLumens";
                            sztukiByly = Double.parseDouble(czescSztuk);
                            sztukiByly -= sztukiWisane;
                            DecimalFormat df = new DecimalFormat("#.########");
                            String sztukiSt = df.format(sztukiByly);
                            superString = czescNazwy + " " + sztukiSt + " ";
                            if (sztukiByly == 0.0) {
                                wszystkieStringi.subList(ktory, ktory + 1).clear();
                            } else if (sztukiByly < 0) {
                                Toast.makeText(Portfel.this, "Nie możesz odjąć więcej niż masz.", Toast.LENGTH_LONG).show();
                            } else
                                wszystkieStringi.set(ktory, superString);
                            przeliczWartosc();
                        }
                    }
                }
                przeliczWartosc();
                ustawSume();
                wyswietlacz(wszystkieStringi.size());
            }
        });
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alert = builder.create();
        alert.show();
    }

    /* //do wyczyszczenia pliku w razie potrzeby
    public void czyscPlik(View view) throws FileNotFoundException {
        usun();
    }
    */

    public void wyswietlacz(int ile)
    {
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        lin = (LinearLayout)findViewById(R.id.tenLay);
        String nazwa, sztuka, wartosc;
        for(int i=0;i<ile;i++) {
            linnowy = new LinearLayout(this);
            linnowy.setOrientation(LinearLayout.VERTICAL);
            linnowy.setId(R.id.lay);
            tab = new TableLayout(this);
            tab.setId(R.id.table);
            tab.setStretchAllColumns(true); //test
            row1 = new TableRow(this);
            row1.setId(R.id.row);
            TVNazwa = new TextView(this);
            TVSztuki = new TextView(this);
            TVWartosc = new TextView(this);
            row1.setGravity(Gravity.CENTER_HORIZONTAL);
            String linia = wszystkieStringi.get(i);
            String[] parts = linia.split(" ");
            nazwa = parts[0];
            sztuka = parts[1];
            wartosc = parts[2];
            TVNazwa.setText(i+1+". "+nazwa);
            TVSztuki.setText(sztuka);
            TVWartosc.setText(wartosc+" "+sp.getString("wybranaWaluta",""));
            TVNazwa.setId(R.id.txt1);
            TVSztuki.setId(R.id.txt2);
            TVWartosc.setId(R.id.txt3);
            TVNazwa.setTextSize(16);
            TVNazwa.setTextColor(Color.parseColor("#EEE6E6"));
            TVNazwa.setTypeface(null, Typeface.BOLD);
            TVSztuki.setTextSize(16);
            TVSztuki.setTextColor(Color.parseColor("#EEE6E6"));
            TVSztuki.setTypeface(null, Typeface.BOLD);
            TVWartosc.setTextSize(16);
            TVWartosc.setTextColor(Color.parseColor("#EEE6E6"));
            TVWartosc.setTypeface(null, Typeface.BOLD);
            TVNazwa.setGravity(Gravity.LEFT);
            TVSztuki.setGravity(Gravity.LEFT);
            TVWartosc.setGravity(Gravity.LEFT);
            TableRow.LayoutParams paramsN = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f);
            TableRow.LayoutParams paramsS = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            TableRow.LayoutParams paramsW = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            TVNazwa.setLayoutParams(paramsN);
            TVSztuki.setLayoutParams(paramsS);
            TVWartosc.setLayoutParams(paramsW);
            row1.addView(TVNazwa);
            row1.addView(TVSztuki);
            row1.addView(TVWartosc);
            tab.addView(row1);
            linnowy.addView(tab);
            lin.addView(linnowy);
        }
    }

    public void ustawSume()
    {
        Double sm;
        DecimalFormat df = new DecimalFormat("#.##");
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        sumaPortf = (TextView)findViewById(R.id.ppp);
        Double tymczasowyDouble;
        tymczasowyDouble = sumaPortfela();
        String sumaKasyWPortfelu;
        sumaKasyWPortfelu = df.format(tymczasowyDouble);
        String toWyswietl = sumaKasyWPortfelu + " "+sp.getString("wybranaWaluta","");
        sumaPortf.setText(toWyswietl);
        sm = porownajSume();
        if(sm>0)
            sumaPortf.setTextColor((Color.parseColor("#00FF00")));
        else if(sm<0)
            sumaPortf.setTextColor((Color.parseColor("#FF0000")));
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

