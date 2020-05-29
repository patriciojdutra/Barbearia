package br.com.dutra.barbearia.Telas.Sistema;

import android.content.Intent;
import android.os.Bundle;

import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Cliente.PainelInfoclientesActivity;
import br.com.dutra.barbearia.Telas.Corte.CadastroDeCortesActivity;
import br.com.dutra.barbearia.Telas.Produto.CadastroDeProdutoActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class GerenciamentoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciamento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }



    public void Cliente(View v){

        Intent it = new Intent(this, PainelInfoclientesActivity.class);
        startActivity(it);

    }

    public void atualizar(){



    }

    public void Atendimento(View v){

    }

    public void Corte(View v){

        Intent it = new Intent(this, CadastroDeCortesActivity.class);
        startActivity(it);

    }

    public void Produto(View v){

        Intent it = new Intent(this, CadastroDeProdutoActivity.class);
        startActivity(it);

    }

    public void Promocao(View v){

    }

}
