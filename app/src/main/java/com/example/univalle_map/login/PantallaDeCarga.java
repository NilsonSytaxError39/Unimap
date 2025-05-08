package com.example.univalle_map.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.view.animation.Animation;
import android.graphics.Typeface;
import android.graphics.Color;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;

import com.example.univalle_map.R;

public class PantallaDeCarga extends Activity {
    private static final int SPLASH_DURATION = 4000; // 4 segundos para ver mejor las animaciones
    // Color rojo exacto del logo de Univalle
    private static final int COLOR_ROJO_UNIVALLE = Color.rgb(237, 28, 36);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Crear layout principal con fondo blanco
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.WHITE);
        
        // Configurar ImageView para el logo
        ImageView logoImage = new ImageView(this);
        logoImage.setImageResource(R.drawable.logo_univalle);
        logoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
            800, // Ancho aumentado para mejor visibilidad
            800  // Alto aumentado para mejor visibilidad
        );
        imageParams.setMargins(50, 50, 50, 50);
        logoImage.setLayoutParams(imageParams);
        
        // Configurar TextView para el mensaje de bienvenida
        TextView welcomeText = new TextView(this);
        welcomeText.setText("Bienvenido a\nUniMap");
        welcomeText.setGravity(Gravity.CENTER);
        welcomeText.setTextSize(32);
        welcomeText.setTypeface(Typeface.DEFAULT_BOLD);
        welcomeText.setTextColor(COLOR_ROJO_UNIVALLE);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.topMargin = 50;
        welcomeText.setLayoutParams(textParams);
        
        // Añadir vistas al layout
        layout.addView(logoImage);
        layout.addView(welcomeText);
        
        setContentView(layout);

        // Animación de aparición gradual para el logo
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1500);
        fadeIn.setFillAfter(true);
        logoImage.startAnimation(fadeIn);

        // Animación de escala para el logo
        ScaleAnimation scaleAnimation = new ScaleAnimation(
            0.7f, 1.0f,
            0.7f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1500);
        scaleAnimation.setStartOffset(1000);
        logoImage.startAnimation(scaleAnimation);

        // Animación de aparición para el texto
        AlphaAnimation textFadeIn = new AlphaAnimation(0.0f, 1.0f);
        textFadeIn.setDuration(1500);
        textFadeIn.setStartOffset(2000);
        textFadeIn.setFillAfter(true);
        welcomeText.startAnimation(textFadeIn);

        // Timer para pasar a la siguiente actividad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                fadeOut.setDuration(500);
                fadeOut.setFillAfter(true);
                layout.startAnimation(fadeOut);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(PantallaDeCarga.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                }, 500);
            }
        }, SPLASH_DURATION);
    }
} 