package com.example.univalle_map.mapa;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.univalle_map.R;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class MapaOSMActivity extends AppCompatActivity {
    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_osm);

        // Inicializar OSMDroid
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx,
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        // Obtener datos de la sede
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("latitud", 3.3753);
        double lon = intent.getDoubleExtra("longitud", -76.5320);
        String nombreSede = intent.getStringExtra("nombreSede");

        // Configurar mapa
        GeoPoint startPoint = new GeoPoint(lat, lon);
        map.getController().setZoom(21);
        map.getController().setCenter(startPoint);

        // Agregar marcador de sede principal
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(nombreSede != null ? nombreSede : "Universidad del Valle Sede Villa");
        map.getOverlays().add(marker);

        //Sede Principe
        // Agregar marcadores personalizados
        agregarMarcadorConInfo(
                new GeoPoint(4.072012469252898, -76.20329197512085),
                "Entrada",
                "Entrada principal de la sede.",
                R.drawable.entrada_icon,
                R.drawable.entrada2_uni
        );

        agregarMarcadorConInfo(
                new GeoPoint(4.071411873725529, -76.20312009679202),
                "Salón 13",
                "Aula de clases salón #13.",
                R.drawable.salones_icon,
                R.drawable.salon13_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071501714208497, -76.20308866483536),
                "Salón 12",
                "Aula de clases salon #12.",
                R.drawable.salones2_icon,
                R.drawable.salon12_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071595673010597, -76.2030878342005),
                "Salón 11",
                "Aula de clases salon #11.",
                R.drawable.salones3_icon,
                R.drawable.salon11_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071649742094265, -76.20305929871398),
                "Salón 10",
                "Aula de clases salon #10.",
                R.drawable.salones_icon,
                R.drawable.salon10_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.0716927133757945, -76.20301606590806),
                "Salón 9",
                "Aula de clases salon #9.",
                R.drawable.salones2_icon,
                R.drawable.salon9_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071742028966742, -76.2029953328458),
                "Salón 8",
                "Aula de clases salon #8.",
                R.drawable.salones3_icon,
                R.drawable.salon8_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071781799602409, -76.20302563501373),
                "Salón 7",
                "Aula de clases salon #7.",
                R.drawable.salones_icon,
                R.drawable.salon7_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071800889506829, -76.20307029084013),
                "Salón 6",
                "Aula de clases salon #6.",
                R.drawable.salones2_icon,
                R.drawable.salon6_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071810434458867, -76.20312132607032),
                "Salón 5",
                "Aula de clases salon #5.",
                R.drawable.salones3_icon,
                R.drawable.salon5_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071824751886714, -76.20316279219483),
                "Salón 4",
                "Aula de clases salon #4.",
                R.drawable.salones_icon,
                R.drawable.salon4_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071889975721471, -76.20317555100237),
                "Casilleros",
                "Se guardan objetos y bolsos \nde los estudiantes y visitantes.",
                R.drawable.casilleros_icon,
                R.drawable.casilleros_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071932928000002, -76.20323296563632),
                "Sala de sistemas 3",
                "Aula de sistemas \ncomputadores y redes.",
                R.drawable.sistemas_icon,
                R.drawable.salon3_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071942344279214, -76.20336238926916),
                "Salón 2",
                " Aula de clases  salon #2.",
                R.drawable.salones2_icon,
                R.drawable.salon2_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071900724227321, -76.20335764774894),
                "Salón 1",
                " Aula de clases  salon #1.",
                R.drawable.salones3_icon,
                R.drawable.salon1_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071764300525451, -76.20331589788422),
                "Monotireo Sistemas",
                "Informacion general y \nmonitoreo de las salas.",
                R.drawable.monitoreo_icon,
                R.drawable.monitoreo_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071554549286222, -76.20322940568116),
                "Cancha/Zona Eventos",
                "Se practican deportes variados y\n se realizan los eventos de la sede.",
                R.drawable.cancha_icon,
                R.drawable.cancha_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.07192179222517, -76.20310537756097),
                "Parqueadero motos",
                "Lugar donde se estacionan \nlos vehiculos tipo moto.",
                R.drawable.parkciclas_icon,
                R.drawable.parqueadero_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.0717992612527745, -76.20293678449298),
                "Baño Hombres",
                "Lugar donde se depositan \nlas necesidades los hombres.",
                R.drawable.banosh_icon,
                R.drawable.banosh_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071803276321584, -76.20297739225856),
                "Baño Mujeres",
                "Lugar donde se depositan \nlas necesidades las mujeres.",
                R.drawable.banosm_icon,
                R.drawable.banosm_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071738126916552, -76.20345044281062),
                "Cafeteria",
                "Lugar para compra de alimento \ny lugar para compartir.",
                R.drawable.cafeteria_icon,
                R.drawable.cafeteria_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071856023235516, -76.20350268422875),
                "Parqueadero ciclas",
                "Lugar para parquear vehiculos \ntipo bicicleta.",
                R.drawable.parkciclas_icon,
                R.drawable.parkciclas_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.072052667940068, -76.20311778725423),
                "Parqueadero Carros",
                "Lugar para parquear vehiculos \ntipo Carro.",
                R.drawable.parqueadero_icon,
                R.drawable.parkcarros_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071693869135564, -76.20284049735855),
                "Parqueadero Carros2",
                "Lugar para parquear vehiculos \ntipo Carro.",
                R.drawable.parqueadero_icon,
                R.drawable.parkcarros2_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071723589015835, -76.20293673814409),
                "Enfermeria/Psicologia\n/Odontologia",
                "Primeros auxilios, ayuda psicologia\ncitas odontologicas.",
                R.drawable.enfermeria_icon,
                R.drawable.enfermeria_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071777097792545, -76.20290589274194),
                "Bienestar Universitario",
                "Informacion general \npara los estudiantes",
                R.drawable.bienestar_icon,
                R.drawable.bienestar_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.071703024153608, -76.2033161052089),
                "Sala de sistemas 1",
                "Aula de sistemas \ncomputadores y redes.",
                R.drawable.sistemas_icon,
                R.drawable.salon3_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.07170987677266, -76.20337634340211),
                "Sala de sistemas 2",
                "Aula de sistemas \ncomputadores y redes.",
                R.drawable.sistemas_icon,
                R.drawable.salon3_uni
        );
        ///////////////////////
        // Sede Villacampestre
        agregarMarcadorConInfo(
                new GeoPoint(4.070542532239702, -76.19077235680992),
                "Portería",
                "Entrada principal de la sede.",
                R.drawable.porteria_icon,
                R.drawable.porteria_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.0704819468598, -76.19059834894499),
                "Entrada",
                "Entrada principal de la sede.",
                R.drawable.entrada_icon,
                R.drawable.entradav_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.07077013673845, -76.19078713106961),
                "Parqueadero motos",
                "Lugar donde se estacionan \nlos vehiculos tipo moto.",
                R.drawable.parkciclas_icon,
                R.drawable.parqueomoto_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070829084654607, -76.19058029153204),
                "Parqueadero carros",
                "Lugar para parquear vehiculos \ntipo Carro.",
                R.drawable.parqueadero_icon,
                R.drawable.parqueocarro_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.0701664461797336, -76.19031750581232),
                "Cafeteria",
                "Lugar para compra de alimento \ny lugar para compartir.",
                R.drawable.cafeteria_icon,
                R.drawable.cafeteria2_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070204671578937, -76.19045615465781),
                "Zona Verde Recreativa",
                "Espacio para divertirse,\nentretenerse y recrearse.",
                R.drawable.recreacion_icon,
                R.drawable.recreacion_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070456163273048, -76.19031399758708),
                "Zona Principal",
                "Espacio para estar primer piso, \njunto a zona de administrativos.",
                R.drawable.principal_icon,
                R.drawable.principal_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.0704845621747, -76.19046717301944),
                "Escaleras",
                "Escaleras que llevan hacia\nel segundo piso de la sede.",
                R.drawable.escaleras_icon,
                R.drawable.escaleras_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070240508430771, -76.19006498632737),
                "Cancha 1/Zona Eventos",
                "Se practican deportes variados y\nse realizan los eventos de la sede.",
                R.drawable.cancha_icon,
                R.drawable.canchav_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.07045612468998, -76.19005321431274),
                "Cancha 2/Zona Verde",
                "Se practican deportes en \nzona verde como futbol.",
                R.drawable.cancha2_icon,
                R.drawable.cancha2_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070244370772423, -76.19027308961216),
                "Zona encuentro / Plazoleta",
                "Reunion en caso de emergencia \ny actividades varias",
                R.drawable.encuentro_icon,
                R.drawable.encuentro_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070633212377961, -76.1905848343033),
                "Parqueadero ciclas villa",
                "Lugar para parquear vehiculos \ntipo bicicleta.",
                R.drawable.parkciclas_icon,
                R.drawable.parkciclasvilla_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070317769436133, -76.1906138888297),
                "Zona Verde/Deporte",
                "Lugar para realizar actividades \nfisicas.",
                R.drawable.sportzone2_icon,
                R.drawable.sportzone_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.0705859426383935, -76.19042124045178),
                "Biblioteca",
                "Se encuentran todos los libros \ny revistas disponibles.",
                R.drawable.biblioteca_icon,
                R.drawable.biblioteca_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.07031644479036, -76.19027007893445),
                "Salida Trasera",
                "Salida patio trasero\nal punto de encuentro.",
                R.drawable.salida_icon,
                R.drawable.salida_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070550685304029, -76.19025653088471),
                "Laboratorio de Electronica",
                "laboratorio de electronica\nde la sede.",
                R.drawable.labelec_icon,
                R.drawable.labelec_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070610746963224, -76.19018577995837),
                "Baño Hombres -\n Primer Piso",
                "Lugar donde se depositan \nlas necesidades los hombres.",
                R.drawable.banosh_icon,
                R.drawable.banos1_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070526660639088, -76.19019932800809),
                "Baño de mujeres -\n Primer piso",
                "Lugar donde se depositan \nlas necesidades las mujeres.",
                R.drawable.banosm_icon,
                R.drawable.banos2_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070571706885244, -76.19019330665266),
                "Casilleros",
                "Se guardan objetos y bolsos \nde los estudiantes y visitantes.",
                R.drawable.casilleros_icon,
                R.drawable.casillerosv_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070613013131195, -76.19022203130359),
                "Laboratorio de Alimentos",
                "laboratorio de alimentos\nde la sede.",
                R.drawable.labali_icon,
                R.drawable.labali_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070449649953597, -76.19022285354167),
                "Salón 6 - Eventos",
                " Aula de clases presentacion\n de eventos certificaciones,\n exposiciones.",
                R.drawable.salones3_icon,
                R.drawable.salon6v_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070420144766695, -76.19037241576522),
                "Sala de sistemas \n- Segundo Piso",
                "Aula de sistemas \ncomputadores y redes.",
                R.drawable.sistemas_icon,
                R.drawable.sistemasv_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070540233344599, -76.19045655794935),
                "Salón 5 - Segundo Piso",
                "Aula de clases salón #5.",
                R.drawable.salones_icon,
                R.drawable.salon5v_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070532435509936, -76.190410955546),
                "Salón 4 - Segundo Piso",
                "Aula de clases salon #4.",
                R.drawable.salones2_icon,
                R.drawable.salon4v_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070519439118657, -76.19036014143943),
                "Salón 3 - Segundo Piso",
                " Aula de clases salon #3.",
                R.drawable.salones3_icon,
                R.drawable.salon3v_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070510341644632, -76.19030802440703),
                "Salón 2 - Segundo Piso",
                "Aula de clases salón #2.",
                R.drawable.salones_icon,
                R.drawable.salon2v_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070503843448833, -76.19024808981979),
                "Salón 1 - Segundo Piso",
                "Aula de clases salón #1.",
                R.drawable.salones2_icon,
                R.drawable.salon1v_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070487775802265, -76.19021047028521),
                "Baño de mujeres -\n Segundo piso",
                "Lugar donde se depositan \nlas necesidades las mujeres.",
                R.drawable.banosm_icon,
                R.drawable.banom2_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070413217766478, -76.19023163251597),
                "Baño Hombres -\n Segundo Piso",
                "Lugar donde se depositan \nlas necesidades los hombres.",
                R.drawable.banosh_icon,
                R.drawable.banoh2_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070431630884115, -76.19046071638952),
                "Oficina - Autoevaluacion \ny calidad academica \n- Segundo piso",
                "Es la encargada de coordinar los\nasuntos de autoevaluación\ny acreditación de la Universidad.",
                R.drawable.autocali_icon,
                R.drawable.autocali_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070425447464912, -76.19041838508387),
                "Oficina - Facultad Ing. \nAlimentos y Facultad Salud \n- Segundo piso",
                "Es la encargada de coordinar\nlos asuntos de ing alimentos\ny nutricion y dietetica.",
                R.drawable.autocali_icon,
                R.drawable.ali_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070415479253728, -76.19031258985788),
                "Oficina - Facultad Ing.\nEmprendimiento e interna\ncionalizacion,Coordinacion\ne investigacion-Segundo\npiso",
                "Es la encargada de coordinar\nlos asuntos de ingenieria de \nsistemas,oportunidades de \nbecas intercambios internaci-\nonales, Actividades de apoyo \na la Gestión de grupos de \ninvestigacion.",
                R.drawable.autocali_icon,
                R.drawable.facuing_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.07032209649798, -76.19032611986717),
                "Ventanilla unica y\nSecretaria academica",
                "Es la encargada de gestionar \ndocumentos,gestionar informacion\ny diligencias sobre las carreras.",
                R.drawable.autocali_icon,
                R.drawable.secre_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.07050856395635, -76.19044213845939),
                "Oficina - Posgrados \ny Salud Ocupacional.",
                "Es la encargada de dinamizar y\narticular las ofertas de\nposgrados de excelencia,\nimplementar las acciones\nnecesarias para prevenir\ny controlar los riesgos\nen la salud de los funcionarios",
                R.drawable.autocali_icon,
                R.drawable.socu_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070366059815417, -76.19024643955811),
                "Oficina - Extensión y \nProyección Social / \nComunicaciones",
                "la universidad vincula la docencia\n y la investigación, al tratamiento\n y planteamiento de alternativas\n y soluciones para los retos y \nproblemas críticos de la región\n y el país ",
                R.drawable.autocali_icon,
                R.drawable.zona_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070336523644964, -76.19041446659486),
                "Oficina - Facultad Admini-\nstracion Facultad Humani-\ndades Facultad artes\nintegradas.",
                "Es la encargada de coordinar\nlos asuntos de administracion\nTrabajo Social y Construccion",
                R.drawable.autocali_icon,
                R.drawable.admin_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070548380084585, -76.19043549850214),
                "Oficina - Inventarios /\nInformatica y Telecomu-\nnicaciones OITEL.",
                "Es la encargada de la Adminis-\ntración de Bienes y Apoyar la\ngestión académica mediante\nla prestación de servicios de\ninformática y de tele-\ncomunicaciones",
                R.drawable.autocali_icon,
                R.drawable.oitel_uni
        );
        agregarMarcadorConInfo(
                new GeoPoint(4.070473165629893, -76.19044718031516),
                "Cuarto De Servidores",
                "Espacio donde se ubican\nlos servidores físicos y virtuales\nque permiten el funcionamiento\nde los servicios informáticos\nde la universidad ",
                R.drawable.autocali_icon,
                R.drawable.servidores_uni
        );

    }
    private void agregarMarcadorConInfo(
            GeoPoint posicion,
            String titulo,
            String descripcion,
            int iconoMarker,
            int imagenInfoWindow
    ) {

        final GeoPoint[] posicionActual = {posicion};

        Marker marker = new Marker(map);
        marker.setPosition(posicionActual[0]);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(titulo);
        marker.setIcon(getResources().getDrawable(iconoMarker));

        marker.setInfoWindow(new InfoWindow(R.layout.custom_info_window, map) {
            @Override
            public void onOpen(Object item) {
                View view = mView;
                ImageView image = view.findViewById(R.id.imgLugar);
                TextView txtTitulo = view.findViewById(R.id.txtTitulo);
                TextView txtDescripcion = view.findViewById(R.id.txtDescripcion);

                image.setImageResource(imagenInfoWindow);
                txtTitulo.setText(titulo);
                txtDescripcion.setText(descripcion);
            }

            @Override
            public void onClose() {}
        });
        marker.setOnMarkerClickListener((m, mapView) -> {
            if (m.isInfoWindowShown()) {
                m.closeInfoWindow();
            } else {
                m.showInfoWindow();

                View markerView = m.getInfoWindow().getView();
                if (markerView != null) {
                    markerView.setScaleX(0f);
                    markerView.setScaleY(0f);
                    markerView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                }

                animateMarkerJump(m);
            }
            return true;
        });
        map.getOverlays().add(marker);
    }
    private void animateMarkerJump(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 600;
        final GeoPoint startPoint = marker.getPosition();
        final double jumpHeight = 0.0003;

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(1 - (float) elapsed / duration, 0);
                double offset = jumpHeight * Math.sin(Math.PI * t);
                marker.setPosition(new GeoPoint(
                        startPoint.getLatitude() + offset,
                        startPoint.getLongitude()
                ));
                if (t > 0.01) {
                    handler.postDelayed(this, 16);
                } else {
                    marker.setPosition(startPoint);
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}

