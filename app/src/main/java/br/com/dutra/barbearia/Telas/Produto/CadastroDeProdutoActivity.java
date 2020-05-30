package br.com.dutra.barbearia.Telas.Produto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import br.com.dutra.barbearia.Modelo.Produto;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.FileDescriptor;
import java.io.IOException;

public class CadastroDeProdutoActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ImageView imgProduto;
    EditText editTextNome;
    EditText editTextPreco;
    EditText editTextTipo;
    EditText editTextQuantidade;
    Button btnSalvar;
    private EditText edtDescricao;
    Uri uri;

    private Activity act;
    private static AlertDialog.Builder alert;
    private static Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_produto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        associacao();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {

            if (data != null) {

                uri = data.getData();
                imgProduto.setImageURI(uri);
            }
        }
    }

    public void buscarImagem(View v) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 42);
    }

    private Bitmap retornaImagem(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void associacao(){

         imgProduto = findViewById(R.id.imgDoCorte);
         editTextNome = findViewById(R.id.editTextNome);
         editTextPreco = findViewById(R.id.editTextPreco);
         editTextTipo = findViewById(R.id.editTextTipo);
         editTextQuantidade = findViewById(R.id.editTextQuantidade);
         edtDescricao = findViewById(R.id.edtDescricao);
         btnSalvar = findViewById(R.id.btnLogar);

    }

    public Produto objNovoProduto(){

        String sPreco = editTextPreco.getText().toString().replace(",",".");

        String nome = editTextNome.getText().toString();
        double preco =Double.parseDouble(sPreco);
        String tipo = editTextTipo.getText().toString();
        int quantidade = Integer.parseInt(editTextQuantidade.getText().toString());
        String descricao = edtDescricao.getText().toString();

        Produto produto = new Produto(nome,preco,tipo,quantidade,descricao);

        return produto;

    }

    public void salvarImagemDoProdutoNoServidor(){

        dialogLoad();

        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/produtos/" + editTextNome.getText().toString());
        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                               dialog.dismiss();
                               salvarDadosDoProdutoNoServidor();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();
                        AlertaUtils.dialogSimples("Erro ao salvar produto",act);

                    }
                });

    }

    public void salvarDadosDoProdutoNoServidor(){

        dialogLoad();

        Produto produto = objNovoProduto();

        String uid = editTextNome.getText().toString();

        FirebaseFirestore.getInstance().collection("Produto")
                .document(uid)
                .set(produto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        dialog.dismiss();
                        dialogSimplesFinish("Produto salvo com sucesso");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();
                        dialogSimples("Erro ao salvar produto");

                    }
                });

    }

    public void salvar(View v){

        salvarImagemDoProdutoNoServidor();

    }

    public void dialogSimples(String txt) {

        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Aviso");
        //define a mensagem
        builder.setMessage(txt);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        alerta.show();
    }

    public void dialogSimplesFinish(String txt) {

        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Aviso");
        //define a mensagem
        builder.setMessage(txt);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

                finish();

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        alerta.show();
    }

    public void dialogLoad()
    {
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Aguarde um momento...");
        alert.setMessage("Carregando");
        alert.setCancelable(false);
        final ProgressBar input = new ProgressBar (this);
        alert.setView(input);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface aleDialog, int whichButton) {

            }
        });
        dialog = alert.show();
    }


}
