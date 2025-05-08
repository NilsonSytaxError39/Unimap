package com.example.univalle_map;

import androidx.multidex.MultiDexApplication;
import com.google.firebase.FirebaseApp;

public class UnivalleMapApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
} 