package br.com.dutra.barbearia.Telas.Agendamento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import br.com.dutra.barbearia.Adaptadores.AdaptadorListaDeHorarios;
import br.com.dutra.barbearia.Modelo.Atendimento;
import br.com.dutra.barbearia.Modelo.Cortes;
import br.com.dutra.barbearia.Modelo.Usuario.Endereco;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.BatePapo.ListaDeConversasActivity;

import br.com.dutra.barbearia.Telas.Corte.ListaDeCortesActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.ConvertUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AgendaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtData;
    private TextView txtHorario;
    private TextView txtTipoDeCorte;
    private TextView txtNomeBarbeiro;
    private TextView txtEndereco;
    private Button btnSalvar;
    private AlertDialog.Builder alert;
    private Dialog dialog;
    private Activity act = this;
    private Cortes corteSelecionado = new Cortes();
    private Usuario barbeiroSelecionado = new Usuario();
    private Endereco endereco = new Endereco();
    private AlertDialog alerta;

    List<String> listaDoshorarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        inicializacao();
        buscarEndereco();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_finalizar_agendamento, menu);
        MenuItem btnFinalizar = menu.findItem(R.id.action_finalizar);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_finalizar && validarDados()) {

            salvarAtendimentoNoFirebase();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                corteSelecionado = (Cortes) data.getExtras().get("nomeCorte");
                txtTipoDeCorte.setText(corteSelecionado.getNomeDoCorte());
            }
        }

        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){

                barbeiroSelecionado = (Usuario) data.getExtras().get("nomeBarbeiro");
                txtNomeBarbeiro.setText(barbeiroSelecionado.getNome() + " " + barbeiroSelecionado.getSobrenome());
            }
        }
    }

    public void inicializacao(){


        txtData = findViewById(R.id.txtData);
        txtHorario = findViewById(R.id.txtHorario);
        txtTipoDeCorte = findViewById(R.id.txtTipoDeCorte);
        txtNomeBarbeiro = findViewById(R.id.txtNomeBarbeiro);
        txtEndereco = findViewById(R.id.txtEndereco);
        btnSalvar = findViewById(R.id.btnLogar);

   }

    public void setDataDoAgendamento(View v) {

        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String dia = String.valueOf(dayOfMonth);
                        String mes = String.valueOf(monthOfYear+1);
                        String ano = String.valueOf(year);

                        Calendar dataSelecionada = Calendar.getInstance();
                        dataSelecionada.set(year,monthOfYear,dayOfMonth);
//                        if(dataSelecionada.DAY_OF_WEEK == Calendar.SATURDAY || dataSelecionada.DAY_OF_WEEK == Calendar.SUNDAY){
//                            AlertaUtils.dialogSimples("Este dia estaremos fechado",act);
//                        }
                        if(monthOfYear<10)
                        {
                            mes = (0+mes);
                        }
                        if(dayOfMonth<10)
                        {
                            dia = (0+dia);
                        }

                        txtData.setText(dia + "/" + mes + "/" + ano);
                    }
                }, mYear, mMonth, mDay);


        //data minima para agendar
        Calendar dataMinima = Calendar.getInstance();
        dataMinima.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(dataMinima.getTimeInMillis());

        //data max para agendar
        Calendar dataMax = Calendar.getInstance();
        dataMax.add(Calendar.DAY_OF_MONTH, 20);
        datePickerDialog.getDatePicker().setMaxDate(dataMax.getTimeInMillis());

        datePickerDialog.show();
    }

    public void setHorarioDoAgendamento(View v) {

        final String sData = txtData.getText().toString();

        Calendar cDataSelecionada = ConvertUtils.ConverteStringParaData(sData,this);
        String diaDaSemana = ConvertUtils.retornaDiaDaSemana(cDataSelecionada,this);
        String nomeBarbeiro = txtNomeBarbeiro.getText().toString();

        buscarListaDeHorarios(sData,diaDaSemana,nomeBarbeiro);

    }

    public void setartipoDecorte(View v){

        Intent it = new Intent(this, ListaDeCortesActivity.class);
        it.putExtra("solicitacao","nomeCorte");
        startActivityForResult(it,1);

    }

    public void setarNomeBarbeiro(View v){

        Intent it = new Intent(this, ListaDeConversasActivity.class);
        it.putExtra("solicitacao","nomeBarbeiro");
        startActivityForResult(it,2);

    }

    public void setarEndereco(View v){

        View view = getLayoutInflater().inflate(R.layout.layout_cadastro_endereco, null);

        final EditText txtCep = view.findViewById(R.id.txtCep);
        final EditText txtRua = view.findViewById(R.id.txtRua);
        final EditText txtNumeroCasa = view.findViewById(R.id.txtNumeroCasa);
        final EditText txtBairro = view.findViewById(R.id.txtBairro);
        final EditText txtCidade = view.findViewById(R.id.txtCidade);
        final EditText txtEstado = view.findViewById(R.id.txtEstado);
        final EditText txtComplemento = view.findViewById(R.id.txtComplemento);
        Button btnSalvar = view.findViewById(R.id.btnSalvarEndereco);
        Button btnCancelar = view.findViewById(R.id.btnCancelarEndereco);

        if(endereco != null){
            txtCep.setText(endereco.getCep());
            txtRua.setText(endereco.getRua());
            txtNumeroCasa.setText(endereco.getNumero());
            txtBairro.setText(endereco.getBairro());
            txtCidade.setText(endereco.getCidade());
            txtEstado.setText(endereco.getEstado());
            txtComplemento.setText(endereco.getComplemento());
            btnSalvar.setText("Atualizar");
        }


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getUid();
                String cep = txtCep.getText().toString();
                String rua = txtRua.getText().toString();
                String numero = txtNumeroCasa.getText().toString();
                String bairro = txtBairro.getText().toString();
                String cidade = txtCidade.getText().toString();
                String estado = txtEstado.getText().toString();
                String complemento = txtComplemento.getText().toString();

                Endereco endereco = new Endereco(uid,cidade,complemento,bairro,rua,numero,cep,estado);

                if(validarEndereco()){
                    cadastrarEndereco(endereco);
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alerta!=null){
                    alerta.dismiss();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(act,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        builder.setTitle("Informe os dados do seu endereço!");
        builder.setIcon(R.drawable.logo250);
        builder.setView(view);
        alerta = builder.create();
        alerta.show();

    }

    public void buscarEndereco(){

        AlertaUtils.dialogLoad(this);

        String uId = FirebaseAuth.getInstance().getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Endereco").document(uId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                try {
                    endereco = documentSnapshot.toObject(Endereco.class);
                    txtEndereco.setText(endereco.getRua() + " nº:" + endereco.getNumero());
                }catch (Exception e){};

                AlertaUtils.getDialog().dismiss();

            }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });
    }

    public void mostrarHorariosDisponiceis() {

        final Dialog horariosDisponiveis = new Dialog(this);
        horariosDisponiveis.setContentView(R.layout.layout_horarios);

        TextView txt = (TextView) horariosDisponiveis.findViewById(R.id.txttitulo);
        txt.setText("Informe o horario desejado");

        GridView gridView = (GridView) horariosDisponiveis.findViewById(R.id.gridView);
        AdaptadorListaDeHorarios adapter = new AdaptadorListaDeHorarios(listaDoshorarios,this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                txtHorario.setText(listaDoshorarios.get(i));
                horariosDisponiveis.dismiss();

            }
        });

        horariosDisponiveis.show();
    }

    public void salvarAtendimentoNoFirebase(){

        String data = txtData.getText().toString();
        String horario = txtHorario.getText().toString();
        String tipoDeCorte = txtTipoDeCorte.getText().toString();
        String nomeBarbeiro = txtNomeBarbeiro.getText().toString();
        String valorDoCorte = corteSelecionado.getValorDoCorte();
        String status = "Agendado";
        String usuarioId = FirebaseAuth.getInstance().getUid();

        final Atendimento atendimento = new Atendimento(data,horario,status,usuarioId,tipoDeCorte,nomeBarbeiro,valorDoCorte,true);

        String idAtenndimento = data.replace("/","\\");
        idAtenndimento = idAtenndimento+"-"+horario;
        idAtenndimento = idAtenndimento + barbeiroSelecionado.getUiId();

        AlertaUtils.dialogLoad(act);

        FirebaseFirestore.getInstance().collection("Atendimento")
                .document(idAtenndimento)
                .set(atendimento)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        AlertaUtils.getDialog().dismiss();
                        dialogConfirmacao(atendimento);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.dialogSimples("Erro (P02)\nNão foi possivel cadastrar usuário", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });

    }

    public void buscarListaDeHorarios(final String data, String diaDaSemana, final String nomeDoBarbeiro){

        AlertaUtils.dialogLoad(act);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Horario")
                .document(diaDaSemana);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                listaDoshorarios = (List<String>)documentSnapshot.getData().get("horariosDeAtendimento");
                verificarHorariosJaMarcados(data,nomeDoBarbeiro);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertaUtils.getDialog().dismiss();
                AlertaUtils.dialogSimples("Ocorreu um erro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, act);
            }
        });
    }

    public void verificarHorariosJaMarcados(final String data, String nomeDoBarbeiro){

            FirebaseFirestore.getInstance().collection("Atendimento")
                    .whereEqualTo("data", data)
                    .whereEqualTo("nomeBarbeiro",nomeDoBarbeiro)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                            if (!docs.isEmpty()) {

                                for (DocumentSnapshot doc : docs) {

                                    Atendimento atendimento = doc.toObject(Atendimento.class);

                                    for (int i = 0; i < listaDoshorarios.size(); i++) {

                                        if (listaDoshorarios.get(i).equals(atendimento.getHorario()) && data.equalsIgnoreCase(atendimento.getData())) {
                                            listaDoshorarios.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }

                            AlertaUtils.getDialog().dismiss();
                            mostrarHorariosDisponiceis();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    AlertaUtils.getDialog().dismiss();
                    AlertaUtils.dialogSimples("Ocorreu um erro", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }, act);
                }
            });
    }

    public void cadastrarEndereco(final Endereco endereco){

        AlertaUtils.dialogLoad(act);
        FirebaseFirestore.getInstance().collection("Endereco")
                .document(endereco.getUidUsuario())
                .set(endereco)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AlertaUtils.getDialog().dismiss();
                        if(alerta!=null)
                            alerta.dismiss();
                        try {
                            txtEndereco.setText(endereco.getRua() + " nº:" + endereco.getNumero());
                        }catch (Exception e){};

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });
    }

    public boolean validarDados(){

        String data = txtData.getText().toString();
        String horario = txtHorario.getText().toString();
        String tipoDeCorte = txtTipoDeCorte.getText().toString();
        String nomeBarbeiro = txtNomeBarbeiro.getText().toString();
        String valorDoCorte = corteSelecionado.getValorDoCorte();
        String sEndereco = txtEndereco.getText().toString();

        if(nomeBarbeiro.equals("Clique aqui")){
            AlertaUtils.dialogSimples("Informe um barbeiro para seu atendimento", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        if(data.equals("Clique aqui")){
            AlertaUtils.dialogSimples("Informe uma data para o seu agendamento", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        if(horario.equals("Clique aqui")){
            AlertaUtils.dialogSimples("Informe o horário do seu agendamento", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        if(tipoDeCorte.equals("Clique aqui")){
            AlertaUtils.dialogSimples("Informe o corte desejado", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        if(sEndereco.equals("Clique aqui")){
            AlertaUtils.dialogSimples("Devido ao surto de coronavírus, só será possível agendar atendimento em domicílio, por favor Informe o seu endereço", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return false;
        }

        return true;

    }

    public boolean validarEndereco(){
        return true;
    }

    public void dialogConfirmacao(Atendimento atendimento) {

        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aviso");
        builder.setIcon(R.drawable.baseline_access_time_24);
        builder.setMessage("Atendimento agendado com sucesso para o dia "+atendimento.getData()+
                " com o profissional "+atendimento.getNomeBarbeiro()+ " as "+atendimento.getHorario());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {
                finish();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

}