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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//fragment for manageplants page
public class BlankFragment extends Fragment {
    //new stuff





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //recyclerView = (RecyclerView)findViewById(R.id.rvp);
        //recyclerView

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button button3 = view.findViewById(R.id.add_new_plants);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("ButtonClick", "Button clicked");
                //Navigation.findNavController(view).navigate(R.id.action_PlantFragment_to_add_plant_page);
                //old ^
                startActivity(new Intent(getContext(), AddActivity2.class));
            }
        });
    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.manage_plants_list, container, false);
    }
    */
    //old code above comnteded.

    //new below
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    //edit search bar
    EditText searchEditText; // = findViewById(R.id.search_bar1);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.manage_plants_list, container, false);

        //new this sets up EditText
        searchEditText = rootView.findViewById(R.id.search_bar1);

        // Set up the RecyclerView
        recyclerView = rootView.findViewById(R.id.rvp);
        // Now you can use the recyclerView

        //more stuff
        // Set up the layout manager (e.g., LinearLayoutManager)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Add any additional setup or logic related to your Fragment's UI
        FirebaseRecyclerOptions<MainUserPlants> options =
                new FirebaseRecyclerOptions.Builder<MainUserPlants>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("UsersPlants"),MainUserPlants.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        Log.d("RecyclerView", "Setting up RecyclerView and adapter");
        //added code in
        searchEditText.addTextChangedListener(new TextWatcher() {
            View rootView = inflater.inflate(R.layout.manage_plants_list, container, false);


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString();
                txtSearch(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this example
                return;
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
    //more new code below for search
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater.inflate(R.menu.search_bar1,menu);
        MenuItem item = menu.findItem(R.id.search_bar1);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                txtSearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query){
                txtSearch(query);
                return false;
            }


        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str){
        FirebaseRecyclerOptions<MainUserPlants> options =
                new FirebaseRecyclerOptions.Builder<MainUserPlants>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("UsersPlants").orderByChild("comname").startAt(str).endAt(str+"~"),MainUserPlants.class)
                        .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }*/
    //revised code below

    //RecyclerView recyclerView2 = findViewById(R.id.rvp);

// Set up the RecyclerView and adapter



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