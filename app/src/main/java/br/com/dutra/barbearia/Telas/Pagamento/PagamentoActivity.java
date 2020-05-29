package br.com.dutra.barbearia.Telas.Pagamento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import br.com.dutra.barbearia.R;

public class PagamentoActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        webView = findViewById(R.id.webView);
        webView.loadUrl("https://www.mercadopago.com.br/checkout/v1/redirect?pref_id=178788434-d0c3ffec-90e5-4929-870c-5d0076d44cdc");


    }
}
