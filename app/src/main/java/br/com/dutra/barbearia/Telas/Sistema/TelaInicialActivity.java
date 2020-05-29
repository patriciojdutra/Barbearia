package br.com.dutra.barbearia.Telas.Sistema;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.controller.MudarTelaController;

public class TelaInicialActivity extends AppCompatActivity {

    private Activity act = this;
    private int tempoDeEspera = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MudarTelaController.irParaDashboard(true,act);
            }
        }, tempoDeEspera);

    }
}
