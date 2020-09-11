package br.com.dutra.barbearia.Adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import br.com.dutra.barbearia.Modelo.Produto;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.FileUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdaptadorListaDeProdutos extends BaseAdapter {

    private List<Produto> itemList;
    private final Activity act;
    private String nomeDoArquivo;

    public AdaptadorListaDeProdutos(List<Produto> itens, Activity act) {
        this.itemList = itens;
        this.act = act;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.adaptador_lista_produtos, parent, false);

        final Produto item = itemList.get(position);

        TextView txtNome = (TextView) view.findViewById(R.id.txtDescricao);
        txtNome.setText(item.getNome());

        TextView txtPreco = (TextView) view.findViewById(R.id.txtPreco);
        String preco = String.valueOf(item.getPreco());
        preco = preco.replace(".",",");
        txtPreco.setText("R$ "+preco);

        TextView txtQuantidade = (TextView) view.findViewById(R.id.txtQuantiEstoque);
        txtQuantidade.setText("Estoque: "+item.getQuantidade());


        final ProgressBar progressBar = view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        final ImageView img = (ImageView) view.findViewById(R.id.imgDoCorte);
        img.setVisibility(View.INVISIBLE);


        nomeDoArquivo = item.getNome();
        File file = act.getApplicationContext().getFileStreamPath(nomeDoArquivo);

        if(file.exists()){
            Uri uri = Uri.fromFile(file);
            img.setImageURI(uri);
            img.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }else {

            try {

                final File localFile = File.createTempFile("images", "jpg");

                StorageReference pathReference = FirebaseStorage.getInstance().getReference();
                pathReference = pathReference.child("/images/produtos/"+item.getNome());

                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        progressBar.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);

                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        img.setImageBitmap(bitmap);



                        String nomeDoArquivo = item.getNome();

                        try {
                            FileUtils.SalvarImage(bitmap,nomeDoArquivo,act);
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

        return view;
    }

}
