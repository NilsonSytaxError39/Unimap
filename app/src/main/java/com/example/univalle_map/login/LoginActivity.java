package com.example.univalle_map.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.univalle_map.R;
import com.example.univalle_map.MenuPrincipal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText editTextUsuario;
    private TextInputEditText editTextPassword;
    private MaterialButton btnIniciarSesion;
    private TextView txtRegistrate;
    private TextView txtRecuperarPassword;
    private FirebaseAuth mAuth;
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);

            // Inicializar Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            // Inicializar vistas
            editTextUsuario = findViewById(R.id.editTextUsuario);
            editTextPassword = findViewById(R.id.editTextPassword);
            btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
            txtRegistrate = findViewById(R.id.txtRegistrate);
            txtRecuperarPassword = findViewById(R.id.txtRecuperarPassword);

            // Crear diálogo de carga
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog_loading);
            builder.setCancelable(false);
            loadingDialog = builder.create();

            // Configurar el botón de inicio de sesión
            btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iniciarSesion();
                }
            });

            // Configurar el texto de registro
            txtRegistrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
                    finish();
                }
            });

            // Configurar el texto de recuperar contraseña
            txtRecuperarPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarDialogoRecuperarPassword();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, 
                "Error al inicializar la pantalla de login", Toast.LENGTH_LONG).show();
        }
    }

    private void iniciarSesion() {
        try {
            String email = editTextUsuario.getText() != null ? 
                editTextUsuario.getText().toString().trim() : "";
            String password = editTextPassword.getText() != null ? 
                editTextPassword.getText().toString().trim() : "";

            // Validación de campos
            if (TextUtils.isEmpty(email)) {
                editTextUsuario.setError("Ingrese su correo");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Ingrese su contraseña");
                return;
            }

            // Mostrar diálogo de carga
            loadingDialog.show();
            btnIniciarSesion.setEnabled(false);

            // Autenticar con Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        loadingDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            Toast.makeText(LoginActivity.this, 
                                "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                            try {
                                Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, 
                                    "Error al abrir el menú principal", Toast.LENGTH_SHORT).show();
                                btnIniciarSesion.setEnabled(true);
                            }
                        } else {
                            // Error en el inicio de sesión
                            String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Error de autenticación";
                            Toast.makeText(LoginActivity.this, 
                                "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            btnIniciarSesion.setEnabled(true);
                        }
                    });
        } catch (Exception e) {
            loadingDialog.dismiss();
            btnIniciarSesion.setEnabled(true);
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, 
                "Error al procesar el login", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogoRecuperarPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar contraseña");

        final TextInputEditText input = new TextInputEditText(this);
        input.setHint("Correo institucional");
        builder.setView(input);

        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu correo", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Correo de recuperación enviado", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Error: " + (task.getException() != null ? task.getException().getMessage() : "No se pudo enviar el correo"), Toast.LENGTH_LONG).show();
                    }
                });
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
} 