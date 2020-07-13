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

import androidx.fragment.app.DialogFragment;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Sistema.DashBoardActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.MyDialog;
import br.com.dutra.barbearia.Utilidades.Utilitario;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private Usuario usuario = new Usuario();
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private ImageView imgFoto;
    private EditText edtNome;
    private EditText edtCelular;
    private EditText edtDataDeNascimento;
    private EditText edtUserLogin;
    private EditText edtSenha;
    private EditText edtCpfOuCnpj;
    private Uri uriDaImagemSelecionada = null;
    private Activity act = this;
    private Button btnSalvar;
    MenuItem btnAtualizar;

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
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {

            uriDaImagemSelecionada  = data.getData();
            imgFoto.setImageURI(uriDaImagemSelecionada);

        }
    }

    public void associacaoDosComponentes(){

        imgFoto = (ImageView)findViewById(R.id.imgFoto);
        edtNome = (EditText)findViewById(R.id.edtNome);
        edtCelular = (EditText)findViewById(R.id.edtCelular);
        edtDataDeNascimento = (EditText)findViewById(R.id.edtDataDeNascimento);
        edtUserLogin = (EditText)findViewById(R.id.edtUserlogin);
        edtSenha = (EditText)findViewById(R.id.edtSenha);
        edtCpfOuCnpj = (EditText)findViewById(R.id.edtCpfOuCnpj);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

    }

    public void mostrarDialog(View v){

    }

    public void doPositiveClick(){

    }

    public void eventoClick(){

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
                    cadastraUsuarioNoServidor();
                }
            }
        });

    }

    public boolean validacaoCadastro(){

        String nome = edtNome.getText().toString();
        String celular = edtCelular.getText().toString();
        String DataNascimento = edtDataDeNascimento.getText().toString();
        String UserLogin = edtUserLogin.getText().toString();
        String senha = edtSenha.getText().toString();

        if(uriDaImagemSelecionada == null){
            AlertaUtils.dialogSimples("Adicione uma foto para seu perfil ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
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

    public boolean validacaoAtualizacao(){

        String nome = edtNome.getText().toString();
        String celular = edtCelular.getText().toString();
        String DataNascimento = edtDataDeNascimento.getText().toString();
        String UserLogin = edtUserLogin.getText().toString();
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

    private void cadastraUsuarioNoServidor(){

        String UserLogin = edtUserLogin.getText().toString();
        String senha = edtSenha.getText().toString();

        AlertaUtils.dialogLoad(act);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(UserLogin, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            logarUsuario();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });
    }

    public void logarUsuario(){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtUserLogin.getText().toString(), edtSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            saveUserInFirebase();
                        } else {
                            AlertaUtils.getDialog().dismiss();
                            AlertaUtils.dialogSimples(task.getException().getMessage(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }, act);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });

    }

    private void saveUserInFirebase() {

        String uid = FirebaseAuth.getInstance().getUid();
        String nome = edtNome.getText().toString();
        String celular = edtCelular.getText().toString();
        String dataNascimento = edtDataDeNascimento.getText().toString();
        String userLogin = edtUserLogin.getText().toString();
        String senha = edtSenha.getText().toString();
        String cpfOuCnpj = edtCpfOuCnpj.getText().toString();

        if(!cpfOuCnpj.isEmpty()){
            AlertaUtils.dialogLoad(act);
        }

        final Usuario user = new Usuario(uid,nome,celular,dataNascimento,senha,userLogin);
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
                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });
    }

    public void enviarImagemDoUsuarioParaServidor(){

        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/usuarios/" + edtUserLogin.getText());
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
                        AlertaUtils.getDialog().dismiss();
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
                buscarDadosUsuario();
            }else {
                edtCpfOuCnpj.setText("");
                edtCpfOuCnpj.setVisibility(View.GONE);
            }
        }catch (Exception e){
            edtCpfOuCnpj.setText("");
            edtCpfOuCnpj.setVisibility(View.GONE);
        }
    }

    public void buscarDadosUsuario(){

        String uId = FirebaseAuth.getInstance().getUid();

        AlertaUtils.dialogLoad(act);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Usuario").document(uId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Usuario user = documentSnapshot.toObject(Usuario.class);
                setarDadosUsuario(user);
                AlertaUtils.getDialog().dismiss();

            }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });
    }

    public void setarDadosUsuario(Usuario usuario){

        btnAtualizar.setVisible(true);
        edtNome.setText(usuario.getNome());
        edtCelular.setText(usuario.getCelular());
        edtDataDeNascimento.setText(usuario.getDataDeNascimento());
        edtUserLogin.setText(usuario.getUserLogin());
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

    public void editaImagemDoUsuario() throws IOException {

        //diminue o tamanho da imagem
        Picasso.get()
                .load(uriDaImagemSelecionada)
                .resize(300,300)
                .centerCrop()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap newBitmap, Picasso.LoadedFrom from) {


                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

    }

    public void salvaImagemNoApp(Bitmap bitmap) throws IOException {

        //salva a imagem no app
        String FILENAME = edtNome.getText().toString() + ".png";
        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

        //convert imagem para byte
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

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

}
