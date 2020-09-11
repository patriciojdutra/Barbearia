package br.com.dutra.barbearia.Telas.Sistema;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import br.com.dutra.barbearia.Controllers.MudarTelaController;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.SharedPreferencesUtils;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private Activity act = this;
    private EditText edtCelular;
    private Button btnLogar;
    private CheckBox checkBoxLembrarMe;
    private Button btnCriarNovaConta;
    private ImageButton imgBtnGoogle;
    private ImageButton imgBtnFacebook;

    private String mVerificationId;

    private AlertDialog.Builder alert;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtCelular = (EditText)findViewById(R.id.edtCelular);
        btnLogar = (Button)findViewById(R.id.btnLogar);
        checkBoxLembrarMe = (CheckBox)findViewById(R.id.checkBoxLembrarMe);
        btnCriarNovaConta = (Button)findViewById(R.id.btnCriarNovaConta);
        imgBtnGoogle = (ImageButton)findViewById(R.id.imgBtnGoogle);
        imgBtnFacebook = (ImageButton)findViewById(R.id.imgBtnFacebook);

        init();

    }

    public void init(){

        checkBoxLembrarMe.setChecked(SharedPreferencesUtils.buscarBooleanPreferences("lembrarme",act));
        edtCelular.setText(SharedPreferencesUtils.buscarStringPreferences("celular",act));

        btnCriarNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MudarTelaController.irParaCadastroDeUsuario(false,act);
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String celular = edtCelular.getText().toString();

                if(validarDadosLogin(celular)) {
                    verificarSeTelefoneJaExiste(celular);
                }
            }
        });
    }

    public void verificarSeTelefoneJaExiste(String celular){

        AlertaUtils.dialogLoad(act);

        FirebaseFirestore.getInstance().collection("Usuario")
                .whereEqualTo("celular",celular)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (docs.isEmpty()) {
                            AlertaUtils.dialogDuplo(act, "Número de telefone inválido, ou você ainda não possui uma conta.", "Criar conta", "Fechar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MudarTelaController.irParaCadastroDeUsuario(true, act);
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                        }else {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user != null){
                                MudarTelaController.irParaDashboard(true,act);
                            }else {
                                verificarTelefone(celular);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                AlertaUtils.fecharDialog();
                AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                }, act);

            }
        });
    }

    public boolean validarDadosLogin(String celular){

        boolean valido = true;

        edtCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtCelular.setTextColor(Color.BLACK);
                edtCelular.setHintTextColor(Color.BLACK);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(celular.isEmpty()){
            edtCelular.setHintTextColor(Color.RED);
            AlertaUtils.dialogSimples("Informe o número do seu celular!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            }, this);
            return false;
        }

        if(checkBoxLembrarMe.isChecked()) {
            SharedPreferencesUtils.salvarStringPreferences("celular", celular, act);
        }else {
            SharedPreferencesUtils.salvarStringPreferences("celular", "", act);
        }

        SharedPreferencesUtils.salvarBooleanPreferences("lembrarme",checkBoxLembrarMe.isChecked(),act);

        return valido;
    }

    private void verificarTelefone(String celularInformado) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+55" + celularInformado,
                20,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

    }

    // verifica o codigo automatico
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verificarSMSRecebido(code);
            }else{

                if(phoneAuthCredential != null) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }else {
                    verificarCodigoManual();
                }
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(act, e.getMessage(), Toast.LENGTH_LONG).show();

            AlertaUtils.fecharDialog();

            AlertaUtils.dialogSimples("Não foi possivel enviar o código, aguarde 2 minutos e tente novamente\n"+e.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            },act);

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;

            verificarCodigoManual();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);

            AlertaUtils.dialogSimples(s, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            },act);

        }
    };

    private void verificarCodigoManual(){

        AlertaUtils.fecharDialog();

        View v = (View) getLayoutInflater().inflate(R.layout.modal_verificacao_code_manual,null);
        EditText editText = v.findViewById(R.id.edtCodigo);

        AlertaUtils.dialogCustomizado(act, v, "Entrar", "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface positivo, int i) {

                String codigo = editText.getText().toString();
                verificarSMSRecebido(codigo);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface negativo, int i) {

            }
        });


    }

    private void verificarSMSRecebido(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            MudarTelaController.irParaDashboard(true,act);
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            AlertaUtils.dialogSimples(message, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            },act);
                        }
                    }
                });
    }
}
