package com.example.myapplicationtest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.text.TextWatcher;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;



//fragment for manageplants page
public class BlankFragment extends Fragment {

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button button3 = view.findViewById(R.id.add_new_plants);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddActivity2.class));
            }
        });
    }

    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    //edit search bar
    EditText searchEditText; // = findViewById(R.id.search_bar1);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate
        View rootView = inflater.inflate(R.layout.manage_plants_list, container, false);

        //new this sets up EditText
        searchEditText = rootView.findViewById(R.id.search_bar1);

        //RecyclerView
        recyclerView = rootView.findViewById(R.id.rvp);

        //layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Firebase reference
        FirebaseRecyclerOptions<MainUserPlants> options =
                new FirebaseRecyclerOptions.Builder<MainUserPlants>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("UsersPlants"),MainUserPlants.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        Log.d("RecyclerView", "Setting up RecyclerView and adapter");
        searchEditText.addTextChangedListener(new TextWatcher() {
            View rootView = inflater.inflate(R.layout.manage_plants_list, container, false);


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString();
                txtSearch(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Not needed
            }
        });


        return rootView;
    }
    @Override
    public void onStart(){
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        mainAdapter.stopListening();
    }

    private void txtSearch(String query) {
        FirebaseRecyclerOptions<MainUserPlants> options =
                new FirebaseRecyclerOptions.Builder<MainUserPlants>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("UsersPlants")
                                .orderByChild("name")
                                .startAt(query)
                                .endAt(query + "~"), MainUserPlants.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.startListening();
    }

}