package com.example.androidlab;

import static com.example.androidlab.helpers.Constants.VALIDATE_USER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidlab.helpers.REST;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validateUser(View view) {

        // 1. Susirenku duomenys
        TextView login = findViewById(R.id.loginField);
        TextView password = findViewById(R.id.passwordField);

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("login", login.getText().toString());
        jsonObject.addProperty("password", password.getText().toString());

        String info = gson.toJson(jsonObject);
        System.out.println(info);
        // siunciu request su prisijungimo duomenimis
        // Gaunu atsakyma, kuris yra json pavidalu

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Atitinka doInBackground
            try {
                String response = REST.sendPost(VALIDATE_USER, info);
                handler.post(() -> {
                   // atitinka onPostExecute
                   try {
                       if (!response.equals("Error") && !response.equals("")) {
                           // Viskas ok ir einam i kita langa
                           //Gson userGson = new Gson();
                           // User connectedUser = userGson.fromJson(response, User.class);
                           Log.d("Debug", "Response: " + response);
                           Properties properties = gson.fromJson(response, Properties.class);
                           String id = properties.getProperty("id");

                           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                           intent.putExtra("userId", id);
                           startActivity(intent);
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}