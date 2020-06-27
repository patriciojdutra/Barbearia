package br.com.dutra.barbearia.Telas.Sistema;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import br.com.dutra.barbearia.Controllers.MudarTelaController;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.SharedPreferencesUtils;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private Activity act = this;
    private EditText edtUsuario;
    private EditText edtsenha;
    private Button btnLogar;
    private CheckBox checkBoxLembrarMe;
    private Button btnCriarNovaConta;
    private ImageButton imgBtnGoogle;
    private ImageButton imgBtnFacebook;


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
        btnLogar = (Button)findViewById(R.id.btnLogar);
        checkBoxLembrarMe = (CheckBox)findViewById(R.id.checkBoxLembrarMe);
        btnCriarNovaConta = (Button)findViewById(R.id.btnCriarNovaConta);
        imgBtnGoogle = (ImageButton)findViewById(R.id.imgBtnGoogle);
        imgBtnFacebook = (ImageButton)findViewById(R.id.imgBtnFacebook);

        init();

    }

    public void init(){

        checkBoxLembrarMe.setChecked(SharedPreferencesUtils.buscarBooleanPreferences("lembrarme",act));
        edtUsuario.setText(SharedPreferencesUtils.buscarStringPreferences("email",act));
        edtsenha.setText(SharedPreferencesUtils.buscarStringPreferences("senha",act));

        btnCriarNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MudarTelaController.irParaCadastroDeUsuario(false,act);
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtUsuario.getText().toString();
                String password = edtsenha.getText().toString();

                if(validarDadosLogin(email,password)) {

                    AlertaUtils.dialogLoad(act);

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    AlertaUtils.getDialog().dismiss();

                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        MudarTelaController.irParaDashboard(true,act);
                                    }
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
            }
        });
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
            AlertaUtils.dialogSimples("Informe seu email!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, this);
            return false;
        }else {
            if(!email.contains("@")){
                edtUsuario.setTextColor(Color.RED);
                AlertaUtils.dialogSimples("Formato de email inválido,\né nescessário possuir '@' no texto!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, this);
                return false;
            }

            if(!email.contains(".com")){
                edtUsuario.setTextColor(Color.RED);
                AlertaUtils.dialogSimples("Formato de email inválido,\né nescessário terminar com '.com' no texto!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, this);
                return false;
            }
        }

        if(senha.isEmpty()){
            edtsenha.setHintTextColor(Color.RED);
            AlertaUtils.dialogSimples("Informe sua senha!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, this);
            return false;
        }else {

            if(senha.length() < 6){

                edtsenha.setTextColor(Color.RED);
                AlertaUtils.dialogSimples("Senha inválida,\né nescessário ter no mínimo 6 dígitos!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, this);
                return false;

            }
        }

        if(checkBoxLembrarMe.isChecked()) {
            SharedPreferencesUtils.salvarStringPreferences("email", email, act);
            SharedPreferencesUtils.salvarStringPreferences("senha", senha, act);
        }else {
            SharedPreferencesUtils.salvarStringPreferences("email", "", act);
            SharedPreferencesUtils.salvarStringPreferences("senha", "", act);
        }

        SharedPreferencesUtils.salvarBooleanPreferences("lembrarme",checkBoxLembrarMe.isChecked(),act);

        return valido;
    }
}
