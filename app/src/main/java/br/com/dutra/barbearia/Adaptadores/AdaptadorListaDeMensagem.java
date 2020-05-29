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


import br.com.dutra.barbearia.Modelo.Mensagem;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
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

public class AdaptadorListaDeMensagem extends BaseAdapter {

    private List<Mensagem> itemList;
    private final Activity act;
    private Usuario userSelecionado;
    private String userLogin;

    public AdaptadorListaDeMensagem(List<Mensagem> itens, Usuario userSelecionado, String userLogin, Activity act) {
        this.itemList = itens;
        this.userSelecionado = userSelecionado;
        this.act = act;
        this.userLogin = userLogin;
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

        final Mensagem item = itemList.get(position);

        View view = !userSelecionado.getTipoUsuario().equalsIgnoreCase(item.getTipoEnvio())
        ? act.getLayoutInflater().inflate(R.layout.adaptador_lista_mensagens_recebidas, parent, false)
                : act.getLayoutInflater().inflate(R.layout.adaptador_lista_mensagens_enviada, parent, false);

        final ImageView imageViewUsuario = (ImageView) view.findViewById(R.id.imgDoUsuario);
        TextView txtMensagemDoUsuario = (TextView) view.findViewById(R.id.txtMensagemDoUsuario);

        txtMensagemDoUsuario.setText(item.getMsg());

        String nomeDaimagem = "";
        if(userSelecionado.getTipoUsuario().equalsIgnoreCase(item.getTipoEnvio())){
            nomeDaimagem = userSelecionado.getUserLogin();
        }else{
            nomeDaimagem = userLogin;
        }

        final String nomeDoArquivo = nomeDaimagem;
        final File file = act.getApplicationContext().getFileStreamPath(nomeDoArquivo+".jpg");

        if(file.exists()){

            Uri uri = Uri.fromFile(file);
            imageViewUsuario.setImageURI(uri);
            imageViewUsuario.setVisibility(View.VISIBLE);

        }else {

            try {

                final File localFile = File.createTempFile("images", "jpg");

                StorageReference pathReference = FirebaseStorage.getInstance().getReference();
                pathReference = pathReference.child("/images/usuarios/"+nomeDoArquivo);

                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        Uri uri = Uri.fromFile(localFile);
                        imageViewUsuario.setImageURI(uri);
                        imageViewUsuario.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());


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
