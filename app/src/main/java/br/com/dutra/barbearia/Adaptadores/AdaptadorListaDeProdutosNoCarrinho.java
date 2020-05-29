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

import br.com.dutra.barbearia.Modelo.Carrinho;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Carrinho.CarrinhoActivity;
import br.com.dutra.barbearia.Utilidades.DinheiroUtils;
import br.com.dutra.barbearia.Utilidades.FileUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdaptadorListaDeProdutosNoCarrinho extends BaseAdapter {

    private List<Carrinho> itemList;
    private final Activity act;
    private String nomeDoArquivo;
    private CarrinhoActivity carrinhoActivity;

    public AdaptadorListaDeProdutosNoCarrinho(List<Carrinho> itens,CarrinhoActivity carrinhoActivity, Activity act) {
        this.itemList = itens;
        this.act = act;
        this.carrinhoActivity = carrinhoActivity;
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
        View view = act.getLayoutInflater().inflate(R.layout.adaptador_lista_carrinho, parent, false);

        final Carrinho item = itemList.get(position);

        TextView txtNome = (TextView) view.findViewById(R.id.txtNomeProdutoCarrinho);
        TextView txtPreco = (TextView) view.findViewById(R.id.txtPrecoUnitarioProdutoCarrinho);
        TextView txtQuantidade = (TextView) view.findViewById(R.id.txtQuantidadeProdutoCarrinho);
        TextView txtTotal = (TextView) view.findViewById(R.id.btnTotalProdutoCarrinho);
        final ImageView img = (ImageView) view.findViewById(R.id.imgProdutoCarrinho);
        final ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDeleteProdutoCarrinho);


        txtNome.setText(item.getNomeDoProduto());
        txtQuantidade.setText("Quantidade: "+item.getQuantidadeDeProduto());
        txtPreco.setText("Preço: "+DinheiroUtils.convertDouble(item.getPrecoDoProduto()));

        double dTotal = item.getQuantidadeDeProduto() * item.getPrecoDoProduto();
        txtTotal.setText("Subtotal: "+DinheiroUtils.convertDouble(dTotal));

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carrinhoActivity.dialogDeConfirmacaoDeRemocaoDeProduto(item);
            }
        });

        nomeDoArquivo = item.getNomeDoProduto();
        File file = act.getApplicationContext().getFileStreamPath(nomeDoArquivo);

        if(file.exists()){
            Uri uri = Uri.fromFile(file);
            img.setImageURI(uri);
            img.setVisibility(View.VISIBLE);
        }else {

            try {

                final File localFile = File.createTempFile("images", "jpg");

                StorageReference pathReference = FirebaseStorage.getInstance().getReference();
                pathReference = pathReference.child("/images/produtos/"+item.getNomeDoProduto());

                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        img.setVisibility(View.VISIBLE);

                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        img.setImageBitmap(bitmap);

                        String nomeDoArquivo = item.getNomeDoProduto();

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
