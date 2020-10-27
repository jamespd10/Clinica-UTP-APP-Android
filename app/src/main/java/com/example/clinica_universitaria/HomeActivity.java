package com.example.clinica_universitaria;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clinica_universitaria.ui.dashboard.DashboardFragment;
import com.example.clinica_universitaria.ui.home.HomeFragment;
import com.example.clinica_universitaria.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private SessionHandler session;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private static final String KEY_CITA = "cita";
    private static final String KEY_ID = "id";
    private static final String KEY_MOTIVO = "motivo";
    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_FECHA = "fecha";
    private static final String KEY_HORA = "hora";
    private String cita_url = "http://192.168.0.137/Clinica-UTP/php/db/android/cancelar.php";
    private String agregarCita_url = "http://192.168.0.137/Clinica-UTP/php/db/android/agregar.php";
    private Toolbar barHome;
    private EditText txtBFecha, txtBHora;
    private Button btnFecha, btnHora;
    private int dia, mes, year, hora, minutos;
    private LinearLayout fechaHora, linearMain;
    BottomNavigationView bottomNavigationView;
    EditText etMotivo, etDescripcion, etHora, etFecha;
    String motivo, descripcion, stringHora, stringFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        /*barHome.findViewById(R.id.actionbarHome);
        setSupportActionBar(barHome);
        getSupportActionBar().setTitle("action bar text");*/
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = findViewById(R.id.welcomeText);
        TextView cita = findViewById(R.id.cita);
        //para mostrar y ocultar los linear layouts
        fechaHora = findViewById(R.id.FechaHora);
        fechaHora.setVisibility(View.GONE);
        linearMain = findViewById(R.id.LinearMain);
        linearMain.setVisibility(View.VISIBLE);
        //fin de linear layouts principal y de fecha y hora
        final LinearLayout linearHora = findViewById(R.id.linearHora);
        Button cancelarCita = findViewById(R.id.btnCancelarCita);
        //para la fecha y hora
        btnFecha = findViewById(R.id.btnFecha);
        btnHora = findViewById(R.id.btnHora);
        txtBFecha = findViewById(R.id.txtBFecha);
        txtBHora = findViewById(R.id.txtBHora);

        //para mostrar los fragments
        if(savedInstanceState ==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        }
        mostrarFragments();
        //fin de mostrar fragments

        //welcomeText.setText("Bienvenido "+user.getFullName()+", su sesión expira en "+user.getSessionExpiryDate());
        welcomeText.setText(user.getFullName());
        if(user.getCita().equals("")){
            cita.setText("Sin Citas.");
            cancelarCita.setVisibility(View.GONE);
            linearHora.setVisibility(View.GONE);
        }
        else{
            //cita.setText("Tiene una cita agendada para el día:  "+user.getCita()+"\n\nID_USER: "+user.getId());
            cancelarCita.setVisibility(View.VISIBLE);
            String[] parts;
            parts = user.getCita().split("#");;
            cita.setText(parts[0]);
            TextView hora = findViewById(R.id.horas);
            hora.setText(parts[1]);
        }

        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }
    /*public void onBackPressed(){
        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setMessage("Desea salir");
        myBuild.setTitle("sexo anal");
        myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        myBuild.setNegativeButton("chao ps", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = myBuild.create();
        dialog.show();
        session.logoutUser();
        finish();
    }*/

    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    public void CancelarCita(View view){
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        //TextView cita = findViewById(R.id.cita);
        //Button btn = findViewById(R.id.btnCancelarCita);
        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setMessage("¿Está seguro que desea cancelar la cita?");
        myBuild.setTitle("Cancelar Cita");
        if(user.getCita().equals("")){
            Toast.makeText(this,"No se pudo cancelar la cita", Toast.LENGTH_SHORT).show();
        }
        else{
            myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Button btn = findViewById(R.id.btnCancelarCita);
                    TextView cita = findViewById(R.id.cita);
                    cita.setText("No tiene citas agendadas.");
                    LinearLayout linearHora = findViewById(R.id.linearHora);
                    linearHora.setVisibility(View.GONE);
                    Button cancelarCita = findViewById(R.id.btnCancelarCita);
                    cancelarCita.setVisibility(View.GONE);
                    //Toast.makeText(DashboardActivity.this,"Cita cancelada correctamente", Toast.LENGTH_SHORT).show();
                    /**********************************************************************************************************/
                    JSONObject request = new JSONObject();
                    session = new SessionHandler(getApplicationContext());
                    User user = session.getUserDetails();
                    try {
                        //Populate the request parameters
                        request.put(KEY_ID, user.getId());
                        request.put(KEY_CITA, user.getCita());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                            (Request.Method.POST, cita_url, request, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    session = new SessionHandler(getApplicationContext());
                                    User user = session.getUserDetails();
                                    try {
                                        //Check if user got logged in successfully

                                        if (response.getInt(KEY_STATUS) == 0) {
                                            session.loginUser(user.getUsername(),user.getFullName(),"", user.getId());
                                            Toast.makeText(HomeActivity.this,"Cita cancelada correctamente", Toast.LENGTH_SHORT).show();
                                            notificacionAdd("Cita Cancelada Correctamente",
                                                    "Se canceló su cita, puede agendar una cita nuevamente");
                                        }else{
                                            Toast.makeText(HomeActivity.this,"No se pudo cancelar la cita", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    //Display error message whenever an error occurs
                                    Toast.makeText(getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();
                                    //Toast.makeText(DashboardActivity.this,"hola", Toast.LENGTH_SHORT).show();

                                }
                            });
                    MySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsArrayRequest);
                    /**********************************************************************************************************/
                }
            });
            myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(HomeActivity.this,"No se canceló la cita", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog = myBuild.create();
            dialog.show();

        }
    }

    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    public void mostrarFecha(View view){
        final Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtBFecha.setText(year+"/"+(month+1)+"/"+dayOfMonth);
            }
        }, year, mes, dia);
        datePickerDialog.show();
    }
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    public void mostrarHora(View view){
        final Calendar calendar = Calendar.getInstance();
        hora = calendar.get(Calendar.HOUR);
        minutos = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtBHora.setText(hourOfDay+":"+minute);
            }
        },hora,minutos,false);
        timePickerDialog.show();
    }
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    public void mostrarFragments(){
        BottomNavigationView navView = findViewById(R.id.nav_view_home);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Button cancelarCita = findViewById(R.id.btnCancelarCita);
                TextView txthoras = findViewById(R.id.horas);
                TextView txtcitas = findViewById(R.id.cita);
                LinearLayout linearHora = findViewById(R.id.linearHora);
                session = new SessionHandler(getApplicationContext());
                User user = session.getUserDetails();

                Fragment fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        fechaHora.setVisibility(View.GONE);
                        linearMain.setVisibility(View.VISIBLE);
                        if(user.getCita().equals("")){
                            cancelarCita.setVisibility(View.GONE);
                            linearHora.setVisibility(View.GONE);
                        }
                        else{
                            String[] parts;
                            parts = user.getCita().split("#");

                            cancelarCita.setVisibility(View.VISIBLE);
                            txthoras.setText(parts[1]);
                            linearHora.setVisibility(View.VISIBLE);
                            txtcitas.setText(parts[0]);
                        }
                        break;
                    case R.id.navigation_dashboard:
                        fragment = new DashboardFragment();
                        fechaHora.setVisibility(View.VISIBLE);
                        linearMain.setVisibility(View.GONE);
                        break;
                    case R.id.navigation_notifications:
                        fragment = new NotificationsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        });
    }
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    public void agregarCita(View view){
        etMotivo = findViewById(R.id.etMotivo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.txtBFecha);
        etHora = findViewById(R.id.txtBHora);
        motivo = etMotivo.getText().toString().toLowerCase().trim();
        descripcion = etDescripcion.getText().toString().trim();
        stringFecha = etFecha.getText().toString().trim();
        stringHora = etHora.getText().toString().trim();
        if (validateInputs()) {
            enviarCita();
        }
    }

    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/

    public void enviarCita(){

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        EditText txtBotonFecha = findViewById(R.id.txtBFecha);
        EditText txtBotonHora = findViewById(R.id.txtBHora);
        final String varFecha, varHora;
        varFecha = txtBotonFecha.getText().toString();
        varHora = txtBotonHora.getText().toString();

        //TextView cita = findViewById(R.id.cita);
        //Button btn = findViewById(R.id.btnCancelarCita);
        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setMessage("¿Está seguro que desea agregar la cita en la fecha y hora seleccionada?");
        myBuild.setTitle("Agregar Cita");
        //para saber si ya tiene una cita agendada
        if(user.getCita().equals("")){
            myBuild.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    JSONObject request = new JSONObject();
                    session = new SessionHandler(getApplicationContext());
                    User user = session.getUserDetails();
                    try {
                        //Populate the request parameters
                        request.put(KEY_ID, user.getId());
                        request.put(KEY_CITA, user.getCita());
                        request.put(KEY_FECHA,varFecha);
                        request.put(KEY_HORA,varHora);
                        request.put(KEY_MOTIVO,motivo);
                        request.put(KEY_DESCRIPCION,descripcion);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                            (Request.Method.POST, agregarCita_url, request, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    session = new SessionHandler(getApplicationContext());
                                    User user = session.getUserDetails();
                                    try {
                                        //Check if user got logged in successfully

                                        if (response.getInt(KEY_STATUS) == 0) {
                                            session.loginUser(user.getUsername(),user.getFullName(),varFecha+"#"+varHora, user.getId());
                                            Toast.makeText(HomeActivity.this,"Cita agregada correctamente", Toast.LENGTH_SHORT).show();
                                            notificacionAdd("Cita Agendada Correctamente",
                                                    "Se agendó su cita para el "+varFecha+" a las "+varHora);
                                        }else{
                                            Toast.makeText(HomeActivity.this,"No se pudo agregar la cita", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    //Display error message whenever an error occurs
                                    Toast.makeText(getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();
                                    //Toast.makeText(DashboardActivity.this,"hola", Toast.LENGTH_SHORT).show();

                                }
                            });
                    MySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsArrayRequest);
                    /**********************************************************************************************************/
                }
            });
            myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(HomeActivity.this,"No se agregó la cita", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog = myBuild.create();
            dialog.show();
        }
        else{
            Toast.makeText(this,"No se pudo agregar la cita, ya tiene una agendada", Toast.LENGTH_SHORT).show();
        }
    }

    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/

    private boolean validateInputs() {
        if(KEY_EMPTY.equals(stringFecha)){
            //etFecha.setError("Debe seleccionar una fecha");
            Toast.makeText(HomeActivity.this,"Debe seleccionar una fecha", Toast.LENGTH_SHORT).show();
            //etFecha.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(stringHora)){
            //etHora.setError("Debe seleccionar una hora");
            Toast.makeText(HomeActivity.this,"Debe seleccionar una hora", Toast.LENGTH_SHORT).show();
            //etHora.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(motivo)){
            etMotivo.setError("El motivo no debe estar vacío");
            etMotivo.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(descripcion)){
            etDescripcion.setError("La descripción no debe estar vacía");
            etDescripcion.requestFocus();
            return false;
        }
        return true;
    }

    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    /****************************************************************************************************************/
    public void notificacionAdd(String titulo, String cuerpo){
        //Map<String, String> data = remoteMessage.getData();
        String title = titulo;
        String body = cuerpo;

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "queloqueeee";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Solo para android Oreo o superior
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Mi notificacion",
                    NotificationManager.IMPORTANCE_MAX
            );

            // Configuracion del canal de notificacion
            channel.setDescription("xcheko51x channel para app");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            //channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            //channel.enableVibration(true);

            manager.createNotificationChannel(channel);

        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                0
        );
        Intent snoozeIntent=new Intent(this, MainActivity.class);
        snoozeIntent.setAction("hola");
        snoozeIntent.putExtra("hola", 0);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(this, 1, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_utp)
                .setTicker("Hearty365")
                //.setTimeoutAfter(1000)
                .setContentTitle(title)
                .setContentText(body)
                //.setVibrate(new long[]{0, 1000, 500, 1000})
                .setContentIntent(pendingIntent)
                .setContentInfo("info")
                .addAction(R.drawable.logo_utp, getString(R.string.pushNotificaciones), snoozePendingIntent);

        manager.notify(1, builder.build());
    }
}