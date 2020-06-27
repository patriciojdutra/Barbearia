package br.com.dutra.barbearia.Telas.BatePapo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import br.com.dutra.barbearia.Adaptadores.AdaptadorListaDeMensagem;
import br.com.dutra.barbearia.Modelo.Conversa;
import br.com.dutra.barbearia.Modelo.Mensagem;
import br.com.dutra.barbearia.Modelo.Notificacao;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class ConversaActivity extends AppCompatActivity {

    FirebaseFirestore db;

    public ListView listViewconversa;
    private EditText editMensagem;
    private Button btnEnviarMensagem;
    private Usuario usuarioSelecionado;
    private Usuario usuarioLogado;
    List<Mensagem> listaDeMensagem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        db = FirebaseFirestore.getInstance();
        listViewconversa = findViewById(R.id.listViewconversa);
        editMensagem = findViewById(R.id.edit_chat);
        btnEnviarMensagem = findViewById(R.id.btn_chat);

        usuarioSelecionado = (Usuario) getIntent().getExtras().getSerializable("Usuario");

        if(usuarioSelecionado == null){

            AlertaUtils.dialogSimples("Usuario não encontrado!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, this);

        }else{
            buscarDadosUsuario();
            btnEnviarMensagem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enviarMensagem();
                }
            });
        }



    }

    public void enviarMensagem(){

        String idDaConversa = "";
        String tipoEnvio = "";

        Conversa conversa = new Conversa();

        if(usuarioSelecionado.getTipoUsuario().equalsIgnoreCase("G")){
            idDaConversa = usuarioSelecionado.getUiId()+FirebaseAuth.getInstance().getUid();
            tipoEnvio = "C";
            conversa.setIdGerente(usuarioSelecionado.getUiId());
            conversa.setIdCliente(FirebaseAuth.getInstance().getUid());
        }else {
            idDaConversa = FirebaseAuth.getInstance().getUid()+usuarioSelecionado.getUiId();
            tipoEnvio = "G";
            conversa.setIdGerente(FirebaseAuth.getInstance().getUid());
            conversa.setIdCliente(usuarioSelecionado.getUiId());
        }
        conversa.setId(idDaConversa);

        final String msg = editMensagem.getText().toString();
        long data = new Date().getTime();

        final Mensagem mensagem = new Mensagem(msg,data,tipoEnvio);

        conversa.getMensagens().add(mensagem);

            FirebaseFirestore.getInstance().collection("Conversa")
                    .document(idDaConversa)
                    .collection("mensagens")
                    .add(conversa.getMensagens().get(0))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            editMensagem.setText("");
                            enviarNotification(msg);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

    }

    public void enviarNotification(final String msg){

         Notificacao notification = new Notificacao();
         notification.setFromId(FirebaseAuth.getInstance().getUid());
         notification.setToId(usuarioSelecionado.getUiId());
         notification.setSetTimestamp(new Date().getTime());
         notification.setText(msg);
         notification.setFromName(usuarioLogado.getNome());

         FirebaseFirestore.getInstance().collection("notifications")
                 .document(usuarioSelecionado.getToken())
                 .set(notification);

    }

    public void carregarlista(){

        AdaptadorListaDeMensagem adpter = new AdaptadorListaDeMensagem(listaDeMensagem, usuarioSelecionado,usuarioLogado.getUserLogin() ,this);
        listViewconversa.setAdapter(adpter);
        listViewconversa.setSelection(listaDeMensagem.size());
    }

    public void buscarDadosUsuario(){

        String uId = FirebaseAuth.getInstance().getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Usuario").document(uId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuarioLogado = documentSnapshot.toObject(Usuario.class);

                buscarConversa();
            }
        });
    }

    public void buscarConversa(){

        String idDaConversa = "";
        String tipoEnvio = "";

        final Conversa conversa = new Conversa();

        if(usuarioSelecionado.getTipoUsuario().equalsIgnoreCase("G")){
            idDaConversa = usuarioSelecionado.getUiId()+FirebaseAuth.getInstance().getUid();
            tipoEnvio = "C";
            conversa.setIdGerente(usuarioSelecionado.getUiId());
            conversa.setIdCliente(FirebaseAuth.getInstance().getUid());
        }else {
            idDaConversa = FirebaseAuth.getInstance().getUid()+usuarioSelecionado.getUiId();
            tipoEnvio = "G";
            conversa.setIdGerente(FirebaseAuth.getInstance().getUid());
            conversa.setIdCliente(usuarioSelecionado.getUiId());
        }

        db.collection("Conversa")
                .document(idDaConversa)
                .collection("mensagens")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            return;
                        }

                        listaDeMensagem.clear();

                        for (QueryDocumentSnapshot doc : value) {

                            Mensagem msg = doc.toObject(Mensagem.class);
                            listaDeMensagem.add(msg);
                        }

                        carregarlista();

                    }
                });
    }


}
