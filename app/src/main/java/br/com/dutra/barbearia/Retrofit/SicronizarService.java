package br.com.dutra.barbearia.Retrofit;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SicronizarService {


    @Headers({
            "Accept: application/json",
            "Authorization: Basic QjI1U0xGMlFZTExaNkhQUk9CUU9JUksyUUdGWVFEM0o6VDFaMEpIRUU5NEI5VERQRllGQ1lDSEhaUDRaRkU1N1FJVThFSUVPNw=="
    })
    @POST("orders")
    Call<JsonObject> pedido(@Body JsonObject obj);

    @Multipart
    @POST("CadastrarUsuario")
    Call<JsonObject> uploadSync(@Part("nome") RequestBody nome,
                             @Part("sobrenome") RequestBody sobrenome,
                             @Part("celular") RequestBody celular,
                             @Part("dataDeNascimento") RequestBody dataDeNascimento,
                             @Part("userLogin") RequestBody userLogin,
                             @Part("senha") RequestBody senha,
                             @Part MultipartBody.Part foto);



}
