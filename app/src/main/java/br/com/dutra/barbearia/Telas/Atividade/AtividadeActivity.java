package br.com.dutra.barbearia.Telas.Atividade;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import br.com.dutra.barbearia.Modelo.Atividade;
import br.com.dutra.barbearia.Modelo.Despesa;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.DinheiroUtils;
import br.com.dutra.barbearia.Utilidades.FirebaseUtils;
import br.com.dutra.barbearia.Utilidades.Utilitario;

public class AtividadeActivity extends AppCompatActivity {

    private LinearLayout linearAtividades;
    private LinearLayout linearDespesas;
    private LinearLayout linearRelatorio;
    private LinearLayout linearLayoutDespesas;
    private Button btnAtividade;
    private Button btnDespesa;
    private Button btnRelatorio;

    private ConstraintLayout linearLayoutData;
    private TextView textData;
    private TextView textDataInicio;
    private TextView textDataFinal;
    private TextView txtTotalFaturamento;
    private TextView txtColocacao;
    private TextView txtVenda;
    private TextView txtLucro;
    private TextView txtTotalDespesa;
    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtPreco;
    private EditText edtItem;
    private EditText edtObs;
    private CheckBox checkBoxEntrega;
    private Spinner spComoNosConheceu;
    private RadioButton rbServico;

    private ConstraintLayout linearLayoutDataDespesa;
    private TextView textDataDespesa;
    private EditText edtNomeDespesa;
    private EditText edtPrecoDespesa;
    private EditText edtObsDespesa;

    private Activity act = this;

    boolean isAtividade = true;
    boolean isDespesa = false;

    List<Atividade> listaDeAtividade = new ArrayList<>();
    List<Despesa> listaDeDespesas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearAtividades = findViewById(R.id.linearAtividades);
        linearDespesas = findViewById(R.id.linearDespesas);
        linearDespesas.setVisibility(View.GONE);
        linearRelatorio = findViewById(R.id.linearRelatorio);
        linearRelatorio.setVisibility(View.GONE);
        btnAtividade = findViewById(R.id.btnAtividade);
        btnDespesa = findViewById(R.id.btnDespesa);
        btnRelatorio = findViewById(R.id.btnRelatorio);

        linearLayoutData = findViewById(R.id.linearLayoutData);
        linearLayoutDespesas = findViewById(R.id.linearLayoutDespesas);
        textData = findViewById(R.id.textData);
        textData.setText(Utilitario.dataAtual());

        textDataInicio = findViewById(R.id.textDataInicio);
        textDataInicio.setText(Utilitario.getDataInicio());

        textDataFinal = findViewById(R.id.textDataFinal);
        textDataFinal.setText(Utilitario.getDataFinal(textDataInicio.getText().toString()));

        txtTotalFaturamento = findViewById(R.id.txtTotal);
        txtColocacao = findViewById(R.id.txtColocacao);
        txtVenda = findViewById(R.id.txtVenda);
        txtLucro = findViewById(R.id.txtLucro);
        txtTotalDespesa = findViewById(R.id.txtTotalDespesa);
        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtPreco = findViewById(R.id.edtPreco);
        edtItem = findViewById(R.id.edtItem);
        edtObs = findViewById(R.id.edtObs);
        checkBoxEntrega = findViewById(R.id.checkBoxEntrega);
        rbServico = findViewById(R.id.rbServico);
        spComoNosConheceu = findViewById(R.id.spComoNosconheceu);

        checkBoxEntrega.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkado) {
                if(checkado){
                    edtObs.setText("Tele-entrega\nValor da entrega:\nMotoboy:");
                }else {
                    edtObs.setText("");
                }
            }
        });

        linearLayoutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilitario.setDataTextView(textData,act);
            }
        });

        linearLayoutDataDespesa = findViewById(R.id.linearLayoutDataDespesa);
        textDataDespesa = findViewById(R.id.textDataDespesa);
        textDataDespesa.setText(Utilitario.dataAtual());
        edtNomeDespesa = findViewById(R.id.edtNomeDespesa);
        edtPrecoDespesa = findViewById(R.id.edtPrecoDespesa);
        edtObsDespesa = findViewById(R.id.edtObsDespesa);

        linearLayoutDataDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilitario.setDataTextView(textDataDespesa,act);
            }
        });

        String[] lista = {"Facebook","Instagram","Através de um conhecido", "Outros"};
        SpinnerAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lista);
        ((ArrayAdapter) adapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComoNosConheceu.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_finalizar_atividade, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_salvar_atividade) {

            if(isAtividade)
                salvarAtividade();
            else if(isDespesa)
                salvarDespesa();
            else
                carregarRelatorio();

        }

        return super.onOptionsItemSelected(item);
    }

    public void btnAtividade(View v){
        linearAtividades.setVisibility(View.VISIBLE);
        linearDespesas.setVisibility(View.GONE);
        linearRelatorio.setVisibility(View.GONE);
        btnAtividade.setTextSize(18);
        btnDespesa.setTextSize(12);
        btnRelatorio.setTextSize(12);
        isAtividade = true;
        isDespesa = false;


    }

    public void btnDespesa(View v){
        linearAtividades.setVisibility(View.GONE);
        linearDespesas.setVisibility(View.VISIBLE);
        linearRelatorio.setVisibility(View.GONE);
        btnAtividade.setTextSize(12);
        btnDespesa.setTextSize(18);
        btnRelatorio.setTextSize(12);
        isAtividade = false;
        isDespesa = true;
    }

    public void btnRelatorio(View v){

        linearAtividades.setVisibility(View.GONE);
        linearDespesas.setVisibility(View.GONE);
        linearRelatorio.setVisibility(View.VISIBLE);
        btnAtividade.setTextSize(12);
        btnDespesa.setTextSize(12);
        btnRelatorio.setTextSize(18);
        isAtividade = false;
        isDespesa = false;

    }

    private void salvarAtividade(){

        Atividade atividade = new Atividade();

        try{

            if(edtPreco.getText().toString().isEmpty()){
                AlertaUtils.dialogSimples("Preço está vazio", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, act);
                return;
            }

            double dPreco = DinheiroUtils.converteStrigEmDouble(edtPreco.getText().toString());
            atividade.setPreco(dPreco);

        }catch (Exception e){
            AlertaUtils.dialogSimples("preço inválido!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return;
        }

        String data = textData.getText().toString();
        String nome  = edtNome.getText().toString();
        String telefone = edtTelefone.getText().toString();
        String comoNosConheceu = (String)spComoNosConheceu.getSelectedItem();
        String tipoAtividade = rbServico.isChecked()?"Serviço":"Venda";
        String nomeItem = edtItem.getText().toString();
        String observacao = edtObs.getText().toString();

        atividade.setData(Utilitario.converteStringEmData(data));
        atividade.setNome(nome);
        atividade.setTelefone(telefone);
        atividade.setComoNosConheceu(comoNosConheceu);
        atividade.setTipoAtividade(tipoAtividade);
        atividade.setItem(nomeItem);
        atividade.setObservacao(observacao);

        AlertaUtils.dialogLoad(act);

        new FirebaseUtils<Atividade>().salvarNoFirebase("Atividade", atividade, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                AlertaUtils.fecharDialog();
                AlertaUtils.dialogSimples("Atividade salva com sucesso", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }, act);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertaUtils.fecharDialog();
                AlertaUtils.dialogSimples("Ocorreu um erro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                }, act);
            }
        });
    }

    private void salvarDespesa(){

        Despesa despesa = new Despesa();

        try{

            if(edtPrecoDespesa.getText().toString().isEmpty()){
                AlertaUtils.dialogSimples("Preço está vazio", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, act);
                return;
            }

            double dPreco = DinheiroUtils.converteStrigEmDouble(edtPrecoDespesa.getText().toString().trim());
            despesa.setPreco(dPreco);

        }catch (Exception e){
            AlertaUtils.dialogSimples("preço inválido!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return;
        }

        String data = textDataDespesa.getText().toString();
        String nome  = edtNomeDespesa.getText().toString();
        String observacao = edtObsDespesa.getText().toString();

        despesa.setData(Utilitario.converteStringEmData(data));
        despesa.setDespesa(nome);
        despesa.setObservacao(observacao);

        AlertaUtils.dialogLoad(act);

        new FirebaseUtils<Despesa>().salvarNoFirebase("Despesa", despesa, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                AlertaUtils.fecharDialog();
                AlertaUtils.dialogSimples("Despesa salva com sucesso", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }, act);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertaUtils.fecharDialog();
                AlertaUtils.dialogSimples("Ocorreu um erro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                }, act);
            }
        });
    }

    private void carregarRelatorio(){

        Date dataInicio = Utilitario.converteStringEmData(textDataInicio.getText().toString());
        Date dataFinal = Utilitario.converteStringEmData(textDataFinal.getText().toString());

        AlertaUtils.dialogLoad(act);

        FirebaseFirestore.getInstance().collection("Atividade")
                .whereGreaterThanOrEqualTo("data", dataInicio)
                .whereLessThan("data", dataFinal)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {
                                Atividade atividade = doc.toObject(Atividade.class);
                                listaDeAtividade.add(atividade);
                            }
                        }

                        getDespesas();

                    }
                });
    }

    private void getDespesas(){

        Date dataInicio = Utilitario.converteStringEmData(textDataInicio.getText().toString());
        Date dataFinal = Utilitario.converteStringEmData(textDataFinal.getText().toString());

        FirebaseFirestore.getInstance().collection("Despesa")
                .whereGreaterThanOrEqualTo("data", dataInicio)
                .whereLessThan("data", dataFinal)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        AlertaUtils.fecharDialog();

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {
                                Despesa despesa = doc.toObject(Despesa.class);
                                listaDeDespesas.add(despesa);
                            }

                            carregarFaturamento();

                        }
                    }
                });

    }

    public void carregarFaturamento(){

        double totalFaturamento = 0;
        double totalDespesas = 0;
        int quantVendas = 0;
        double valorVenda = 0;
        int quantColocao = 0;
        double valorColocacao = 0;
        double lucro = 0;

        for (Atividade atividade : listaDeAtividade) {
            totalFaturamento += atividade.getPreco();

            if(atividade.getTipoAtividade().equals("Venda")){
                quantVendas++;
                valorVenda += atividade.getPreco();
            }else {
                quantColocao++;
                valorColocacao += atividade.getPreco();
            }
        }

        for (Despesa despesa : listaDeDespesas) {
            totalDespesas += despesa.getPreco();

            int marge = 16;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(marge,marge,marge,marge);

            TextView txt = new TextView(this);
            txt.setText(despesa.getDespesa() + " " + DinheiroUtils.convertDouble(despesa.getPreco()));
            txt.setLayoutParams(params);
            linearLayoutDespesas.addView(txt);

        }

        lucro = totalFaturamento-totalDespesas;

        txtTotalFaturamento.setText("Faturamento: "+DinheiroUtils.convertDouble(totalFaturamento));
        txtColocacao.setText("Colocações: "+quantColocao+"\n"+DinheiroUtils.convertDouble(valorColocacao));
        txtVenda.setText("Vendas: "+quantVendas+"\n"+DinheiroUtils.convertDouble(valorVenda));
        txtTotalDespesa.setText("Despesas: "+DinheiroUtils.convertDouble(totalDespesas));
        txtLucro.setText("Lucro: "+(DinheiroUtils.convertDouble(lucro)));

    }

    public void opcaoDeItens(View v){

        if(isAtividade){

            String[] listaDeItens = new String[4];
            listaDeItens[0] = "Colocação de cabelo Orgânico";
            listaDeItens[1] = "Colocação de trança box braid";
            listaDeItens[2] = "Venda de cabelo Orgânico";
            listaDeItens[3] = "venda de jumbo";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecione um item");
            builder.setItems(listaDeItens, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int opcaoSelecionada) {
                    edtItem.setText(listaDeItens[opcaoSelecionada]);
                }
            });

            builder.create();
            builder.show();

        }else {

            ImageButton img = (ImageButton) v;

            if(img.getTag().equals("img1")){

                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                Utilitario.setDataTextView(textDataInicio,act, new DatePickerDialog(act, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String dia = String.valueOf(dayOfMonth);
                        String mes = String.valueOf(monthOfYear+1);
                        String ano = String.valueOf(year);

                        if((monthOfYear + 1) < 10)
                        {
                            mes = (0+mes);
                        }
                        if(dayOfMonth<10)
                        {
                            dia = (0+dia);
                        }

                        textDataInicio.setText(dia + "/" + mes + "/" + ano);
                        textDataFinal.setText(Utilitario.getDataFinal(textDataInicio.getText().toString()));

                    }
                }, mYear, mMonth, mDay));


            }else {
                Utilitario.setDataTextView(textDataFinal,act);
            }
        }





    }
}