package com.example.rafael.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Princpal extends AppCompatActivity {

    private ImageView imgLogo;
    private TextView lblFecha;
    private EditText txtFecha;
    private Calendar Calcular;
    private FloatingActionButton btnAyuda;
    private  Calendar cal = Calendar.getInstance();
    private Date date = cal.getTime();
    private Button btnGuardar;

    private Animation anim_rotar;

    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    Calendar C = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_princpal);

        this.txtFecha = findViewById(R.id.txtFechaN);
        this.lblFecha = findViewById(R.id.lblFechaNa);
        this.imgLogo = findViewById(R.id.imgLogo);
        this.btnGuardar = findViewById(R.id.btnGuardar);
        this.btnAyuda = findViewById(R.id.btnYuda);

        SharedPreferences pref = getSharedPreferences("info",Context.MODE_PRIVATE);

        txtFecha.setText(pref.getString("fecha1",""));
        lblFecha.setText(pref.getString("fecha2","fecha que el bebe nacera"));

        anim_rotar = AnimationUtils.loadAnimation(Princpal.this, R.anim.rotar);
        imgLogo.startAnimation(anim_rotar);

        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);

        if (txtFecha.getText().toString().isEmpty()){
            btnGuardar.setVisibility(View.VISIBLE);
        }else {
            btnGuardar.setVisibility(View.INVISIBLE);
        }




        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgLogo.startAnimation(anim_rotar);
                if(!txtFecha.getText().toString().isEmpty()) return;
                    AlertDialog.Builder builder = new AlertDialog.Builder(Princpal.this);
                    builder.setIcon(R.mipmap.ic_launcher).
                            setTitle("Atencion").setMessage("La fecha de nacimiento del bebe es aproximado, no es exacta.\n" +
                            "Desea Calcularla?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            showDialog(DATE_ID);

                            imgLogo.startAnimation(anim_rotar);
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            imgLogo.startAnimation(anim_rotar);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(txtFecha.getText().toString().isEmpty())
                    Toast.makeText(Princpal.this, "Ingrese fecha", Toast.LENGTH_SHORT).show();
                String fecha1 = txtFecha.getText().toString();
                String fecha2 = lblFecha.getText().toString();
                SharedPreferences preferences = getSharedPreferences("info",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fecha1",fecha1);
                editor.putString("fecha2",fecha2);
                editor.commit();
                btnGuardar.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void colocar_fecha() {
        txtFecha.setText(mYearIni + "-" + (mMonthIni + 1) + "-" + mDayIni);
        Calcular = convertirACalendar(txtFecha.getText().toString());
        date = sumarMeses(Calcular.getTime(),-3,7,1);
        lblFecha.setText("El bebe nacera aproximadamente el :"+date.toString());


    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYearIni = year;
                    mMonthIni = monthOfYear;
                    mDayIni = dayOfMonth;
                    colocar_fecha();
                }

            };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYearIni, sMonthIni, sDayIni);
        }

        return null;
    }
    private Calendar convertirACalendar(String fecha){

        String[] fechArray = fecha.split("-");

        int anio = Integer.parseInt(fechArray[0]);

        int mes =  Integer.parseInt(fechArray[1]) - 1;

        int dia =  Integer.parseInt(fechArray[2]);

        Calendar c1 = new GregorianCalendar(anio, mes, dia);

        return c1;

    }

    private Date sumarMeses(Date fecha,int meses,int dias,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(calendar.MONTH,meses);
        calendar.add(calendar.DAY_OF_MONTH,dias);
        calendar.add(calendar.YEAR,year);
        return calendar.getTime();
    }



}
