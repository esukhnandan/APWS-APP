package com.example.myapplicationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity2 extends AppCompatActivity {

    EditText name, comname, sciname, symbol, waterusage,surl;
    Button btnAdd, btnBack, btnAddImg, btnSelect;
    RecyclerView recyclerView2;
    MainApiAdapter mainAdapter2;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    EditText searchEditText2;

    //variables to store selected plant details
    private String selectedComName;
    private String selectedSciName;
    private String selectedSymbol;
    private String selectedWaterUsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add2);

        name = findViewById(R.id.txtName);
        surl = findViewById(R.id.txtSurl);
        btnAddImg = findViewById(R.id.btnAddImg);
        recyclerView2 = findViewById(R.id.arvp);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        searchEditText2 = findViewById(R.id.search_bar2);

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                clearAll();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);

        FirebaseRecyclerOptions<MainApiPlants> options =
                new FirebaseRecyclerOptions.Builder<MainApiPlants>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PlantApi"), MainApiPlants.class)
                        .build();

        mainAdapter2 = new MainApiAdapter(options);
        recyclerView2.setAdapter(mainAdapter2);

        Log.d("RecyclerView", "Setting up RecyclerView and adapter");

        searchEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString();
                txtSearch2(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //not needed
            }
        });

    }

    public void setDataFromAdapter(String comName, String sciName, String Symbol, String waterUsage) {
        selectedComName = comName;
        selectedSciName = sciName;
        selectedSymbol = Symbol;
        selectedWaterUsage = waterUsage;
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            surl.setText(imageUri.toString());
        }
    }

    private void insertData() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("surl", surl.getText().toString());
        map.put("comname", MainApiAdapter.newselectedComName);
        map.put("sciname", MainApiAdapter.newselectedSciName);
        map.put("symbol", MainApiAdapter.newselectedSymbol);
        map.put("waterusage", MainApiAdapter.newselectedWaterUsage);
        //added 4 more

        FirebaseDatabase.getInstance().getReference().child("UsersPlants").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddActivity2.this, "New Plant Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddActivity2.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAll() {
        name.setText("");
        surl.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter2.stopListening();
    }

    private void txtSearch2(String query) {
        FirebaseRecyclerOptions<MainApiPlants> options =
                new FirebaseRecyclerOptions.Builder<MainApiPlants>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PlantApi")
                                .orderByChild("comname")
                                .startAt(query.toLowerCase())
                                .endAt(query.toLowerCase() + "~"), MainApiPlants.class)
                        .build();

        mainAdapter2 = new MainApiAdapter(options);
        recyclerView2.setAdapter(mainAdapter2);
        mainAdapter2.startListening();
    }
}

