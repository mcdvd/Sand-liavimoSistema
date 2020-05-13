package com.example.warehouse.apiCalls;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.example.warehouse.objects.Attribute;
import com.example.warehouse.objects.Barcode;
import com.example.warehouse.objects.Category;
import com.example.warehouse.objects.Item;
import com.example.warehouse.objects.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.0.50:80/api/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public interface GetDataService {
        @GET("item/item_api.php")
        Call<List<Item>> getItems(@Query("function") String function);

        @GET("category/category_api.php")
        Call<List<Category>> getCategories(@Query("function") String function);

        @GET("barcode/barcode_api.php")
        Call<List<Barcode>> getItemByBarcode(@Query("function") String function, @Query("barcode") String barcode);

        @Headers("Content-Type: application/json")
        @POST("barcode/barcode_api.php")
        Call<String> insertItemByBarcode(@Body String barcode);

        @Headers("Content-Type: application/json")
        @POST("item/item_api.php")
        Call<String> updateItem(@Body String Item);

        @Headers("Content-Type: application/json")
        @POST("item/item_api.php")
        Call<String> deleteItem(@Body String Item);

        @Headers("Content-Type: application/json")
        @POST("category/category_api.php")
        Call<String> insertCategory(@Body String category);

        @GET("attribute/attribute_api.php")
        Call<List<Attribute>> getAttributes(@Query("function") String function);

        @Headers("Content-Type: application/json")
        @POST("attribute/attribute_api.php")
        Call<String> insertAttribute(@Body String attribute);

        @Headers("Content-Type: application/json")
        @POST("attribute/attribute_api.php")
        Call<String> deleteAttribute(@Body String attribute);

        @Headers("Content-Type: application/json")
        @POST("attribute/attribute_api.php")
        Call<String> updateAttribute(@Body String attribute);

        @Headers("Content-Type: application/json")
        @POST("category/category_api.php")
        Call<String> deleteCategory(@Body String category);

        @Headers("Content-Type: application/json")
        @POST("category/category_api.php")
        Call<String> updateCategory(@Body String category);

        @GET("user/user_api.php")
        Call<List<User>> getUsers(@Query("function") String function);

        @Headers("Content-Type: application/json")
        @POST("user/user_api.php")
        Call<String> insertUser(@Body String user);

        @Headers("Content-Type: application/json")
        @POST("user/user_api.php")
        Call<String> updateUser(@Body String user);

        @Headers("Content-Type: application/json")
        @POST("user/user_api.php")
        Call<String> deleteUser(@Body String user);

        @Headers("Content-Type: application/json")
        @POST("user/user_api.php")
        Call<String> validateUser(@Body String user);
    }
}