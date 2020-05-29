package br.com.dutra.barbearia.Adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.Utilidades.FileUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.dutra.barbearia.R;

public class AdaptadorListaDeContato extends BaseAdapter {

    private List<Usuario> itemList;
    private final Activity act;

    public AdaptadorListaDeContato(List<Usuario> itens, Activity act) {
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
                .inflate(R.layout.adaptador_lista_contatos, parent, false);

        final Usuario item = itemList.get(position);

        final ImageView imageViewUsuario = (ImageView) view.findViewById(R.id.imgDoCorte);
        TextView txtNomeDoContato = (TextView) view.findViewById(R.id.txtNomeDoContato);
        TextView txtUltimaMensagem = (TextView) view.findViewById(R.id.txtUltimaMensage);

        txtNomeDoContato.setText(item.getNome());

        String nomeDoArquivo = item.getUserLogin()+".jpg";
        final File file = act.getApplicationContext().getFileStreamPath(nomeDoArquivo);

        if(file.exists()){

            Uri uri = Uri.fromFile(file);
            imageViewUsuario.setImageURI(uri);
            imageViewUsuario.setVisibility(View.VISIBLE);

        }else {

            try {

                final File localFile = File.createTempFile("images", "jpg");

                StorageReference pathReference = FirebaseStorage.getInstance().getReference();
                pathReference = pathReference.child("/images/usuarios/"+item.getUserLogin());

                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        Uri uri = Uri.fromFile(localFile);
                        imageViewUsuario.setImageURI(uri);
                        imageViewUsuario.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                        String nomeDoArquivo = item.getUserLogin();

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
