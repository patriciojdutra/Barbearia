package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUtils {

    public static void salvarStringPreferences(String nome, String valor, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(nome,valor);
        editor.commit();
    }
    public static void salvarDoublePreferences(String nome, Float valor, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putFloat(nome,valor);
        editor.commit();
    }
    public static void salvarIntPreferences(String nome, int valor, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(nome,valor);
        editor.commit();
    }
    public static void salvarBooleanPreferences(String nome, boolean valor, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(nome,valor);
        editor.commit();
    }

    public static String buscarStringPreferences(String nome, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        return shared.getString(nome,"");
    }
    public static float buscarFloatPreferences(String nome, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        return shared.getFloat(nome,0);
    }
    public static int buscarIntPreferences(String nome, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        return shared.getInt(nome,0);
    }
    public static boolean buscarBooleanPreferences(String nome, Activity act){
        SharedPreferences shared = act.getSharedPreferences("preferences", MODE_PRIVATE);
        return shared.getBoolean(nome,false);
    }

}
