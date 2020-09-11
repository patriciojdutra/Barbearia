package br.com.dutra.barbearia.Telas.Corte;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import br.com.dutra.barbearia.Adaptadores.AdaptadorListaDeCortes;
import br.com.dutra.barbearia.Modelo.Cortes;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class
ListaDeCortesActivity extends AppCompatActivity {

    private ListView listView;
    private List<Cortes> listaDeCortes = new ArrayList<>();
    private AdaptadorListaDeCortes adpter;
    private String solicitacao = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cortes);

        listView = (ListView)findViewById(R.id.listViewCortes);

        try{
            solicitacao = getIntent().getStringExtra("solicitacao");
        }catch (Exception e){ }

        eventosDeClicks();
        baixardados();
    }

    public void carregarlista(){
        adpter = new AdaptadorListaDeCortes(listaDeCortes, this);
        listView.setAdapter(adpter);
    }

    public void baixardados(){

        AlertaUtils.dialogLoad(this);

        FirebaseFirestore.getInstance().collection("Corte").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {

                                Cortes cortes = doc.toObject(Cortes.class);

                                if(cortes != null) {

                                    listaDeCortes.add(cortes);
//                                    String nomeDoArquivo = cortes.getNomeDoCorte()+1+ ".png";
//                                    File file = act.getApplicationContext().getFileStreamPath(nomeDoArquivo);
//
//                                    if (!file.exists()) {
//                                        try {
//                                            baixarUrlDaImagem(cortes);
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
                                }
                            }
                        }

                        carregarlista();
                        AlertaUtils.fecharDialog();

                    }
                });

    }

    public void eventosDeClicks() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cortes corte = (Cortes) listView.getItemAtPosition(position);

                if(solicitacao != null && solicitacao.equals("nomeCorte")){

                    Intent mudarTela = new Intent();
                    mudarTela.putExtra("nomeCorte",corte);
                    setResult(RESULT_OK,mudarTela);
                    finish();

                }else {

                    Intent mudarTela = new Intent(getApplicationContext(), InfoCorteActivity.class);
                    mudarTela.putExtra("corte",corte);
                    startActivity(mudarTela);

                }
            }
        });
    }
}
