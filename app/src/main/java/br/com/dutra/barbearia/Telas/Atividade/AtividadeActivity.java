package br.com.dutra.barbearia.Telas.Atividade;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import br.com.dutra.barbearia.Controllers.MudarTelaController;
import br.com.dutra.barbearia.Modelo.Atividade;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.DinheiroUtils;
import br.com.dutra.barbearia.Utilidades.FirebaseUtils;
import br.com.dutra.barbearia.Utilidades.Utilitario;

public class AtividadeActivity extends AppCompatActivity {

    private ConstraintLayout linearLayoutData;
    private TextView textData;
    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtPreco;
    private EditText edtItem;
    private EditText edtObs;
    private Spinner spComoNosConheceu;
    private RadioButton rbServico;
    private Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayoutData = findViewById(R.id.linearLayoutData);
        textData = findViewById(R.id.textData);
        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtPreco = findViewById(R.id.edtPreco);
        edtItem = findViewById(R.id.edtItem);
        edtObs = findViewById(R.id.edtObs);
        rbServico = findViewById(R.id.rbServico);
        spComoNosConheceu = findViewById(R.id.spComoNosconheceu);

        linearLayoutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilitario.setDataTextView(textData,act);
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
            salvarAtividade();
        }

        return super.onOptionsItemSelected(item);
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

        atividade.setData(data);
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
                AlertaUtils.getDialog().dismiss();
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
                AlertaUtils.getDialog().dismiss();
                AlertaUtils.dialogSimples("Ocorreu um erro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                }, act);
            }
        });
    }
}