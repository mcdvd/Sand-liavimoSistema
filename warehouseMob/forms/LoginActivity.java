package com.example.warehouse.forms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehouse.R;
import com.example.warehouse.apiCalls.RetrofitClientInstance;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

    }

    public void onLoginClick(View v){
        EditText username = this.findViewById(R.id.login_username);
        EditText password = this.findViewById(R.id.login_password);
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();
        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        JsonObject jsonElementMain = new JsonObject();
        jsonElementMain.getAsJsonObject().addProperty("function", "validateUser");
        jsonElementMain.getAsJsonObject().addProperty("username", usernameValue);
        jsonElementMain.getAsJsonObject().addProperty("password", passwordValue);

        Log.d("json?", jsonElementMain.toString());
        Call<String> call = service.validateUser(jsonElementMain.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    loginToApp();
                    Log.d("Login OK", response.toString() + " " + response.body());

                } else {
                    Log.d("Login BAD", response.toString() + " " + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Delete fail", t.getMessage());
            }
        });
    }

    public void loginToApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
