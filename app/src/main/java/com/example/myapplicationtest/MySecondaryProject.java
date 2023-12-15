package com.example.myapplicationtest;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class MySecondaryProject {
    private static FirebaseApp INSTANCE;

    public static FirebaseApp getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = getSecondProject(context);
        }
        return INSTANCE;
    }

    private static FirebaseApp getSecondProject(Context context) {
        FirebaseOptions options1 = new FirebaseOptions.Builder()
                .setApiKey("AI...gs")
                .setApplicationId("1:5...46")
                .setProjectId("my-project-id")
                // setDatabaseURL(...)      // in case you need firebase Realtime database
                // setStorageBucket(...)    // in case you need firebase storage MySecondaryProject
                .build();

        FirebaseApp.initializeApp(context, options1, "admin");
        return FirebaseApp.getInstance("admin");
    }

    //Usage Below:
    //FirebaseFirestore db = FirebaseFirestore.getInstance(); //- DEFAULT PROJECT
    //FirebaseFirestore secondDB = FirebaseFirestore.getInstance(MySecondaryProject.getInstance(this)); // - SECOND PROJECT
}
