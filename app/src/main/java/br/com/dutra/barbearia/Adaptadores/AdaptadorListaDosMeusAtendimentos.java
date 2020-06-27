package br.com.dutra.barbearia.Adaptadores;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import br.com.dutra.barbearia.Modelo.Atendimento;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;

import java.util.List;

public class AdaptadorListaDosMeusAtendimentos extends BaseAdapter {

    private List<Atendimento> itemList;
    private final Activity act;

    public AdaptadorListaDosMeusAtendimentos(List<Atendimento> itens, Activity act) {
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
                .inflate(R.layout.adaptador_lista_meus_atendimentos, parent, false);

        final Atendimento item = itemList.get(position);

        TextView txtDataEHora = (TextView) view.findViewById(R.id.txtDataEHora);
        TextView txtNomeBarbeiro = (TextView) view.findViewById(R.id.txtNomeBarbeiro);

        TextView txtNomeDoCorte = (TextView) view.findViewById(R.id.txtNomeDoCorte);
        TextView txtValor = (TextView) view.findViewById(R.id.txtValor);

        TextView txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        Button btnCancelarAtendimento = (Button) view.findViewById(R.id.btnCancelarAtendimento);

        txtDataEHora.setText(item.getData()+" - "+ item.getHorario());
        txtNomeBarbeiro.setText(item.getNomeBarbeiro());
        txtNomeDoCorte.setText(item.getTipoDeCorte());
        txtValor.setText(item.getValor());
        txtStatus.setText(item.getStatus());

        btnCancelarAtendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertaUtils.dialogSimples("Cancelando atendimento com " + item.getNomeBarbeiro(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, act);
            }
        });

        return view;
    }

}
