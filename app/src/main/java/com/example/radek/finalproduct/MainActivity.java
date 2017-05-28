package com.example.radek.finalproduct;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    KryptoDAO kryptoDAO = new KryptoDAO(this);
    private String[] fullName;
    private String[] shortName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        if(sp.getString("wybranaWaluta","").isEmpty())
        {
            editor.putString("wybranaWaluta","USD");
            editor.commit();
        }
        if(sp.getString("powiadomienia","").isEmpty())
        {
            editor.putString("powiadomienia","10%");
            editor.commit();
        }

        if(kryptoDAO.isTableEmpty()) {
            Resources res = getResources();
            fullName = res.getStringArray(R.array.krypto_name_full);
            shortName = res.getStringArray(R.array.krypto_name_short);
            Integer[] imageId = {
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
            for (int i = 0; i < shortName.length; i++) {
                Krypto krypto = new Krypto();
                krypto.setId(i);
                krypto.setShortName(shortName[i]);
                krypto.setLongName(fullName[i]);
                krypto.setObserwowane("false");
                krypto.setAnkualnaCena(0);
                krypto.setZmiana(0);
                krypto.setObrazek(imageId[i]);
                kryptoDAO.insertKrypto(krypto);
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_glowna, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.ustawienia){
            Intent i = new Intent(this, Ustawienia.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    public void autorzy(View view)
    {
        Intent intent = new Intent(this, Autorzy.class);
        startActivity(intent);
    }

    public void rynek(View view)
    {
        Intent intent = new Intent(this, Rynek.class);
        startActivity(intent);
    }

    public void portfel(View view)
    {
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        final Intent intent = new Intent(this, Portfel.class);
        if(!sp.getString("haslo", "").isEmpty()) {
            final String haslo = sp.getString("haslo", "");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hasło");
            builder.setMessage("Wprowadź hasło dostępu");
            builder.setCancelable(false);
            AlertDialog alert;
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.alert_view, null);
            builder.setView(dialogView);
            final EditText et = (EditText)dialogView.findViewById(R.id.inputPassword);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    final String wprowadzone = et.getText().toString();
                    if(wprowadzone.equals(haslo))
                        startActivity(intent);
                    else
                        Toast.makeText(MainActivity.this, "Podaj poprawne hasło!", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    // finish();
                }
            });
            alert = builder.create();
            alert.show();
        }
        else {
            startActivity(intent);
        }
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
