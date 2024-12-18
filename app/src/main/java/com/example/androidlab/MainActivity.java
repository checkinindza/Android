package com.example.androidlab;

import static com.example.androidlab.helpers.Constants.DELETE_PUBLICATION;
import static com.example.androidlab.helpers.Constants.GET_AVAILABLE_BOOKS;
import static com.example.androidlab.helpers.Constants.GET_MY_BOOKS;
import static com.example.androidlab.helpers.Constants.SET_PUBLICATION_OWNER_TO_NULL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidlab.helpers.REST;
import com.example.androidlab.model.Publication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    int selectedItem;
    String selectedItemTitle;
    Spinner spinner;
    String response;
    String selectedCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        populateSpinner();
        loadPublicationsList("Available publications");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void loadPublicationsList(String category) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                String url = category.equals("Available publications")? GET_AVAILABLE_BOOKS : category.equals("My publications")? GET_MY_BOOKS + "/" + getIntent().getStringExtra("userId") : "";
                if (!url.isEmpty()) {
                    response = REST.sendGet(url);
                }
                handler.post(() -> {
                    if (!response.equals("") && !response.equals("Error")) {
                        Gson builder = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                        Type pubsListType = new TypeToken<List<Publication>>() {
                        }.getType();
                        final List<Publication> pubsListFromJson = builder.fromJson(response, pubsListType);
                        ListView pubsList = findViewById(R.id.availableBooksList);
                        ArrayAdapter<Publication> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,pubsListFromJson);
                        pubsList.setAdapter(arrayAdapter);
                        pubsList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                        pubsList.setItemChecked(1, true);
                        pubsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                selectedItemTitle = pubsList.getItemAtPosition(position).toString();
                                Publication publication = (Publication) parent.getItemAtPosition(position);
                                selectedItem = publication.getId();
                            }
                        });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deletePublication(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete publication").setMessage("Are you sure you want to delete " + selectedItemTitle + "?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                try {
                    String responseSetOwner = REST.sendPut(SET_PUBLICATION_OWNER_TO_NULL + "/" + selectedItem);
                    String responseDeletePublication = REST.sendDelete(DELETE_PUBLICATION + "/" + selectedItem);
                    handler.post(() -> {
                        if (!responseSetOwner.equals("") && !responseSetOwner.equals("Error")) {
                            if (!responseDeletePublication.equals("") && !responseDeletePublication.equals("Error")) {
                                loadPublicationsList(selectedCategory);
                                Toast.makeText(this, "Publication deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to delete publication", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("No", (dialog, which) -> {
           dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goToUsersActivity(View view) {
        Intent intent = new Intent(this, UsersActivity.class);
        startActivity(intent);
    }

    public void populateSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.publication_spinner,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                loadPublicationsList(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}