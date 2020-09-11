package br.com.dutra.barbearia.Telas.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import br.com.dutra.barbearia.Controllers.MudarTelaController;
import br.com.dutra.barbearia.Modelo.Produto;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Sistema.DashBoardActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.FirebaseUtils;
import br.com.dutra.barbearia.Utilidades.MyDialog;
import br.com.dutra.barbearia.Utilidades.Utilitario;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private Usuario usuario = new Usuario();
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private ImageView imgFoto;
    private EditText edtNome;
    private EditText edtCelular;
    private EditText edtDataDeNascimento;
    private EditText edtSenha;
    private EditText edtCpfOuCnpj;
    private Uri uriDaImagemSelecionada = null;
    private Activity act = this;
    private Button btnSalvar;
    MenuItem btnAtualizar;

    private String mVerificationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_usuario);

        associacaoDosComponentes();
        eventoClick();
        verificarSolicitacao();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastro, menu);
        btnAtualizar = menu.findItem(R.id.action_atualizar);
        btnAtualizar.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

            uriDaImagemSelecionada  = data.getData();
            imgFoto.setImageURI(uriDaImagemSelecionada);

        }
    }

    public void associacaoDosComponentes(){

        imgFoto = (ImageView)findViewById(R.id.imgFoto);
        edtNome = (EditText)findViewById(R.id.edtNome);
        edtCelular = (EditText)findViewById(R.id.edtCelular);
        edtDataDeNascimento = (EditText)findViewById(R.id.edtDataDeNascimento);
        edtSenha = (EditText)findViewById(R.id.edtSenha);
        edtCpfOuCnpj = (EditText)findViewById(R.id.edtCpfOuCnpj);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

    }

    public void eventoClick(){

        edtDataDeNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilitario.setDataTextView(edtDataDeNascimento, act);
            }
        });

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarFotoUsuario();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validacaoCadastro()) {

                    AlertaUtils.dialogSimples("Será enviando um SMS com o codigo de confirmação ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String celular = edtCelular.getText().toString();
                            verificarSeTelefoneJaExiste(celular);

                        }
                    }, act);
                }
            }
        });
    }

    public void verificarSeTelefoneJaExiste(String celular){

        AlertaUtils.dialogLoad(act);

        FirebaseFirestore.getInstance().collection("Usuario")
                .whereEqualTo("celular",celular)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (docs.isEmpty()) {
                            verificarTelefone(celular);
                        }else {
                            AlertaUtils.dialogSimples("Celular já cadastrado, acesse sua conta com seu número de telefone", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MudarTelaController.irParaDashboard(true,act);
                                }
                            }, act);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                AlertaUtils.fecharDialog();
                AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                }, act);

            }
        });
    }

    public boolean validacaoCadastro(){

        String nome = edtNome.getText().toString();
        String celular = edtCelular.getText().toString();
        String dataNascimento = edtDataDeNascimento.getText().toString();
        String senha = edtSenha.getText().toString();

        if(uriDaImagemSelecionada == null){
            AlertaUtils.dialogSimples("Adicione uma foto para seu perfil ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        if(nome.trim().isEmpty()){
            AlertaUtils.dialogSimples("Nome não pode ser vazio", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        if(celular.trim().isEmpty()){
            AlertaUtils.dialogSimples("Celular não pode ser vazio", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        if(senha.trim().isEmpty()){
            AlertaUtils.dialogSimples("Senha não pode ser vazio", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        if(dataNascimento.trim().isEmpty()){
            AlertaUtils.dialogSimples("Data de nascimento não pode ser vazio", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        return true;
    }

    public boolean validacaoAtualizacao(){

        String nome = edtNome.getText().toString();
        String celular = edtCelular.getText().toString();
        String DataNascimento = edtDataDeNascimento.getText().toString();
        String senha = edtSenha.getText().toString();
        String cpfOuCnpj = edtCpfOuCnpj.getText().toString();

        if(cpfOuCnpj.isEmpty()){
            AlertaUtils.dialogSimples("Informe seu CNPJ/CPF", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            edtCpfOuCnpj.setHintTextColor(Color.RED);
            return false;
        }

        if(cpfOuCnpj.length() != 11 && cpfOuCnpj.length() != 14){
            AlertaUtils.dialogSimples("O CNPJ/CPF informado é inválido!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            edtCpfOuCnpj.setTextColor(Color.RED);
            return false;
        }

        if(nome.length()<1){
            AlertaUtils.dialogSimples("Nome não pode ser vazio", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        return true;
    }

    private void saveUserInFirebase() {

        String uid = FirebaseAuth.getInstance().getUid();
        String nome = edtNome.getText().toString();
        String celular = edtCelular.getText().toString();
        String dataNascimento = edtDataDeNascimento.getText().toString();
        String senha = edtSenha.getText().toString();
        String cpfOuCnpj = edtCpfOuCnpj.getText().toString();

        if(!cpfOuCnpj.isEmpty()){
            AlertaUtils.dialogLoad(act);
        }

        final Usuario user = new Usuario(uid,nome,celular,dataNascimento,senha);
        user.setCpfouCnpj(cpfOuCnpj);
        usuario = user;

        FirebaseFirestore.getInstance().collection("Usuario")
                .document(uid)
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if(uriDaImagemSelecionada != null) {
                            enviarImagemDoUsuarioParaServidor();
                        }else {
                            finalizarCadastro(false);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.fecharDialog();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });
    }

    public void enviarImagemDoUsuarioParaServidor(){

        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/usuarios/" + edtCelular.getText().toString());
        ref.putFile(uriDaImagemSelecionada)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                finalizarCadastro(true);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.fecharDialog();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });

    }

    public void finalizarCadastro(boolean irParaDashboard){

        if(irParaDashboard){
            Intent it = new Intent(this, DashBoardActivity.class);
            startActivity(it);
            finish();
        }else {
            finish();
        }


    }

    public void verificarSolicitacao() {

        try {
            String solicitacao = getIntent().getExtras().getString("solicitacao", "");
            if (solicitacao.equalsIgnoreCase("finalizarCadastro")) {
                imgFoto.setVisibility(View.GONE);
                buscarDadosUsuario(FirebaseAuth.getInstance().getUid());
            }else {
                edtCpfOuCnpj.setText("");
                edtCpfOuCnpj.setVisibility(View.GONE);
            }
        }catch (Exception e){
            edtCpfOuCnpj.setText("");
            edtCpfOuCnpj.setVisibility(View.GONE);
        }
    }

    public void buscarDadosUsuario(String uId){

        AlertaUtils.dialogLoad(act);

       new FirebaseUtils<Usuario>().buscarObjetoNoFirebase("Usuario",uId, new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {

               Usuario user = documentSnapshot.toObject(Usuario.class);
               setarDadosUsuario(user);
               AlertaUtils.fecharDialog();

           }},
               new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               AlertaUtils.fecharDialog();
               AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               }, act);
           }
       } );
    }

    public void setarDadosUsuario(Usuario usuario){

        if(btnAtualizar != null){
            btnAtualizar.setVisible(true);
        }

        edtNome.setText(usuario.getNome());
        edtCelular.setText(usuario.getCelular());
        edtDataDeNascimento.setText(usuario.getDataDeNascimento());
        edtSenha.setText(usuario.getSenha());
        edtSenha.setEnabled(false);
        edtCpfOuCnpj.setText(usuario.getCpfouCnpj());

        if(usuario.getCpfouCnpj() == null || usuario.getCpfouCnpj().isEmpty()) {
            edtCpfOuCnpj.requestFocus();
            AlertaUtils.dialogSimples("Informe seu CNPJ/CPF para finalizar seu cadastro", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
        }

    }

    public void carregarFotoUsuario() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {

            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, IMAGE_GALLERY_REQUEST);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    IMAGE_GALLERY_REQUEST);
        }
    }

    private void verificarTelefone(String celularInformado) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+55" + celularInformado,
                20,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

    }

    // verifica o codigo automatico
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verificarSMSRecebido(code);
            }else{

                if(phoneAuthCredential != null) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }else {
                    verificarCodigoManual();
                }
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(act, e.getMessage(), Toast.LENGTH_LONG).show();

            AlertaUtils.fecharDialog();

            AlertaUtils.dialogSimples("Não foi possivel enviar o código, aguarde 2 minutos e tente novamente\n"+e.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            },act);

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;

            verificarCodigoManual();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);

            AlertaUtils.dialogSimples(s, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            },act);

        }
    };

    private void verificarCodigoManual(){

        AlertaUtils.fecharDialog();

        View v = (View) getLayoutInflater().inflate(R.layout.modal_verificacao_code_manual,null);
        EditText editText = v.findViewById(R.id.edtCodigo);

        AlertaUtils.dialogCustomizado(act, v, "Entrar", "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface positivo, int i) {

                String codigo = editText.getText().toString();
                verificarSMSRecebido(codigo);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface negativo, int i) {

            }
        });


    }

    private void verificarSMSRecebido(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //MudarTelaController.irParaDashboard(true,act);
                            saveUserInFirebase();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            AlertaUtils.dialogSimples(message, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            },act);
                        }
                    }
                });
    }

}
