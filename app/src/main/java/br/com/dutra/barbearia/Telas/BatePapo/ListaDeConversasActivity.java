package br.com.dutra.barbearia.Telas.BatePapo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.dutra.barbearia.Adaptadores.AdaptadorListaDeContato;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaDeConversasActivity extends AppCompatActivity {

    private Usuario usuarioLogado = new Usuario();
    private List<Usuario> contatos = new ArrayList<>();
    private Activity act = this;
    private ListView listviewContatos;
    private String solicitacao = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_conversas);
        listviewContatos = findViewById(R.id.listviewContatos);

        verificarchamada();
    }

    public void verificarchamada(){
        try{

            solicitacao = getIntent().getStringExtra("solicitacao");

            if(solicitacao.equals("nomeBarbeiro") || solicitacao.equals("Barbeiro")){
                listarBarbeiro();
            }else {
                solicitacao = "";
                buscarDadosUsuario();
            }

        }catch (Exception e){
            solicitacao = "";
            buscarDadosUsuario();
        }
    }

    public void buscarDadosUsuario(){

        String uId = FirebaseAuth.getInstance().getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Usuario").document(uId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                usuarioLogado = documentSnapshot.toObject(Usuario.class);

                if(usuarioLogado.getTipoUsuario().equalsIgnoreCase("C")){
                    listarBarbeiro();
                }else if(usuarioLogado.getTipoUsuario().equalsIgnoreCase("B")) {
                    listarclientes();
                }else{
                    listarBarbeiro();
                    listarclientes();
                }
            }
        });
    }

    public void listarclientes(){

        FirebaseFirestore.getInstance().collection("Usuario").whereEqualTo("tipoUsuario","C").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {

                                Usuario user = doc.toObject(Usuario.class);
                                contatos.add(user);
                            }
                        }

                        carregarLista();
                    }
                });

    }

    public void listarBarbeiro(){

        FirebaseFirestore.getInstance().collection("Usuario").whereEqualTo("tipoUsuario","B").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {

                                Usuario user = doc.toObject(Usuario.class);
                                contatos.add(user);
                            }
                        }

                        carregarLista();
                    }
                });

    }

    public void carregarLista(){

        AdaptadorListaDeContato adpter = new AdaptadorListaDeContato(contatos,act);
        listviewContatos.setAdapter(adpter);

        listviewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Usuario usuario = contatos.get(i);

                if(solicitacao.equals("nomeBarbeiro")){

                    Intent it = new Intent();
                    it.putExtra("nomeBarbeiro",usuario);
                    setResult(RESULT_OK,it);
                    finish();

                }else {

                    Intent it = new Intent(act, ConversaActivity.class);
                    it.putExtra("Usuario",usuario);
                    startActivity(it);

                }
            }
        });
    }
    
}
