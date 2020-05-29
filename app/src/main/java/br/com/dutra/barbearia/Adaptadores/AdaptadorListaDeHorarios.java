package br.com.dutra.barbearia.Adaptadores;

import android.app.Activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.dutra.barbearia.R;

import java.util.List;

public class AdaptadorListaDeHorarios extends BaseAdapter {


    private List<String> itemList;
    private final Activity act;

    public AdaptadorListaDeHorarios(List<String> itens, Activity act) {
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
                .inflate(R.layout.adaptador_lista_atendimento, parent, false);

        String item = itemList.get(position);

        TextView horario = (TextView) view.findViewById(R.id.textHorario);
        horario.setText(item);

        return view;
    }


}
