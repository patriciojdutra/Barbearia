package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void SalvarImage(Bitmap finalBitmap, String nomeArquivo, Activity act) throws IOException {

        String FILENAME = nomeArquivo;

        FileOutputStream fos = act.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

    }

}
