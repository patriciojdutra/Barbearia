package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUtils {

    public static void salvarStringPreferences(String nome, String valor, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        shared.edit().putString(nome,valor);
        shared.edit().commit();
    }

    public static void salvarDoublePreferences(String nome, Float valor, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        shared.edit().putFloat(nome,valor);
        shared.edit().commit();
    }

    public static void salvarIntPreferences(String nome, int valor, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        shared.edit().putInt(nome,valor);
        shared.edit().commit();
    }

    public static void salvarBooleanPreferences(String nome, boolean valor, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        shared.edit().putBoolean(nome,valor);
        shared.edit().commit();
    }





}
