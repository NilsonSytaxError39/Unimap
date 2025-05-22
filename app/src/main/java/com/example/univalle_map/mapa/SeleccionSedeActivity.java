package com.example.univalle_map.mapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.univalle_map.R;
import androidx.appcompat.app.AppCompatActivity;

public class SeleccionSedeActivity extends AppCompatActivity {

    Button btnTuluav, btnTuluap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_sede);

        btnTuluav = findViewById(R.id.btnTuluav);
        btnTuluap = findViewById(R.id.btnTuluap);

        btnTuluav.setOnClickListener(view -> abrirMapa(4.070448419104303, -76.19046505056319, "Sede Tulua - VillaCampestre"));
        btnTuluap.setOnClickListener(view -> abrirMapa(4.071954, -76.203337, "Sede Tulua - Principe"));
    }

    private void abrirMapa(double lat, double lon, String nombreSede) {
        Intent intent = new Intent(this, MapaOSMActivity.class);
        intent.putExtra("latitud", lat);
        intent.putExtra("longitud", lon);
        intent.putExtra("nombreSede", nombreSede);

        boolean esInvitado = getIntent().getBooleanExtra("modo_invitado", false);
        intent.putExtra("modo_invitado", esInvitado);

        startActivity(intent);
    }
}
