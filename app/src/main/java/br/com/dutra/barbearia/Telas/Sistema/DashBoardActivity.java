package br.com.dutra.barbearia.Telas.Sistema;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import br.com.dutra.barbearia.Configuracoes.ConfiguracaoApplication;
import br.com.dutra.barbearia.Modelo.Fila;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Agendamento.AgendaActivity;
import br.com.dutra.barbearia.Telas.Agendamento.MeusAgendamentosActivity;
import br.com.dutra.barbearia.Telas.BatePapo.ListaDeConversasActivity;
import br.com.dutra.barbearia.Telas.Carrinho.CarrinhoActivity;
import br.com.dutra.barbearia.Telas.Local.MapsActivity;
import br.com.dutra.barbearia.Telas.Usuario.CadastroUsuarioActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.Utilitario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private  AlertDialog alerta;

    private static final int IMAGE_GALLERY_REQUEST = 1;
    Usuario usuarioLogado = new Usuario();

    private TextView txtNomeNoMenu;
    private CircleImageView imgDoUsuarioNoMenu;
    private ImageView imgClientesAguardando;
    private ImageView imgSinc;
    private ProgressBar progressBar;
    private TextView txtQuantidadeEspera;
    private TextView txtTempoEspera;

    private Spinner spQuantidade;
    private Spinner spTempo;
    private int quantidade;
    private String tempo;

    Activity act = this;
    List<DocumentSnapshot> docs;
    List<String> listTempo = new ArrayList<>();
    List<String>  listQuantidade = new ArrayList<>();

    MenuItem iconeGerenciamento;
    MenuItem iconeUpdate;
    MenuItem iconeCarrinho;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtNomeNoMenu = (TextView) headerView.findViewById(R.id.txtNomeNoMenu);
        imgDoUsuarioNoMenu = (CircleImageView)headerView.findViewById(R.id.imgFoto);
        imgClientesAguardando = (ImageView) findViewById(R.id.imgClientesAguardando);
        imgSinc = (ImageView) findViewById(R.id.imgSinc);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtTempoEspera = findViewById(R.id.txtTempoEspera);
        txtQuantidadeEspera = findViewById(R.id.txtQuantidadeEspera);

        if( Utilitario.verifiacrAutencicao(act)){

            //PagamentoController pagamentoController = new PagamentoController(this);
            //pagamentoController.cadastrarClienteNoWireCard();

            ConfiguracaoApplication application = (ConfiguracaoApplication) getApplication();
            getApplication().registerActivityLifecycleCallbacks(application);
            Utilitario.updateToken();

            permisaoAcesGaleria();
            inicializaLista();
            buscarDadosUsuario();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        progressBar.setVisibility(View.GONE);
        imgSinc.setImageResource(R.drawable.sincnot);
        getFilaDeEspera();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        setarTokenUsuario(token);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gerenciamento, menu);
        iconeGerenciamento = menu.findItem(R.id.action_gerenciar);
        iconeUpdate = menu.findItem(R.id.action_update);
        iconeCarrinho = menu.findItem(R.id.action_carrinho);
        iconeGerenciamento.setVisible(false);
        iconeUpdate.setVisible(false);
        iconeCarrinho.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            dialogAtualizarFila();
            return true;
        }

        if(id == R.id.action_gerenciar){
            Intent it = new Intent(this, GerenciamentoActivity.class);
            startActivity(it);
        }

        if(id == R.id.action_carrinho){

            Intent it = new Intent(this, CarrinhoActivity.class);
            startActivity(it);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.nav_meus_atendimentos) {
            Intent mudarTela = new Intent(getApplicationContext(), MeusAgendamentosActivity.class);
            startActivity(mudarTela);
        }

        if (id == R.id.nav_agendamento) {
            Intent mudarTela = new Intent(getApplicationContext(),AgendaActivity.class);
            startActivity(mudarTela);
        }

//        if (id == R.id.nav_carrinho) {
//            Intent mudarTela = new Intent(getApplicationContext(),CarrinhoActivity.class);
//            startActivity(mudarTela);
//        }

        if (id == R.id.nav_servicos) {
            Intent mudartela = new Intent(getApplicationContext(), ServicosActivity.class);
            startActivity(mudartela);
        }

        if (id == R.id.nav_barbeiros) {
            Intent it = new Intent(this, ListaDeConversasActivity.class);
            it.putExtra("solicitacao","Barbeiro");
            startActivity(it);
        }

        if (id == R.id.nav_localizacao) {
            Intent mudartela = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(mudartela);
        }

        if (id == R.id.nav_meu_perfil) {
            Intent mudartela = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
            mudartela.putExtra("solicitacao","finalizarCadastro");
            startActivity(mudartela);
        }

//        if (id == R.id.nav_reclame_aqui) {
//            Intent mudarTela = new Intent(getApplicationContext(), ReclameAquiActivity.class);
//            startActivity(mudarTela);
//        }
//
//        if (id == R.id.nav_configuracao) {
//            Intent mudartela = new Intent(getApplicationContext(), ConfiguracaoActivity.class);
//            startActivity(mudartela);
//        }

        if (id == R.id.nav_sair) {
            AlertaUtils.dialogConfirmacaoLogout(act);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setarTokenUsuario(final String token){

        String uId = FirebaseAuth.getInstance().getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Usuario").document(uId);
        docRef.update("token", token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //Toast.makeText(getApplicationContext(), "Token atualizado\n"+token, Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erro ao buscar token", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void buscarDadosUsuario(){

        String uId = FirebaseAuth.getInstance().getUid();

        if(uId == null)
            return;

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Usuario")
                .document(uId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                usuarioLogado = documentSnapshot.toObject(Usuario.class);

                if(usuarioLogado != null){

                    txtNomeNoMenu.setText(usuarioLogado.getNome() +" "+ usuarioLogado.getSobrenome());
                    buscarFotoUsuario();

                    if(usuarioLogado.getTipoUsuario().equalsIgnoreCase("C")){
                        iconeGerenciamento.setVisible(false);
                        iconeUpdate.setVisible(false);
                        iconeCarrinho.setVisible(false);
                    }else {
                        iconeCarrinho.setVisible(false);
                        iconeGerenciamento.setVisible(true);
                        iconeUpdate.setVisible(true);
                    }
                }else {
                    FirebaseAuth.getInstance().signOut();
                    Utilitario.verifiacrAutencicao(act);
                }


            }
        });
    }

    public void buscarFotoUsuario(){

        try {

            final File localFile = File.createTempFile("images", "jpeg");

            StorageReference pathReference = FirebaseStorage.getInstance().getReference();
            pathReference = pathReference.child("/images/usuarios/"+usuarioLogado.getUserLogin());

            pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Uri uri = Uri.fromFile(localFile);
                    imgDoUsuarioNoMenu.setImageURI(uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    String a = "";
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void inicializaLista(){

        listQuantidade.add("1"); listQuantidade.add("2"); listQuantidade.add("3"); listQuantidade.add("4"); listQuantidade.add("5"); listQuantidade.add("6");
        listTempo.add("10min"); listTempo.add("20min"); listTempo.add("30min"); listTempo.add("40min"); listTempo.add("50min");
        listTempo.add("1h"); listTempo.add("1h:15min"); listTempo.add("1h:30min"); listTempo.add("1h:45min"); listTempo.add("2h"); listTempo.add("Mais 2 horas"); listTempo.add("Fechar");
    }

    public void permisaoAcesGaleria() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    IMAGE_GALLERY_REQUEST);
        }
    }

    public void irParaPerfil(View v){

        Intent mudarTela = new Intent(getApplicationContext(), ListaDeConversasActivity.class);
        startActivity(mudarTela);

    }

    public void irParaAgenda(View v){

        Intent mudarTela = new Intent(getApplicationContext(), AgendaActivity.class);
        startActivity(mudarTela);

    }

    public void irParaLocalizacao(View v){
        Intent mudarTela = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(mudarTela);
    }

    public void irParaServicos(View v){
        Intent mudarTela = new Intent(getApplicationContext(),ServicosActivity.class);
        startActivity(mudarTela);
    }

    public void getFilaDeEspera(){

            FirebaseFirestore.getInstance().collection("Fila")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                return;
                            }

                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                            if (!docs.isEmpty()) {

                                for (DocumentSnapshot doc : docs) {

                                    Fila fila = doc.toObject(Fila.class);

                                    txtQuantidadeEspera.setText("" + fila.getClientesEsperando());
                                    txtTempoEspera.setText("" + fila.getTempoDeEspera());

                                    switch (fila.getClientesEsperando()) {
                                        case 1:

                                            imgClientesAguardando.setImageResource(R.drawable.espera1);
                                            imgClientesAguardando.getLayoutParams().width = 150;
                                            imgClientesAguardando.getLayoutParams().height = 150;

                                            break;
                                        case 2:

                                            imgClientesAguardando.setImageResource(R.drawable.espera2);
                                            imgClientesAguardando.getLayoutParams().width = 150;
                                            imgClientesAguardando.getLayoutParams().height = 150;

                                            break;

                                        case 3:

                                            imgClientesAguardando.setImageResource(R.drawable.espera3);
                                            imgClientesAguardando.getLayoutParams().width = 250;
                                            imgClientesAguardando.getLayoutParams().height = 150;

                                            break;

                                        case 4:
                                            imgClientesAguardando.setImageResource(R.drawable.espera4);
                                            imgClientesAguardando.getLayoutParams().width = 250;
                                            imgClientesAguardando.getLayoutParams().height = 150;

                                            break;

                                        case 5:
                                            imgClientesAguardando.setImageResource(R.drawable.espera5);
                                            imgClientesAguardando.getLayoutParams().width = 300;
                                            imgClientesAguardando.getLayoutParams().height = 150;

                                            break;

                                        case 6:
                                            imgClientesAguardando.setImageResource(R.drawable.espera6);
                                            imgClientesAguardando.getLayoutParams().width = 300;
                                            imgClientesAguardando.getLayoutParams().height = 150;

                                            break;
                                    }

                                    imgSinc.setImageResource(R.drawable.sincok);
                                }
                            }

                        }
                    });


            //imgSinc.setImageResource(R.drawable.sincnot);
            //imgClientesAguardando.setImageResource(R.drawable.cortando);
            //txtQuantidadeEspera.setText("");
            //txtTempoEspera.setText("");

    }

    public void atualizarFilaDeEspera(View v){

        progressBar.setVisibility(View.VISIBLE);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFilaDeEspera();
                progressBar.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void dialogAtualizarFila() {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_atualizar_fila,null);
        view.setBackgroundColor(Color.parseColor("#615232"));

        spQuantidade = view.findViewById(R.id.spQuantidade);
        SpinnerAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listQuantidade);
        ((ArrayAdapter) adapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQuantidade.setAdapter(adapter);

        spTempo = view.findViewById(R.id.spTempo);
        SpinnerAdapter adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listTempo);
        ((ArrayAdapter) adapter1).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTempo.setAdapter(adapter1);

        Button btnSalvar = view.findViewById(R.id.btnLogar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    String quant = (String) spQuantidade.getSelectedItem();
                    tempo = (String) spTempo.getSelectedItem();

                    if(tempo.equalsIgnoreCase("Fechar")) {
                        quantidade = 0;
                    }else {
                        quantidade = Integer.parseInt(quant);
                    }

                }catch (Exception e){

                    AlertaUtils.dialogSimples("Ocorreu um erro de conversão",act);
                    alerta.dismiss();
                    return;
                }

                    FirebaseFirestore.getInstance().collection("Fila").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                    if (e != null) {

                                        AlertaUtils.dialogSimples("Ocorreu um erro de sincronização",act);
                                        alerta.dismiss();
                                        return;
                                    }

                                    docs = queryDocumentSnapshots.getDocuments();
                                    atualizar();

                                }
                            });


            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo250);
        builder.setView(view);

        alerta = builder.create();
        alerta.show();

    }

    public void atualizar(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference noteRef = db.collection("Fila")
                .document(docs.get(0).getId());

        if(quantidade > 0){

            noteRef.update("clientesEsperando",quantidade,"tempoDeEspera",tempo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                            }

                            alerta.dismiss();
                        }
                    });

        }else {

            noteRef.update("clientesEsperando",0,"tempoDeEspera","Fechado")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                            }

                            alerta.dismiss();
                        }
                    });

        }

    }

}

