package com.example.univalle_map;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import android.widget.Toast;
import android.widget.GridLayout;

import com.example.univalle_map.mapa.MapaOSMActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.univalle_map.login.LoginActivity;
import com.example.univalle_map.login.Usuario;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.File;

public class MenuPrincipal extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView txtNombreUsuario;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            // Inicializar Firebase
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            
            // Verificar autenticación solo si no es invitado
            boolean esInvitado = getIntent().getBooleanExtra("modo_invitado", false);
            
            setContentView(R.layout.activity_menu_principal);
            
            // Inicializar vistas
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
            
            // Configurar Navigation Drawer
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, 
                    R.string.navigation_drawer_open, 
                    R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            
            navigationView.setNavigationItemSelectedListener(this);
            
            // Configurar clicks en las tarjetas
            setupCardClicks();
            
            // Ocultar tarjeta de eventos si es invitado
            if (esInvitado) {
                CardView cardEvento = findViewById(R.id.cardEvento);
                if (cardEvento != null) cardEvento.setVisibility(View.GONE);

                // Ocultar el espacio vacío si existe
                View spaceGrid = findViewById(R.id.spaceGrid);
                if (spaceGrid != null) spaceGrid.setVisibility(View.GONE);
                
                // Configurar nombre de usuario para invitado
                txtNombreUsuario.setText("Modo Invitado");
            } else {
                // Verificar autenticación solo si no es invitado
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    mAuth.signOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                // Cargar datos del usuario solo si no es invitado
                cargarDatosUsuario();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al inicializar el menú principal", Toast.LENGTH_LONG).show();
            // Redirigir al login en caso de error
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosUsuario();
    }

    private void setupCardClicks() {
        CardView cardMapa = findViewById(R.id.cardMapa);
        CardView cardPerfil = findViewById(R.id.cardPerfil);
        CardView cardConfiguracion = findViewById(R.id.cardConfiguracion);
        CardView cardEvento = findViewById(R.id.cardEvento);
        CardView cardCerrarSesion = findViewById(R.id.cardCerrarSesion);

        cardMapa.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, com.example.univalle_map.mapa.SeleccionSedeActivity.class);
            startActivity(intent);
        });

        cardPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, PerfilActivity.class);
            if (getIntent().getBooleanExtra("modo_invitado", false)) {
                intent.putExtra("modo_invitado", true);
            }
            startActivity(intent);
        });

        cardConfiguracion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, ConfiguracionActivity.class);
            if (getIntent().getBooleanExtra("modo_invitado", false)) {
                intent.putExtra("modo_invitado", true);
            }
            startActivity(intent);
        });

        cardEvento.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, EventoActivity.class);
            startActivity(intent);
        });

        cardCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void cargarDatosUsuario() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mDatabase.child("usuarios").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            if (usuario != null) {
                                txtNombreUsuario.setText(usuario.getNombre());
                                
                                // Actualizar header del Navigation Drawer
                                View headerView = navigationView.getHeaderView(0);
                                TextView navNombre = headerView.findViewById(R.id.nav_header_nombre);
                                TextView navEmail = headerView.findViewById(R.id.nav_header_email);
                                CircleImageView navImage = headerView.findViewById(R.id.nav_header_image);
                                
                                navNombre.setText(usuario.getNombre());
                                navEmail.setText(usuario.getEmail());
                                
                                // Cargar imagen de perfil si existe
                                if (usuario.getFotoUrl() != null) {
                                    File imageFile = new File(usuario.getFotoUrl());
                                    if (imageFile.exists()) {
                                        Glide.with(MenuPrincipal.this)
                                                .load(imageFile)
                                                .placeholder(R.drawable.ic_person)
                                                .error(R.drawable.ic_person)
                                                .into(navImage);
                                    } else {
                                        // Si el archivo no existe, mostrar imagen por defecto
                                        navImage.setImageResource(R.drawable.ic_person);
                                    }
                                } else {
                                    // Si no hay URL de foto, mostrar imagen por defecto
                                    navImage.setImageResource(R.drawable.ic_person);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Manejar error
                        }
                    });
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.nav_mapa) {
            Intent intent = new Intent(MenuPrincipal.this, com.example.univalle_map.mapa.SeleccionSedeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(MenuPrincipal.this, PerfilActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_configuracion) {
            Intent intent = new Intent(MenuPrincipal.this, ConfiguracionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cerrar_sesion) {
            cerrarSesion();
        }
        
        drawerLayout.closeDrawers();
        return true;
    }

    private void cerrarSesion() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
} 