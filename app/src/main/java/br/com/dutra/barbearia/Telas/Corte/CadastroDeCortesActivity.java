package br.com.dutra.barbearia.Telas.Corte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.dutra.barbearia.Modelo.Cortes;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Sistema.DashBoardActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CadastroDeCortesActivity extends AppCompatActivity {

    Button btnSalvar;

    private EditText edtNomeDoCorte;
    private EditText edtPrecoDoCorte;
    private EditText edtDuracaoDoCorte;
    private EditText edtDescricao;

    private ImageView imgFoto1;
    Uri uriImg1 = null;
    private ImageView imgFoto2;
    Uri uriImg2 = null;
    private ImageView imgFoto3;
    Uri uriImg3 = null;
    private ImageView imgFoto4;
    Uri uriImg4 = null;

    int numerosDefotosSalvasNoServidor = 0;
    int numeroDeFotosSelecionadas = 0;
    private Cortes corte = new Cortes();
    private Dialog dialog;
    int cont = 0;

    Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_cortes);

        associacaoDeComponentes();
        eventoClick();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && null != data){

            if (requestCode == 1){

                uriImg1 = data.getData();
                imgFoto1.setImageURI(uriImg1);

            }else if (requestCode == 2){

                uriImg2 = data.getData();
                imgFoto2.setImageURI(uriImg2);


            }else if (requestCode == 3){

                uriImg3 = data.getData();
                imgFoto3.setImageURI(uriImg3);

            }else if (requestCode == 4){

                uriImg4 = data.getData();
                imgFoto4.setImageURI(uriImg4);

            }
        }
    }

    public void associacaoDeComponentes(){

        btnSalvar = findViewById(R.id.btnLogar);

        edtNomeDoCorte = findViewById(R.id.edtNomeDoCorte);
        edtPrecoDoCorte = findViewById(R.id.edtPrecoDoCorte);
        edtDuracaoDoCorte = findViewById(R.id.edtDuracaoDoCorte);

        imgFoto1 = (ImageView)findViewById(R.id.img1);
        imgFoto2 = (ImageView)findViewById(R.id.img2);
        imgFoto3 = (ImageView)findViewById(R.id.img3);
        imgFoto4 = (ImageView)findViewById(R.id.img4);

    }

    public void carregarFotoUsuario(int code) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {

            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, code);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    code);
        }
    }

    public void eventoClick(){

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarCadastro();
            }
        });

        imgFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarFotoUsuario(1);
            }
        });

        imgFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarFotoUsuario(2);
            }
        });

        imgFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarFotoUsuario(3);
            }
        });

        imgFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarFotoUsuario(4);
            }
        });
    }

    public void finalizarCadastro(){

        if(uriImg1 != null){
            numeroDeFotosSelecionadas++;
        }
        if(uriImg2 != null){
            numeroDeFotosSelecionadas++;
        }
        if(uriImg3 != null){
            numeroDeFotosSelecionadas++;
        }
        if(uriImg4 != null){
            numeroDeFotosSelecionadas++;
        }

        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_upload_file,null);
        final TextView textView = linearLayout.findViewById(R.id.texto);
        textView.setText(numerosDefotosSalvasNoServidor+"/"+numeroDeFotosSelecionadas);
        final ProgressBar progressBar = linearLayout.findViewById(R.id.progressBar4);
        progressBar.setMax(numeroDeFotosSelecionadas);
        progressBar.setProgress(0);
        dialogSalvarDados(linearLayout);

        for(int i = 1; i<=numeroDeFotosSelecionadas; i++){

            if(i == 1){
                enviarImagemParaServidor(uriImg1,edtNomeDoCorte.getText().toString(),i,linearLayout);
            }

            if(i == 2){
                enviarImagemParaServidor(uriImg2,edtNomeDoCorte.getText().toString(),i,linearLayout);
            }

            if(i == 3){
                enviarImagemParaServidor(uriImg3,edtNomeDoCorte.getText().toString(),i,linearLayout);
            }

            if(i == 4){
                enviarImagemParaServidor(uriImg4,edtNomeDoCorte.getText().toString(),i,linearLayout);
            }
        }
    }

    public void enviarImagemParaServidor(Uri data, final String nomeDoArquivo, final int id, final LinearLayout linearLayout){

        final TextView textView = linearLayout.findViewById(R.id.texto);
        final ProgressBar progressBar = linearLayout.findViewById(R.id.progressBar4);

        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/cortes/"+nomeDoArquivo+"/"+id);
        final UploadTask  uploadTask = ref.putFile(data);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //progressBar.setProgress((int)progress);
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                numerosDefotosSalvasNoServidor++;

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                numerosDefotosSalvasNoServidor++;
                textView.setText(numerosDefotosSalvasNoServidor+"/"+numeroDeFotosSelecionadas);
                progressBar.setProgress(numerosDefotosSalvasNoServidor);

                if(numerosDefotosSalvasNoServidor == numeroDeFotosSelecionadas) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            saveCorteNoFirebase();

                        }
                    }, 1000);
                }

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }



                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            }
        });
    }

    private void saveCorteNoFirebase() {

        String uid = edtNomeDoCorte.getText().toString();
        String nome = edtNomeDoCorte.getText().toString();
        String valor = edtPrecoDoCorte.getText().toString();
        String duracao = edtDuracaoDoCorte.getText().toString();

        corte.setNomeDoCorte(nome);
        corte.setValorDoCorte(valor);
        corte.setDuracaoDoCorte(duracao);

        FirebaseFirestore.getInstance().collection("Corte")
                .document(uid)
                .set(corte)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        dialog.dismiss();
                        dialogFinalizarCadastro();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialogSimples("Erro (P02)\nNão foi possivel cadastrar corte");
                    }
                });
    }

    public void dialogSalvarDados(LinearLayout linearLayout) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Salvando...");
        alert.setCancelable(false);
        alert.setView(linearLayout);
        dialog = alert.show();
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

    public void dialogFinalizarCadastro() {

        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Aviso");
        //define a mensagem
        builder.setMessage("Cadastro salvo com sucesso");
        //define um botão como positivo
        builder.setPositiveButton("Add novo corte", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

                Intent it = new Intent(getApplicationContext(),CadastroDeCortesActivity.class);
                startActivity(it);
                finish();

            }
        });

        builder.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

                Intent it = new Intent(getApplicationContext(), DashBoardActivity.class);
                startActivity(it);
                finish();

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        alerta.show();
    }

}
