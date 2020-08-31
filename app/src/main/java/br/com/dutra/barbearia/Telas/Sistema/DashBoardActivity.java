package br.com.dutra.barbearia.Telas.Sistema;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import br.com.dutra.barbearia.Configuracoes.ConfiguracaoApplication;
import br.com.dutra.barbearia.Controllers.MudarTelaController;
import br.com.dutra.barbearia.Modelo.Banner;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Agendamento.AgendaActivity;
import br.com.dutra.barbearia.Telas.Agendamento.MeusAgendamentosActivity;
import br.com.dutra.barbearia.Telas.BatePapo.ListaDeConversasActivity;
import br.com.dutra.barbearia.Telas.Carrinho.CarrinhoActivity;
import br.com.dutra.barbearia.Telas.Local.MapsActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.Utilitario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private  AlertDialog alerta;

    private static final int IMAGE_GALLERY_REQUEST = 1;
    Usuario usuarioLogado = new Usuario();

    private TextView txtNomeNoMenu;
    private CircleImageView imgDoUsuarioNoMenu;
    Activity act = this;

    LinearLayout linearScroll;

    MenuItem iconeGerenciamento;
    MenuItem iconeUpdate;
    MenuItem iconeCarrinho;

    private List<Banner> listaDeBanner = new ArrayList<>();


    SliderLayout sliderLayout;


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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.action_loja) {
                    MudarTelaController.irParaLoja(false, act);
                }else if(item.getItemId() == R.id.action_servicos) {
                    MudarTelaController.irParaServicos(false, act);
                }else if(item.getItemId() == R.id.action_agendamento) {
                    MudarTelaController.irParaAgendamento(false, act);
                }else if(item.getItemId() == R.id.action_enderecos) {
                    MudarTelaController.irParaEnderecos(false, act);
                }else if(item.getItemId() == R.id.action_contatos) {
                    MudarTelaController.irParaContatos(false, act);
                }

                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        txtNomeNoMenu = (TextView) headerView.findViewById(R.id.txtNomeNoMenu);
        imgDoUsuarioNoMenu = (CircleImageView)headerView.findViewById(R.id.imgFoto);

        linearScroll = findViewById(R.id.linearScroll);

        if( Utilitario.verifiacrAutencicao(act)){

            ConfiguracaoApplication application = (ConfiguracaoApplication) getApplication();
            getApplication().registerActivityLifecycleCallbacks(application);
            Utilitario.updateToken();

            permisaoAcesGaleria();
            buscarDadosUsuario();
        }

        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.WORM); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(5); //set scroll delay in seconds :

        baixarDadosDoBanner();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if( Utilitario.verifiacrAutencicao(act)) {
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

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            MudarTelaController.irParaAtividade(false,this);
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

            MudarTelaController.irParaTelaDeLogin(false,act);
//            Intent mudartela = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
//            mudartela.putExtra("solicitacao","finalizarCadastro");
//            startActivity(mudartela);
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

    public void baixarDadosDoBanner(){

        AlertaUtils.dialogLoad(act);

        FirebaseFirestore.getInstance().collection("Banner").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {
                                Banner banner = doc.toObject(Banner.class);
                                listaDeBanner.add(banner);
                            }
                        }

                        AlertaUtils.getDialog().dismiss();

                        setarBanner();
                    }
                });

    }

    private void setarBanner() {

        for (int i = 0; i < listaDeBanner.size() ; i++) {

            LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.adaptador_card_style_1,null);
            linearScroll.addView(linearLayout);

            LinearLayout linearLayout2 = (LinearLayout) getLayoutInflater().inflate(R.layout.adaptador_card_style_2,null);
            linearScroll.addView(linearLayout2);


            SliderView sliderView = new SliderView(this);
            sliderView.setDescription(listaDeBanner.get(i).getDescricao());
            sliderView.setImageUrl(listaDeBanner.get(i).getUrl());
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_INSIDE);

            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(act, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);

        }
    }
}

