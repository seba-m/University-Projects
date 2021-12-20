package com.seba.inventariado.utils;

import com.seba.inventariado.model.DashboardDto;
import com.seba.inventariado.model.Producto;
import com.seba.inventariado.model.Tag;
import com.seba.inventariado.model.Users;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServerService {

    //---- login & registro
    @POST("/api/v1/login")
    Call<Users> login();

    @FormUrlEncoded
    @POST("/api/v1/forgot")
    Call<Void> recover(@Field("email") String email);

    @FormUrlEncoded
    @POST("/api/v1/signup")
    Call<Void> register(@Field("email") String email, @Field("password") String password, @Field("firstName") String firstName, @Field("lastName") String lastName);

    //---- dashboard
    @GET("/api/v1/dashboard")
    Call<DashboardDto> dashboard();

    //---- productos
    @GET("/api/v1/productos/all")
    Call<List<Producto>> allProducts();

    @GET("/api/v1/productos/alerts")
    Call<List<Producto>> allAlarms();

    @GET("/api/v1/productos/search/{id}")
    Call<Producto> searchProduct(@Path("id") UUID idProducto);

    @Multipart
    @POST("/api/v1/productos/add")
    Call<Producto> saveProduct(@Part("Producto") RequestBody productToSave, @Part List<MultipartBody.Part> fotos);

    @Multipart
    @POST("/api/v1/productos/edit/{id}")
    Call<Producto> editProduct(@Path("id") String idProducto, @Part("Producto") RequestBody productToEdit, @Part List<MultipartBody.Part> fotos);

    @DELETE("/api/v1/productos/delete/{id}")
    Call<Void> deleteProduct(@Path("id") UUID idProducto);

    //---- tags
    @GET("/api/v1/tags/all")
    Call<List<Tag>> allTags();

    @POST("/api/v1/tags/add")
    Call<Tag> saveTag(@Body String tagToSave);

    @PUT("/api/v1/tags/edit/{id}")
    Call<Tag> editTag(@Path("id") String idTag, @Body Tag tagName);

    @DELETE("/api/v1/tags/delete/{id}")
    Call<Void> deleteTag(@Path("id") UUID idTag);

    //---- user
    @DELETE("/api/v1/account/deleteAccount")
    Call<Void> deleteUserAccount();

    @Multipart
    @POST("/api/v1/account/update")
    Call<Map<String, String>> updateUserAccount(@Part("UserJson") RequestBody productToSave, @Part MultipartBody.Part fotos);
}
