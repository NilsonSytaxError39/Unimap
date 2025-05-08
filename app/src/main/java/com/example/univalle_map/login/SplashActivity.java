package com.example.univalle_map.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.AlphaAnimation;

import com.example.univalle_map.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 4000;
    private ImageView splashLogo;
    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Inicializar vistas
        splashLogo = findViewById(R.id.splashLogo);
        splashText = findViewById(R.id.splashText);

        // Animación de fade in para el logo
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1500);
        fadeIn.setFillAfter(true);
        splashLogo.startAnimation(fadeIn);

        // Animación de fade in para el texto con delay
        AlphaAnimation textFadeIn = new AlphaAnimation(0.0f, 1.0f);
        textFadeIn.setDuration(1500);
        textFadeIn.setStartOffset(1000);
        textFadeIn.setFillAfter(true);
        splashText.startAnimation(textFadeIn);

        // Timer para pasar a MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, SPLASH_DURATION);
    }
} 