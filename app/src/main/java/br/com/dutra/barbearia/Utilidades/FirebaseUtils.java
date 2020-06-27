package br.com.dutra.barbearia.Utilidades;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseUtils<E> {

    public void salvarNoFirebase(String tabela, E documento, OnSuccessListener onSuccessListener,OnFailureListener onFailureListener) {

        FirebaseFirestore.getInstance().collection(tabela).add(documento).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);

    }
}
