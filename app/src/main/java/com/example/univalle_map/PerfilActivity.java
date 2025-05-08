package com.example.univalle_map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.univalle_map.login.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final String TAG = "PerfilActivity";
    private static final int MAX_IMAGE_SIZE = 1024 * 1024; // 1MB
    
    private CircleImageView imagenPerfil;
    private TextInputEditText editTextNombre;
    private TextInputEditText editTextEmail;
    private MaterialButton btnGuardar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Uri imageUri;
    private AlertDialog loadingDialog;


    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                imageUri = result.getData().getData();
                if (imageUri != null) {
                    try {
                        // Mostrar la imagen seleccionada
                        Glide.with(this)
                            .load(imageUri)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(imagenPerfil);
                    } catch (Exception e) {
                        Toast.makeText(this, 
                            "Error al cargar la imagen", 
                            Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    );

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestPermission(),
        isGranted -> {
            if (isGranted) {
                abrirGaleria();
            } else {
                Toast.makeText(this, 
                    "Se necesita permiso para acceder a la galería", 
                    Toast.LENGTH_SHORT).show();
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Inicializar vistas
        imagenPerfil = findViewById(R.id.imagenPerfil);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnGuardar = findViewById(R.id.btnGuardar);
        ImageButton btnCambiarFoto = findViewById(R.id.btnCambiarFoto);

        // Crear diálogo de carga
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_loading);
        builder.setCancelable(false);
        loadingDialog = builder.create();

        // Cargar datos del usuario
        cargarDatosUsuario();

        // Configurar listeners
        btnCambiarFoto.setOnClickListener(v -> verificarPermisosGaleria());
        btnGuardar.setOnClickListener(v -> guardarCambios());

        // Ocultar campos si es invitado
        boolean esInvitado = getIntent().getBooleanExtra("modo_invitado", false);
        if (esInvitado) {
            if (editTextNombre != null) {
                ((View)editTextNombre.getParent().getParent()).setVisibility(View.GONE);
            }
            if (editTextEmail != null) {
                ((View)editTextEmail.getParent().getParent()).setVisibility(View.GONE);
            }
        }
    }

    private void verificarPermisosGaleria() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 y superior
            if (ContextCompat.checkSelfPermission(this, 
                Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                abrirGaleria();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Para Android 6.0 a 12
            if (ContextCompat.checkSelfPermission(this, 
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                abrirGaleria();
            }
        } else {
            // Para Android 5.1 y anterior
            abrirGaleria();
        }
    }

    private void abrirGaleria() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImageLauncher.launch(Intent.createChooser(intent, "Seleccionar imagen"));
        } catch (Exception e) {
            Log.e(TAG, "Error opening gallery: " + e.getMessage());
            Toast.makeText(this,
                    "Error al abrir la galería: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void cargarDatosUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            editTextEmail.setText(user.getEmail());

            mDatabase.child("usuarios").child(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            if (usuario != null) {
                                editTextNombre.setText(usuario.getNombre());
                                
                                if (usuario.getFotoUrl() != null) {
                                    File imageFile = new File(usuario.getFotoUrl());
                                    if (imageFile.exists()) {
                                        Glide.with(PerfilActivity.this)
                                                .load(imageFile)
                                                .placeholder(R.drawable.ic_person)
                                                .error(R.drawable.ic_person)
                                                .into(imagenPerfil);
                                    } else {
                                        // Si el archivo no existe, mostrar imagen por defecto
                                        imagenPerfil.setImageResource(R.drawable.ic_person);
                                    }
                                } else {
                                    // Si no hay URL de foto, mostrar imagen por defecto
                                    imagenPerfil.setImageResource(R.drawable.ic_person);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(PerfilActivity.this, 
                                "Error al cargar datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void guardarCambios() {
        if (imageUri == null) {
            actualizarPerfilConFoto(mAuth.getCurrentUser().getUid(), 
                editTextNombre.getText().toString().trim(), null);
            return;
        }

        loadingDialog.show();

        try {
            // Guardar imagen en el almacenamiento interno
            String fileName = "profile_" + mAuth.getCurrentUser().getUid() + ".jpg";
            String imagePath = guardarImagenEnAlmacenamientoInterno(imageUri, fileName);
            
            // Actualizar perfil con la ruta de la imagen local
            actualizarPerfilConFoto(mAuth.getCurrentUser().getUid(), 
                editTextNombre.getText().toString().trim(), 
                imagePath);

        } catch (Exception e) {
            loadingDialog.dismiss();
            Toast.makeText(this, 
                "Error al guardar la imagen: " + e.getMessage(), 
                Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String guardarImagenEnAlmacenamientoInterno(Uri imageUri, String fileName) throws Exception {
        InputStream imageStream = getContentResolver().openInputStream(imageUri);
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

        // Comprimir imagen
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageData = baos.toByteArray();

        // Guardar en almacenamiento interno
        File directory = getDir("profile_images", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        File imageFile = new File(directory, fileName);

        FileOutputStream fos = new FileOutputStream(imageFile);
        fos.write(imageData);
        fos.close();

        return imageFile.getAbsolutePath();
    }

    private void actualizarPerfilConFoto(String userId, String nombre, String imagePath) {
        DatabaseReference userRef = mDatabase.child("usuarios").child(userId);
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", nombre);
        if (imagePath != null) {
            updates.put("fotoUrl", imagePath);
        }

        userRef.updateChildren(updates)
            .addOnSuccessListener(aVoid -> {
                loadingDialog.dismiss();
                Toast.makeText(PerfilActivity.this, 
                    "Perfil actualizado correctamente", 
                    Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            })
            .addOnFailureListener(e -> {
                loadingDialog.dismiss();
                Toast.makeText(PerfilActivity.this, 
                    "Error al actualizar perfil: " + e.getMessage(), 
                    Toast.LENGTH_LONG).show();
            });
    }
} 