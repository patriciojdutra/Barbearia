package br.com.dutra.barbearia.Telas.Sistema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.dutra.barbearia.R;
import com.google.firebase.auth.FirebaseAuth;

public class TelaInicial extends AppCompatActivity {

    private int tempoDeEspera = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String uid = FirebaseAuth.getInstance().getUid();

                if(uid!=null){
                    Intent tela2 = new Intent(getApplicationContext(), DashBoardActivity.class);
                    startActivity(tela2);
                    finish();
                }else {
                    Intent tela2 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(tela2);
                    finish();
                }
            }
        }, tempoDeEspera);
    }
}
