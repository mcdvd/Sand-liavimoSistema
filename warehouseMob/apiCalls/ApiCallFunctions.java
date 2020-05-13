package com.example.warehouse.apiCalls;

import android.util.Log;

import com.example.warehouse.objects.Attribute;
import com.example.warehouse.objects.AttributeValue;
import com.example.warehouse.objects.Barcode;
import com.example.warehouse.objects.Category;
import com.example.warehouse.objects.Item;
import com.example.warehouse.objects.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallFunctions {

    private RetrofitClientInstance.GetDataService service;

    public ApiCallFunctions() {
        service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
    }

    public interface OnItemsListReceivedCallback {
        void OnItemsListReceived(List<Item> list);
    }

    public interface OnItemsByBarcodeListReceivedCallback {
        void OnItemsByBarcodeListReceived(List<Barcode> list);
    }

    public interface OnCategoriesListReceivedCallback {
        void OnCategoriesListReceived(List<Category> list);
    }

    public void getItems(final OnItemsListReceivedCallback callback) {
        Call<List<Item>> call = service.getItems("getItems");
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                callback.OnItemsListReceived(response.body());
                Log.d("labas", response.body().listIterator().next().getItemName());
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("REST TESTsadf", t.getMessage());
            }
        });
    }

    public void getItemsByBarcode(final OnItemsByBarcodeListReceivedCallback callback, String barcode) {
        Call<List<Barcode>> call = service.getItemByBarcode("getItemByBarcode", barcode);
        call.enqueue(new Callback<List<Barcode>>() {
            @Override
            public void onResponse(Call<List<Barcode>> call, Response<List<Barcode>> response) {
                callback.OnItemsByBarcodeListReceived(response.body());

                Log.d("RESTES TESTES", response.toString());
            }

            @Override
            public void onFailure(Call<List<Barcode>> call, Throwable t) {
                Log.e("REST TEST2", t.getMessage());
            }
        });
    }

    public void addNewCategory(String categoryName, Integer parentID) {
        Category category = new Category(null, categoryName, parentID);

        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "insertCategory");
        jsonElementMain.getAsJsonObject().add("data", new Gson().toJsonTree(category));

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.insertCategory(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Insert Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Insert fail", t.getMessage());
            }
        });
    }

    public void addNewItem(String itemName, String itemBarcode, int itemCategoryID) {
        Barcode barcode = new Barcode(null, itemBarcode.trim(), "test_12", new Item(null, itemName));

        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "insertItemByBarcode");
        jsonElementMain.getAsJsonObject().add("data", new Gson().toJsonTree(barcode));
        jsonElementMain.get("data").getAsJsonObject().addProperty("categoryID", itemCategoryID);

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.insertItemByBarcode(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Insert Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Insert fail", t.getMessage());
            }
        });
    }

    public void updateItem(Item item, String barcode) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "updateItem");
        jsonElementMain.getAsJsonObject().add("data", new Gson().toJsonTree(item));
        //TODO padaryt dinamišką barcode traukimą
        jsonElementMain.getAsJsonObject().addProperty("barcode", barcode);

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.updateItem(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Update Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Update fail", t.getMessage());
            }
        });
    }

    public void deleteItem(int id) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "deleteItem");
        jsonElementMain.getAsJsonObject().addProperty("id", id);

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.deleteItem(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Delete Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Delete fail", t.getMessage());
            }
        });
    }

/*    public void getCategories(final OnCategoriesListReceivedCallback callback) {
        Call<List<Category>> call = service.getCategories("getCategories");
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                callback.OnCategoriesListReceived(response.body());
                Log.d("labas", response.body().listIterator().next().getCategory_name());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("REST TESTsadf", t.getMessage());
            }
        });
    }*/

    public List<Item> getItems() throws IOException {
        Call<List<Item>> call = service.getItems("getItems");
        Response<List<Item>> response = call.execute();
        return response.body();
    }

    public List<Category> getCategories() throws IOException {
        Call<List<Category>> call = service.getCategories("getCategories");
        Response<List<Category>> response = call.execute();
        return response.body();
    }

    public List<Attribute> getAttributes() throws IOException {
        Call<List<Attribute>> call = service.getAttributes("getAttributes");
        Response<List<Attribute>> response = call.execute();
        return response.body();
    }

    public void addNewAttribute(String attributeName, AttributeValue[] lovs) {
        Attribute attribute = new Attribute(null, attributeName, lovs);

        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "insertAttribute");
        jsonElementMain.getAsJsonObject().add("data", new Gson().toJsonTree(attribute));

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.insertAttribute(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Insert Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Insert fail", t.getMessage());
            }
        });
    }

    public List<User> getUsers() throws IOException {
        Call<List<User>> call = service.getUsers("getUsers");
        Response<List<User>> response = call.execute();
        return response.body();
    }

    public void deleteAttribute(int id) {

        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "deleteAttribute");
        jsonElementMain.getAsJsonObject().addProperty("id", id);

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.deleteAttribute(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Delete Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Delete fail", t.getMessage());
            }
        });
    }

    public void updateAttribute(Attribute attribute) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "updateAttribute");
        jsonElementMain.getAsJsonObject().add("data", new Gson().toJsonTree(attribute));

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.updateAttribute(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Update Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Update fail", t.getMessage());
            }
        });
    }

    public void deleteCategory(int id) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "deleteCategory");
        jsonElementMain.getAsJsonObject().addProperty("id", id);

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.deleteCategory(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Delete Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Delete fail", t.getMessage());
            }
        });
    }

    public void updateCategory(Category category) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "updateCategory");
        jsonElementMain.getAsJsonObject().add("data", new Gson().toJsonTree(category));

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.updateCategory(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Update Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Update fail", t.getMessage());
            }
        });
    }

    public void addNewUser(User user) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "insertUser");
        jsonElementMain.getAsJsonObject().add("data", new Gson().toJsonTree(user));

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.insertUser(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Insert Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Insert fail", t.getMessage());
            }
        });
    }

    public void updateUser(User user) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "updateUser");
        jsonElementMain.getAsJsonObject().add("data", new Gson().toJsonTree(user));

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.updateUser(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Update Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Update fail", t.getMessage());
            }
        });
    }

    public void deleteUser(int id) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "deleteUser");
        jsonElementMain.getAsJsonObject().addProperty("id", id);

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.deleteUser(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Delete Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Delete fail", t.getMessage());
            }
        });
    }

    public void validateUser(String username, String password) {
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "validateUser");
        jsonElementMain.getAsJsonObject().addProperty("username", username);
        jsonElementMain.getAsJsonObject().addProperty("password", password);

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.validateUser(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.d("Delete Ok", response.toString() + " " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Delete fail", t.getMessage());
            }
        });
    }
}
