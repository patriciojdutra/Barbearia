package br.com.dutra.barbearia.Telas.Agendamento;

import android.app.Activity;
import android.os.Bundle;

import br.com.dutra.barbearia.Adaptadores.AdaptadorListaDosMeusAtendimentos;
import br.com.dutra.barbearia.Modelo.Atendimento;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.dutra.barbearia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MeusAgendamentosActivity extends AppCompatActivity {

    private List<Atendimento> listaDeAtendimento = new ArrayList<>();
    private Activity act = this;
    private ListView listviewAtendimentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_agendamentos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listviewAtendimentos = findViewById(R.id.listViewmeusAtendimentos);
        listarAtendimento();
    }


    public void listarAtendimento(){

        String uId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("Atendimento").whereEqualTo("cliente",uId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {

                                Atendimento atend = doc.toObject(Atendimento.class);
                                listaDeAtendimento.add(atend);
                            }
                        }

                        carregarLista();
                    }
                });

    }

    public void carregarLista(){

        AdaptadorListaDosMeusAtendimentos adpter = new AdaptadorListaDosMeusAtendimentos(listaDeAtendimento,act);
        listviewAtendimentos.setAdapter(adpter);

        listviewAtendimentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Atendimento anteAtendimento = listaDeAtendimento.get(i);

            }
        });
    }


}
