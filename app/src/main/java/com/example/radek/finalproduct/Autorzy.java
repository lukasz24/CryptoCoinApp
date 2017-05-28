package com.example.radek.finalproduct;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Autorzy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorzy_layout);
    }

    public void mail(View view)
    {
        String tytul = "Mail z aplikacji CryptoCoinAPP";
        String tresc = "Zgłaszam następujące uwagi: ";
        String[] adres  = {"lukasz.pudzisz@gmail.com", "rsmyksy@gmail.com"};
        Intent mail = new Intent(Intent.ACTION_SEND);
        mail.setType("mail/plain");
        mail.putExtra(Intent.EXTRA_SUBJECT, tytul);
        mail.putExtra(Intent.EXTRA_TEXT , tresc);
        mail.putExtra(Intent.EXTRA_EMAIL  , adres);
        startActivity(Intent.createChooser(mail, "Wyślij maila do " +adres));
        finish();
    }
}
