package br.com.dutra.barbearia.Telas.Pagamento;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.dutra.barbearia.Modelo.Usuario.Cartao;
import br.com.dutra.barbearia.Modelo.Usuario.Endereco;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Carrinho.CarrinhoActivity;
import br.com.dutra.barbearia.Telas.Usuario.CadastroUsuarioActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroDePagamentoActivity extends AppCompatActivity {

    private Activity act = this;
    private Usuario usuario;
    private Endereco endereco;

    private EditText editTextBloco1;
    private EditText editTextBloco2;
    private EditText editTextBloco3;
    private EditText editTextBloco4;
    private EditText editTextDataVencimento1;
    private EditText editTextDataVencimento2;
    private EditText editTextCVV;
    private EditText txtNomeNoCartao;
    private ImageView imgStatusEnderecoCadastrado;
    private ImageView imgStatusUsuarioCadastrado;

    private Button btnCadastroEndereco;
    private Button btnCadastroUsuario;

    private boolean enderecoCadastroOk = false;
    private boolean usuarioCadastroOk = false;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_pagamento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgStatusEnderecoCadastrado = findViewById(R.id.imgStatusEnderecoCadastrado);
        imgStatusUsuarioCadastrado = findViewById(R.id.imgStatusUsuarioCadastrado);

        btnCadastroEndereco = findViewById(R.id.btnCadastroEndereco);
        btnCadastroUsuario = findViewById(R.id.btnCadastroUsuario);


    }

    @Override
    protected void onStart() {
        super.onStart();

        buscarDadosUsuario();

        btnCadastroEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getLayoutInflater().inflate(R.layout.layout_cadastro_endereco, null);

                final EditText txtCep = view.findViewById(R.id.txtCep);
                final EditText txtRua = view.findViewById(R.id.txtRua);
                final EditText txtNumeroCasa = view.findViewById(R.id.txtNumeroCasa);
                final EditText txtBairro = view.findViewById(R.id.txtBairro);
                final EditText txtCidade = view.findViewById(R.id.txtCidade);
                final EditText txtEstado = view.findViewById(R.id.txtEstado);
                final EditText txtComplemento = view.findViewById(R.id.txtComplemento);
                Button btnSalvar = view.findViewById(R.id.btnSalvarEndereco);
                Button btnCancelar = view.findViewById(R.id.btnCancelarEndereco);

                if(endereco != null){
                    txtCep.setText(endereco.getCep());
                    txtRua.setText(endereco.getRua());
                    txtNumeroCasa.setText(endereco.getNumero());
                    txtBairro.setText(endereco.getBairro());
                    txtCidade.setText(endereco.getCidade());
                    txtEstado.setText(endereco.getEstado());
                    txtComplemento.setText(endereco.getComplemento());
                    btnSalvar.setText("Atualizar");
                }


                btnSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uid = FirebaseAuth.getInstance().getUid();
                        String cep = txtCep.getText().toString();
                        String rua = txtRua.getText().toString();
                        String numero = txtNumeroCasa.getText().toString();
                        String bairro = txtBairro.getText().toString();
                        String cidade = txtCidade.getText().toString();
                        String estado = txtEstado.getText().toString();
                        String complemento = txtComplemento.getText().toString();

                        Endereco endereco = new Endereco(uid,cidade,complemento,bairro,rua,numero,cep,estado);

                        if(validarEndereco()){
                            cadastrarEndereco(endereco);
                        }

                    }
                });

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(alerta!=null){
                            alerta.dismiss();
                        }
                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                builder.setTitle("Informe os dados do seu endereço!");
                builder.setIcon(R.drawable.logo250);
                builder.setView(view);
                alerta = builder.create();
                alerta.show();


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(usuarioCadastroOk && enderecoCadastroOk){
            AlertaUtils.dialogDadosDePagamentoOk(act);
        }else {
            Intent it = new Intent(act.getApplicationContext(), CarrinhoActivity.class);
            act.startActivity(it);
            act.finish();
        }
    }

    public void buscarDadosUsuario(){

        String uId = FirebaseAuth.getInstance().getUid();

        AlertaUtils.dialogLoad(act);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Usuario").document(uId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                usuario = documentSnapshot.toObject(Usuario.class);
                verificarCadastroDoUsuario(usuario);

                buscarEndereco();


            }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.getDialog().dismiss();
                        //AlertaUtils.dialogSimples(e.getMessage(),act);
                    }
                });
    }

    public void buscarEndereco(){

        String uId = FirebaseAuth.getInstance().getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Endereco").document(uId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                endereco = documentSnapshot.toObject(Endereco.class);
                verificarCadastroDoEndereco(endereco);
                AlertaUtils.getDialog().dismiss();
            }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.getDialog().dismiss();
                       // AlertaUtils.dialogSimples(e.getMessage(),act);
                    }
                });
    }

    public boolean validarCartao(){

        return true;
    }

    public boolean validarEndereco(){

        return true;
    }

    public void cadastrarEndereco(Endereco endereco){

        AlertaUtils.dialogLoad(act);
        FirebaseFirestore.getInstance().collection("Endereco")
                .document(endereco.getUidUsuario())
                .set(endereco)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AlertaUtils.getDialog().dismiss();
                        if(alerta!=null)
                            alerta.dismiss();

                        buscarDadosUsuario();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.getDialog().dismiss();
                        //AlertaUtils.dialogSimples(e.getMessage(),act);
                    }
                });
    }

    public void verificarCadastroDoEndereco(Endereco endereco){

        if(endereco!=null){
            imgStatusEnderecoCadastrado.setImageResource(R.drawable.baseline_access_time_24);
            btnCadastroEndereco.setText("Alterar dados?\nclique aqui");
            enderecoCadastroOk = true;
        }

        dadosOk();
    }

    public void verificarCadastroDoUsuario(Usuario usuario){

        if(usuario.getCpfouCnpj() !=null && (usuario.getCpfouCnpj().length() == 11 || usuario.getCpfouCnpj().length() == 14)){
            imgStatusUsuarioCadastrado.setImageResource(R.drawable.baseline_access_time_24);
            btnCadastroUsuario.setText("Alterar dados?\nclique aqui");
            usuarioCadastroOk = true;
        }

        dadosOk();
    }

    public void dadosOk(){

        if(usuarioCadastroOk && enderecoCadastroOk){
            AlertaUtils.dialogDadosDePagamentoOk(act);
        }
    }
}
