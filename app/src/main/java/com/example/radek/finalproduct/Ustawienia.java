package com.example.radek.finalproduct;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Ustawienia extends AppCompatActivity {
    EditText st, no1, no2;
    String hasloStare="", hasloNowe1, hasloNowe2;
    Switch sw;
    Spinner waluty, powiadomienia;
    String waluta="";
    String powiadamia="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ustawienia_layout);
        waluty = (Spinner) findViewById(R.id.waluty);
        powiadomienia = (Spinner) findViewById(R.id.zmiana);
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        if(!sp.getString("wybranaWaluta", "").isEmpty())
        {
            waluta = sp.getString("wybranaWaluta", "");
            ArrayAdapter myAdap = (ArrayAdapter) waluty.getAdapter();
            int spinnerPosition = myAdap.getPosition(waluta);
            waluty.setSelection(spinnerPosition);
        }
        else
        {
            waluta = "USD";
            ArrayAdapter myAdap = (ArrayAdapter) waluty.getAdapter();
            int spinnerPosition = myAdap.getPosition(waluta);
            waluty.setSelection(spinnerPosition);
        }
        if(!sp.getString("powiadomienia", "").isEmpty())
        {
            powiadamia = sp.getString("powiadomienia", "");
            ArrayAdapter myAdap = (ArrayAdapter) powiadomienia.getAdapter();
            int spinnerPosition = myAdap.getPosition(powiadamia);
            powiadomienia.setSelection(spinnerPosition);
        }
        else
        {
            powiadamia = "5%";
            ArrayAdapter myAdap = (ArrayAdapter) powiadomienia.getAdapter();
            int spinnerPosition = myAdap.getPosition(powiadamia);
            powiadomienia.setSelection(spinnerPosition);
        }
        if(!sp.getString("haslo", "").isEmpty())
        {
            st = (EditText)findViewById(R.id.stare);
            st.setText(sp.getString("haslo", ""));
        }
    }

    public void zmianaSwitcha(View view) {
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        hasloStare = sp.getString("haslo","");
        sw = (Switch)findViewById(R.id.switch1);
        boolean on = sw.isChecked();
        st = (EditText)findViewById(R.id.stare);
        no1 = (EditText)findViewById(R.id.nowe1);
        no2 = (EditText)findViewById(R.id.nowe2);
        if(sp.getString("haslo","").isEmpty()) {
            if (on) {
                st.setEnabled(false);
                no1.setEnabled(true);
                no2.setEnabled(true);
            } else {
                st.setEnabled(false);
                no1.setEnabled(false);
                no2.setEnabled(false);
            }
        }
        else
        {
            if (on) {
                st.setEnabled(true);
                st.setText("");
                no1.setEnabled(true);
                no2.setEnabled(true);
            } else {
                st.setEnabled(false);
                no1.setEnabled(false);
                no2.setEnabled(false);
            }
        }
    }

    public void potwierdz(View view)
    {
        st = (EditText)findViewById(R.id.stare);
        no1 = (EditText)findViewById(R.id.nowe1);
        no2 = (EditText)findViewById(R.id.nowe2);
        SharedPreferences sp = getSharedPreferences("Ustawienia", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        sw = (Switch)findViewById(R.id.switch1);
        if(sw.isChecked())
        {
            hasloStare = st.getText().toString();
            hasloNowe1 = no1.getText().toString();
            hasloNowe2 = no2.getText().toString();
            if(st.isEnabled()==false)
            {
                if(hasloNowe1.equals(hasloNowe2))
                {
                    editor.putString("haslo", hasloNowe1);
                    Toast.makeText(Ustawienia.this, "Hasło poprawnie ustawione!", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Ustawienia.this, "Wprowadź hasło poprawnie dwa razy!", Toast.LENGTH_LONG).show();
            }
            else
            {
                if(sp.getString("haslo","").equals(hasloStare))
                {
                    if(hasloNowe1.equals(hasloNowe2))
                    {
                        editor.putString("haslo", hasloNowe1);
                        Toast.makeText(Ustawienia.this, "Hasło poprawnie ustawione!", Toast.LENGTH_LONG).show();
                    }
                else
                    Toast.makeText(Ustawienia.this, "Wprowadź hasło poprawnie dwa razy!", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Ustawienia.this, "Złe stare hasło!", Toast.LENGTH_LONG).show();
            }
        }
        final Spinner spinWaluty = (Spinner)findViewById(R.id.waluty);
        waluta = spinWaluty.getSelectedItem().toString();
        editor.putString("wybranaWaluta", waluta);
        final Spinner spinPow = (Spinner)findViewById(R.id.zmiana);
        powiadamia = spinPow.getSelectedItem().toString();
        editor.putString("powiadomienia", powiadamia);
        editor.commit();
        Toast.makeText(Ustawienia.this, "Zmiany zostały poprawnie wprowadzone", Toast.LENGTH_LONG).show();
    }

    public void anuluj(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
