package br.com.dutra.barbearia.Utilidades;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils<E> {

    public void salvarNoFirebase(String tabela, E documento, OnSuccessListener onSuccessListener,OnFailureListener onFailureListener) {

        String idDocumento = Utilitario.dataEHoraAtual().replace("/","");
        idDocumento = idDocumento.replace(":","");

        FirebaseFirestore.getInstance().collection(tabela).document(idDocumento).set(documento).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
}
