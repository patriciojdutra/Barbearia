package br.com.dutra.barbearia.Telas.Sistema;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import br.com.dutra.barbearia.Telas.Usuario.CadastroUsuarioActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import br.com.dutra.barbearia.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsuario;
    private EditText edtsenha;
    private AlertDialog.Builder alert;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtUsuario = (EditText)findViewById(R.id.edtUsuario);
        edtsenha = (EditText)findViewById(R.id.edtSenha);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cadastrar) {

            Intent mudarTela = new Intent(this, CadastroUsuarioActivity.class);
            startActivity(mudarTela);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View v){

        String email = edtUsuario.getText().toString();
        String password = edtsenha.getText().toString();

        if(validarDadosLogin(email,password)) {

            dialogLoad();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {

                                Intent it = new Intent(getApplicationContext(), DashBoardActivity.class);
                                startActivity(it);
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                            dialogSimples(e.getMessage());
                        }
                    });
        }
    }

    public boolean validarDadosLogin(String email, String senha){

        boolean valido = true;

        edtUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtUsuario.setTextColor(Color.BLACK);
                edtUsuario.setHintTextColor(Color.BLACK);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtsenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtsenha.setTextColor(Color.BLACK);
                edtsenha.setHintTextColor(Color.BLACK);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(email.isEmpty()){
            edtUsuario.setHintTextColor(Color.RED);
            AlertaUtils.dialogSimples("Informe seu email!", this);
            return false;
        }else {
            if(!email.contains("@")){
                edtUsuario.setTextColor(Color.RED);
                AlertaUtils.dialogSimples("Formato de email inválido,\né nescessário possuir '@' no texto!", this);
                return false;
            }

            if(!email.contains(".com")){
                edtUsuario.setTextColor(Color.RED);
                AlertaUtils.dialogSimples("Formato de email inválido,\né nescessário terminar com '.com' no texto!", this);
                return false;
            }
        }

        if(senha.isEmpty()){
            edtsenha.setHintTextColor(Color.RED);
            AlertaUtils.dialogSimples("Informe sua senha!", this);
            return false;
        }else {

            if(senha.length() < 6){

                edtsenha.setTextColor(Color.RED);
                AlertaUtils.dialogSimples("Senha inválida,\né nescessário ter no mínimo 6 dígitos!", this);
                return false;

            }
        }

        return valido;
    }

    public void SaveImage(Bitmap finalBitmap) throws IOException {

        String FILENAME = FirebaseAuth.getInstance().getUid() + ".jpg";

        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

    }

    public void dialogSimples(String txt) {

        AlertDialog alerta;
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Aviso");
        //define a mensagem
        builder.setMessage(txt);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        alerta.show();
    }

    public void dialogLoad()
    {
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Aguarde um momento...");
        alert.setMessage("Carregando");
        alert.setCancelable(false);
        final ProgressBar input = new ProgressBar (this);
        alert.setView(input);

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface aleDialog, int whichButton) {

            }
        });
        dialog = alert.show();
    }
}
