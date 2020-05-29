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

import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Modelo.Cortes;

import br.com.dutra.barbearia.Utilidades.FileUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdaptadorListaDeCortes  extends BaseAdapter {

    private List<Cortes> itemList;
    private final Activity act;

    public AdaptadorListaDeCortes(List<Cortes> itens, Activity act) {
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
        View view = act.getLayoutInflater()
                .inflate(R.layout.adaptador_lista_cortes, parent, false);

        final Cortes item = itemList.get(position);

        final ImageView imageViewCorte = (ImageView) view.findViewById(R.id.imgDoCorte);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView txtNomeDoCorte = (TextView) view.findViewById(R.id.txtNomeDoProduto);
        TextView txtDuracaoDoCorte = (TextView) view.findViewById(R.id.txtDuracaoDoCorte);
        TextView txtPrecoDoCoerte = view.findViewById(R.id.txtPrecoDoCoerte);

        txtDuracaoDoCorte.setText(item.getDuracaoDoCorte());
        txtNomeDoCorte.setText(item.getNomeDoCorte());
        txtPrecoDoCoerte.setText(item.getValorDoCorte());

        String nomeDoArquivo = item.getNomeDoCorte()+1+".jpg";
        final File file = act.getApplicationContext().getFileStreamPath(nomeDoArquivo);

        if(file.exists()){

            progressBar.setVisibility(View.GONE);
            Uri uri = Uri.fromFile(file);
            imageViewCorte.setImageURI(uri);
            imageViewCorte.setVisibility(View.VISIBLE);

        }else {


            try {

                final File localFile = File.createTempFile("images", "jpg");

                StorageReference pathReference = FirebaseStorage.getInstance().getReference();
                pathReference = pathReference.child("/images/cortes/"+item.getNomeDoCorte()+"/1");

                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        progressBar.setVisibility(View.GONE);
                        imageViewCorte.setVisibility(View.VISIBLE);

                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageViewCorte.setImageBitmap(bitmap);


                        String nomeDoArquivo = item.getNomeDoCorte()+"1";

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
