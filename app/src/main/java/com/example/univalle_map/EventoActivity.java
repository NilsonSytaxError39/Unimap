package com.example.univalle_map;

import static java.lang.System.currentTimeMillis;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private EventoAdapter adapter;
    private List<Evento> listaEventos;
    private FloatingActionButton fabAgregar;

    private SharedPreferences preferences;
    private Handler handler = new Handler();
    private Runnable notificacionRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        // Crear canal de notificación (solo para Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "eventos_channel",
                    "Notificaciones de Eventos",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Canal para notificaciones de eventos");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        preferences = getSharedPreferences("UniMapPrefs", MODE_PRIVATE);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Configurar Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Eventos");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewEventos);
        fabAgregar = findViewById(R.id.fabAgregarEvento);

        // Lista de eventos
        listaEventos = new ArrayList<>();
        listaEventos.add(new Evento("Feria de Innovación", "Muestra de proyectos estudiantiles", "10 de mayo","09:00 AM","Sede Villa - Cancha1/Zona Eventos"));
        listaEventos.add(new Evento("Charla de IA", "Conferencia sobre inteligencia artificial", "15 de mayo","10:00AM","Sede Villa - Salon 6 - Eventos"));
        listaEventos.add(new Evento("Hackathon UV", "Competencia de desarrollo de software", "20 de mayo","08:00AM","Sede Principe - Sala De Sistemas 1"));
        listaEventos.add(new Evento("Semana Cultural", "Actividades artísticas y presentaciones musicales", "25 de mayo","09:00AM","Sede Principe - Cancha/Zona eventos - Pasillos"));
        listaEventos.add(new Evento("Foro Ambiental", "Debate sobre sostenibilidad y medio ambiente", "30 de mayo","02:00PM","Sede Villa - Salon 6 - Eventos"));
        listaEventos.add(new Evento("Torneo Deportivo", "Competencias entre facultades", "5 de junio","03:00PM","Sede Villa - Cancha1/Zona Eventos"));
        listaEventos.add(new Evento("Feria de Emprendimiento", "Estudiantes presentan sus ideas de negocio", "10 de junio","08:00AM","Sede Principe - Cancha/Zona eventos - Pasillos"));
        listaEventos.add(new Evento("Taller de Primeros Auxilios", "Capacitación en emergencias básicas", "12 de junio","09:00AM","Sede Principe - Enfermeria"));
        listaEventos.add(new Evento("Festival de Cine Universitario", "Proyección de cortos hechos por estudiantes", "15 de junio","6:00PM","Sede Villa - Salon 6 - Eventos"));

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventoAdapter(listaEventos);
        recyclerView.setAdapter(adapter);

        // Definir Runnable de notificaciones
        notificacionRunnable = new Runnable() {
            @Override
            public void run() {
                boolean notificacionesActivas = preferences.getBoolean("notificaciones", true);
                if (notificacionesActivas && !listaEventos.isEmpty()) {
                    Evento eventoAleatorio = listaEventos.get(new Random().nextInt(listaEventos.size()));
                    mostrarNotificacion(eventoAleatorio);
                    handler.postDelayed(this, 300000); // 5 minutos
                }
            }
        };

        // Iniciar Runnable si está activado
        if (preferences.getBoolean("notificaciones", true)) {
            handler.postDelayed(notificacionRunnable, 10000); // Inicia a los 10 segundos
        }

        // Acción del botón flotante
        fabAgregar.setOnClickListener(v ->
                Toast.makeText(EventoActivity.this, "Agregar evento (próximamente)", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean notificacionesActivas = preferences.getBoolean("notificaciones", true);
        handler.removeCallbacks(notificacionRunnable);

        if (notificacionesActivas) {
            handler.postDelayed(notificacionRunnable, 10000); // Se reinicia cuando vuelve
        }
    }

    private void mostrarNotificacion(Evento evento) {
        String titulo = "Evento destacado: " + evento.getTitulo();
        String mensaje = evento.getDescripcion() +
                "\n📅 " + evento.getFecha() +
                " 🕒 " + evento.getHora() +
                "\n📍 " + evento.getLugar();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "eventos_channel")
                .setSmallIcon(R.drawable.ic_menu_event)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(mensaje))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        notificationManager.notify((int) (System.currentTimeMillis() % Integer.MAX_VALUE), builder.build());
    }
}


