package com.example.univalle_map.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static final String PREF_NAME = "theme_pref";
    private static final String KEY_THEME = "is_dark_mode";
    private static ThemeManager instance;
    private final SharedPreferences preferences;

    private ThemeManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context);
        }
        return instance;
    }

    public boolean isDarkMode() {
        return preferences.getBoolean(KEY_THEME, false);
    }

    public void setDarkMode(boolean isDarkMode) {
        preferences.edit().putBoolean(KEY_THEME, isDarkMode).apply();
        AppCompatDelegate.setDefaultNightMode(isDarkMode ? 
            AppCompatDelegate.MODE_NIGHT_YES : 
            AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void toggleTheme() {
        setDarkMode(!isDarkMode());
    }
} 