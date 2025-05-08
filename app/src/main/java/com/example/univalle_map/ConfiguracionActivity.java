package com.example.univalle_map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;

import com.example.univalle_map.login.LoginActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.univalle_map.utils.ThemeManager;
import java.io.File;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class ConfiguracionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    private SwitchMaterial switchModoOscuro;
    private SwitchMaterial switchNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar SharedPreferences
        preferences = getSharedPreferences("UniMapPrefs", MODE_PRIVATE);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Inicializar vistas
        switchModoOscuro = findViewById(R.id.switchModoOscuro);
        switchNotificaciones = findViewById(R.id.switchNotificaciones);
        
        // Sincronizar el estado del switch con la preferencia guardada
        boolean isDark = ThemeManager.getInstance(this).isDarkMode();
        switchModoOscuro.setChecked(isDark);
        
        // Configurar listener para el switch de modo oscuro
        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeManager.getInstance(this).setDarkMode(isChecked);
            // No es necesario recrear la actividad
        });
        
        // Ocultar opciones si es invitado
        boolean esInvitado = getIntent().getBooleanExtra("modo_invitado", false);
        if (esInvitado) {
            View cardCambiarPassword = findViewById(R.id.cardCambiarPassword);
            if (cardCambiarPassword != null) cardCambiarPassword.setVisibility(View.GONE);
            View cardBorrarDatos = findViewById(R.id.cardBorrarDatos);
            if (cardBorrarDatos != null) cardBorrarDatos.setVisibility(View.GONE);
            // Ocultar el TextView de la sección 'Datos'
            for (int i = 0; i < ((ViewGroup)cardBorrarDatos.getParent()).getChildCount(); i++) {
                View v = ((ViewGroup)cardBorrarDatos.getParent()).getChildAt(i);
                if (v instanceof TextView && ((TextView)v).getText().toString().trim().equalsIgnoreCase("Datos")) {
                    v.setVisibility(View.GONE);
                    break;
                }
            }
        }
        
        // Cargar estado de notificaciones
        boolean notificacionesActivadas = preferences.getBoolean("notificaciones", true);
        switchNotificaciones.setChecked(notificacionesActivadas);

        // Configurar listeners
        findViewById(R.id.cardCambiarPassword).setOnClickListener(v -> mostrarDialogoCambiarPassword());
        findViewById(R.id.cardBorrarDatos).setOnClickListener(v -> mostrarDialogoBorrarDatos());
        
        switchNotificaciones.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("notificaciones", isChecked).apply();
            Toast.makeText(this, 
                isChecked ? "Notificaciones activadas" : "Notificaciones desactivadas", 
                Toast.LENGTH_SHORT).show();
        });
    }

    private void mostrarDialogoCambiarPassword() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_cambiar_password, null);
        EditText editTextPasswordActual = view.findViewById(R.id.editTextPasswordActual);
        EditText editTextPasswordNueva = view.findViewById(R.id.editTextPasswordNueva);
        EditText editTextConfirmarPassword = view.findViewById(R.id.editTextConfirmarPassword);

        new AlertDialog.Builder(this)
                .setTitle("Cambiar Contraseña")
                .setView(view)
                .setPositiveButton("Cambiar", (dialog, which) -> {
                    String passwordActual = editTextPasswordActual.getText().toString();
                    String passwordNueva = editTextPasswordNueva.getText().toString();
                    String confirmarPassword = editTextConfirmarPassword.getText().toString();

                    if (passwordActual.isEmpty() || passwordNueva.isEmpty() || confirmarPassword.isEmpty()) {
                        Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!passwordNueva.equals(confirmarPassword)) {
                        Toast.makeText(this, "Las contraseñas nuevas no coinciden", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    cambiarPassword(passwordActual, passwordNueva);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cambiarPassword(String passwordActual, String passwordNueva) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Reautenticar usuario
            mAuth.signInWithEmailAndPassword(user.getEmail(), passwordActual)
                    .addOnSuccessListener(authResult -> {
                        // Cambiar contraseña
                        user.updatePassword(passwordNueva)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, 
                                            "Contraseña actualizada correctamente", 
                                            Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, 
                                            "Error al actualizar la contraseña", 
                                            Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .addOnFailureListener(e -> 
                        Toast.makeText(this, 
                            "Contraseña actual incorrecta", 
                            Toast.LENGTH_SHORT).show());
        }
    }

    private void mostrarDialogoBorrarDatos() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_borrar_datos, null);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);

        new AlertDialog.Builder(this)
                .setTitle("Borrar Cuenta")
                .setView(view)
                .setPositiveButton("Borrar", (dialog, which) -> {
                    String password = editTextPassword.getText().toString();
                    if (password.isEmpty()) {
                        Toast.makeText(this, "Por favor ingresa tu contraseña", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null && user.getEmail() != null) {
                        // Reautenticar usuario
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                        user.reauthenticate(credential)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Reautenticación exitosa", Toast.LENGTH_SHORT).show();
                                    // Primero borrar datos del usuario en Firestore
                                    FirebaseFirestore.getInstance()
                                        .collection("usuarios")
                                        .document(user.getUid())
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Datos de usuario en Firestore borrados", Toast.LENGTH_SHORT).show();
                                            // Luego borrar datos de eventos del usuario
                                            FirebaseFirestore.getInstance()
                                                .collection("eventos")
                                                .whereEqualTo("usuarioId", user.getUid())
                                                .get()
                                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                                    Toast.makeText(this, "Eventos obtenidos: " + queryDocumentSnapshots.size(), Toast.LENGTH_SHORT).show();
                                                    List<Task<Void>> deleteTasks = new ArrayList<>();
                                                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                                                        deleteTasks.add(document.getReference().delete());
                                                    }
                                                    Tasks.whenAll(deleteTasks)
                                                        .addOnSuccessListener(aVoid1 -> {
                                                            Toast.makeText(this, "Todos los eventos borrados", Toast.LENGTH_SHORT).show();
                                                            // Ahora borrar el usuario de Firebase Authentication
                                                            user.delete()
                                                                .addOnCompleteListener(deleteTask -> {
                                                                    if (deleteTask.isSuccessful()) {
                                                                        Toast.makeText(this, "Usuario de Firebase Authentication borrado", Toast.LENGTH_SHORT).show();
                                                                        // Borrar SharedPreferences
                                                                        preferences.edit().clear().apply();
                                                                        // Borrar caché de la aplicación
                                                                        try {
                                                                            File cacheDir = getCacheDir();
                                                                            File appDir = new File(cacheDir.getParent());
                                                                            if (appDir.exists()) {
                                                                                String[] children = appDir.list();
                                                                                for (String s : children) {
                                                                                    if (!s.equals("lib")) {
                                                                                        deleteDir(new File(appDir, s));
                                                                                    }
                                                                                }
                                                                            }
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        // Cerrar sesión
                                                                        mAuth.signOut();
                                                                        // Reiniciar switch de notificaciones
                                                                        switchNotificaciones.setChecked(true);
                                                                        Toast.makeText(this, 
                                                                            "Cuenta y datos borrados correctamente", 
                                                                            Toast.LENGTH_LONG).show();
                                                                        // Volver a la pantalla de login
                                                                        Intent intent = new Intent(this, LoginActivity.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(this, 
                                                                            "Error al borrar la cuenta: " + deleteTask.getException().getMessage(), 
                                                                            Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(this, "Error al borrar los eventos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                        });
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(this, "Error al obtener los eventos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                });
                                        });
                                } else {
                                    Toast.makeText(this, 
                                        "Contraseña incorrecta o reautenticación fallida", 
                                        Toast.LENGTH_LONG).show();
                                }
                            });
                    } else {
                        // Si no hay usuario, solo borrar datos locales
                        preferences.edit().clear().apply();
                        switchNotificaciones.setChecked(true);
                        Toast.makeText(this, 
                            "Datos borrados correctamente", 
                            Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
} 