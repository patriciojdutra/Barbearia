package br.com.dutra.barbearia.Telas.Corte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.dutra.barbearia.Modelo.Cortes;
import br.com.dutra.barbearia.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InfoCorteActivity extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtDuracao;
    private TextView txtPreco;
    private TextView txtDescricao;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;

    private Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_corte);

        txtNome = (TextView)findViewById(R.id.txtNomeDoProduto);
        txtDuracao = (TextView)findViewById(R.id.txtDuracao);
        txtPreco = (TextView)findViewById(R.id.txtPreco);
        txtDescricao = (TextView)findViewById(R.id.txtDescricaoInfo);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagem(txtNome.getText()+"1.jpg");
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagem(txtNome.getText()+"2.jpg");
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagem(txtNome.getText()+"3.jpg");
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagem(txtNome.getText()+"4.jpg");
            }
        });


        verificarOrigemDaChamada();
    }

    public void verificarOrigemDaChamada() {

        try {

            Bundle extras = getIntent().getExtras();
            final Cortes corte = (Cortes) extras.get("corte");

            txtNome.setText(corte.getNomeDoCorte());
            txtDuracao.setText(corte.getDuracaoDoCorte());
            txtPreco.setText(corte.getValorDoCorte());
            //txtDescricao.setText(corte.getDescricao());

            for (int i = 1; i <= 4; i++) {

            String nomeDoArquivo = corte.getNomeDoCorte() + i + ".jpg";
            final File file = act.getApplicationContext().getFileStreamPath(nomeDoArquivo);

            if (file.exists()) {

                if(i==1){
                    Uri uri = Uri.fromFile(file);
                    img1.setImageURI(uri);
                }

                if(i==2){
                    Uri uri = Uri.fromFile(file);
                    img2.setImageURI(uri);
                    img2.setVisibility(View.VISIBLE);
                }

                if(i==3){
                    Uri uri = Uri.fromFile(file);
                    img3.setImageURI(uri);
                    img3.setVisibility(View.VISIBLE);
                }

                if(i==4){
                    Uri uri = Uri.fromFile(file);
                    img4.setImageURI(uri);
                    img4.setVisibility(View.VISIBLE);
                }

            } else {

                try {

                    final int finalI = i;
                    final File localFile = File.createTempFile("images", "jpg");
                    StorageReference pathReference = FirebaseStorage.getInstance().getReference();
                    pathReference = pathReference.child("/images/cortes/" + corte.getNomeDoCorte() + "/"+i);

                    pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Uri uri = Uri.fromFile(localFile);

                            if( finalI == 1){
                                img1.setImageURI(uri);
                            }

                            if( finalI == 2){
                                img2.setImageURI(uri);
                                img2.setVisibility(View.VISIBLE);
                            }

                            if( finalI == 3){
                                img3.setImageURI(uri);
                                img3.setVisibility(View.VISIBLE);
                            }

                            if( finalI == 4){
                                img4.setImageURI(uri);
                                img4.setVisibility(View.VISIBLE);
                            }

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                            try {
                                SaveImage(bitmap, corte.getNomeDoCorte() + finalI);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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
        }


        }catch (Exception e){

        }
    }

    public void SaveImage(Bitmap finalBitmap, String img) throws IOException {

        String FILENAME = img+".jpg";

        FileOutputStream fos = act.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

    }

    public void openImagem(String nomeDoArquivo){

        final Dialog dialog=new Dialog(this,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        View view = getLayoutInflater().inflate(R.layout.dialog_full_screen,null);
        ImageView imgZoom = view.findViewById(R.id.imgFoto);

        final File file = act.getApplicationContext().getFileStreamPath(nomeDoArquivo);
        if(file.exists()) {
            Uri uri = Uri.fromFile(file);
            imgZoom.setImageURI(uri);
        }

        ImageView imgFechar = view.findViewById(R.id.imgFechar);
        imgFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView imgConpartilhar = view.findViewById(R.id.imgConpartilhar);
        dialog.setContentView(view);
        dialog.show();

    }

}
