package br.com.dutra.barbearia.Controllers;

import android.app.Activity;

import br.com.dutra.barbearia.Retrofit.RetrofitConfig;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.Utilitario;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagamentoController {

    private Activity act;

    public PagamentoController(Activity act) {
        this.act = act;
    }

    public void cadastrarClienteNoWireCard() {

        AlertaUtils.dialogLoad(act);

        JsonObject json = new JsonObject();
        json.addProperty("ownId","pedido_2");

        JsonArray arrayItem = new JsonArray();

        JsonObject item = new JsonObject();
        item.addProperty("product","carro");
        item.addProperty("quantity",5);
        item.addProperty("detail","Eco sport 2020");
        item.addProperty("price","2000000");

        arrayItem.add(item);

        json.add("items",arrayItem);

        JsonObject customer = new JsonObject();
        customer.addProperty("ownId","cliente_xyz");
        customer.addProperty("fullname","João Silva");
        customer.addProperty("email","joaosilva@email.com");

        json.add("customer",customer);

        Call<JsonObject> call = new RetrofitConfig(Utilitario.Url).getSicronizarService().pedido(json);

        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.body() != null) {

                    String sResponse = response.body().toString();
                    JSONObject jObj = new JSONObject();

                    try {

                        jObj = new JSONObject(sResponse);
                        String id = jObj.getString("id");

                    } catch (JSONException e) {

                    }

                }else{ }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t)
            {
String me = t.getMessage();
int a = 1;
            }
        });

    }




}
