package com.example.univalle_map.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.AlertDialog;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.univalle_map.R;
import com.example.univalle_map.MenuPrincipal;

public class RegistroActivity extends AppCompatActivity {
    private static final String TAG = "RegistroActivity";
    private static final String DOMINIO_PERMITIDO = "@correounivalle.edu.co";
    
    private TextInputEditText editTextNombre, editTextEmail, editTextPassword, editTextConfirmPassword;
    private MaterialButton btnRegistrar;
    private TextView txtTengoCuenta;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Inicializar vistas
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        txtTengoCuenta = findViewById(R.id.txtTengoCuenta);

        // Crear diálogo de carga
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_loading);
        builder.setCancelable(false);
        loadingDialog = builder.create();

        // Configurar botón de registro
        btnRegistrar.setOnClickListener(v -> registrarUsuario());

        // Configurar link a login
        txtTengoCuenta.setOnClickListener(v -> {
            startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
            finish();
        });
    }

    private boolean esCorreoUnivalle(String email) {
        return email != null && email.toLowerCase().endsWith(DOMINIO_PERMITIDO);
    }

    private void registrarUsuario() {
        String nombre = editTextNombre.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validaciones
        if (TextUtils.isEmpty(nombre)) {
            editTextNombre.setError("Ingrese su nombre");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Ingrese su correo");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Ingrese un correo válido");
            return;
        }

        if (!esCorreoUnivalle(email)) {
            editTextEmail.setError("Solo se permiten correos @correounivalle.edu.co");
            mostrarError("Solo se permiten registros con correo institucional de Univalle (@correounivalle.edu.co)");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Ingrese una contraseña");
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Las contraseñas no coinciden");
            return;
        }

        // Mostrar diálogo de carga
        loadingDialog.show();
        btnRegistrar.setEnabled(false);

        // Crear usuario en Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        
                        // Crear objeto Usuario
                        Usuario nuevoUsuario = new Usuario(
                            nombre,
                            email,
                            java.time.LocalDateTime.now().toString()
                        );

                        // Guardar en Realtime Database
                        mDatabase.child("usuarios")
                                .child(user.getUid())
                                .setValue(nuevoUsuario)
                                .addOnCompleteListener(dbTask -> {
                                    loadingDialog.dismiss();
                                    if (dbTask.isSuccessful()) {
                                        mostrarDialogoExito();
                                    } else {
                                        mostrarError("Error al guardar datos: " + 
                                            dbTask.getException().getMessage());
                                    }
                                });
                    } else {
                        loadingDialog.dismiss();
                        mostrarError("Error en el registro: " + 
                            task.getException().getMessage());
                    }
                    btnRegistrar.setEnabled(true);
                });
    }

    private void mostrarDialogoExito() {
        new AlertDialog.Builder(this)
                .setTitle("¡Registro Exitoso!")
                .setMessage("Tu cuenta ha sido creada correctamente")
                .setPositiveButton("Continuar", (dialog, which) -> {
                    startActivity(new Intent(RegistroActivity.this, MenuPrincipal.class));
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void mostrarError(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
} 