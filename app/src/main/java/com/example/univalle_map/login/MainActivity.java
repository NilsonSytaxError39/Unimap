package com.example.univalle_map.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import com.example.univalle_map.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.univalle_map.utils.ThemeManager;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton btnLogin;
    private AppCompatButton btnRegistro;
    private AppCompatButton btnInvitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Aplicar tema guardado
        ThemeManager.getInstance(this).setDarkMode(
            ThemeManager.getInstance(this).isDarkMode()
        );
        
        // Verificar si el usuario ya está autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, com.example.univalle_map.MenuPrincipal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        
        setContentView(R.layout.activity_main);

        try {
            // Inicializar botones
            btnLogin = findViewById(R.id.btnLogin);
            btnRegistro = findViewById(R.id.btnRegistro);
            btnInvitado = findViewById(R.id.btnInvitado);

            // Configurar el botón de login
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, 
                            "Error al abrir la pantalla de login", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Configurar el botón de registro
            btnRegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, 
                            "Error al abrir la pantalla de registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Configurar el botón de invitado
            btnInvitado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, com.example.univalle_map.MenuPrincipal.class);
                        intent.putExtra("modo_invitado", true);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, 
                            "Error al abrir modo invitado: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al inicializar la aplicación", 
                Toast.LENGTH_LONG).show();
        }
    }

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presiona nuevamente para salir", 
            Toast.LENGTH_SHORT).show();

        new android.os.Handler().postDelayed(
            () -> doubleBackToExitPressedOnce = false, 2000);
    }
} 