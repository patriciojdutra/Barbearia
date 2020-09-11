package br.com.dutra.barbearia.Utilidades;

import android.content.DialogInterface;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import br.com.dutra.barbearia.Modelo.Produto;
import br.com.dutra.barbearia.Modelo.Usuario.Usuario;

public class FirebaseUtils<E> {

    public void salvarNoFirebase(String tabela, E documento, OnSuccessListener onSuccessListener,OnFailureListener onFailureListener) {

        String idDocumento = Utilitario.dataEHoraAtual().replace("/","");
        idDocumento = idDocumento.replace(":","");

        FirebaseFirestore.getInstance().collection(tabela).document(idDocumento).set(documento).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }

    public void buscarObjetoNoFirebase(String tabela, String id, OnSuccessListener onSuccessListener,OnFailureListener onFailureListener) {

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(tabela).document(id);
        docRef.get().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);

    }

}
