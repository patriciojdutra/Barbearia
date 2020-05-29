package br.com.dutra.barbearia.Telas.Sistema;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Corte.ListaDeCortesActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;

public class ServicosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void irParaCorte(View view) {

        Intent mudarTela = new Intent(getApplicationContext(), ListaDeCortesActivity.class);
        startActivity(mudarTela);

    }

    public void irParaProdutos(View view) {

//        Intent mudarTela = new Intent(getApplicationContext(), ListaDeProdutosActivity.class);
//        startActivity(mudarTela);

        AlertaUtils.dialogSimples("Essa funcionalidade ainda não está disponivel!",this);

    }
}
