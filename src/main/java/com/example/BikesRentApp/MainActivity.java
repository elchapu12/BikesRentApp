package com.example.BikesRentApp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText et1;
    private TextView textView6;
    private RadioButton r1, r2, r3;
    private Switch switch1;
    private Button button, button2;
    private Spinner spinner;
    private CheckBox checkBox;
    private RadioGroup radioGroup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes
        et1 = findViewById(R.id.horas);
        textView6 = findViewById(R.id.precio);
        r1 = findViewById(R.id.mon);
        r2 = findViewById(R.id.ur);
        r3 = findViewById(R.id.elec);
        switch1 = findViewById(R.id.switch1);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        spinner = findViewById(R.id.spinner);
        checkBox = findViewById(R.id.checkBox);
        radioGroup = findViewById(R.id.tipos_bicicletas);

        // Configurar el spinner con métodos de pago
        String[] metodoPago = {"Efectivo", "Transferencia", "Tarjeta en cuotas"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, metodoPago);
        spinner.setAdapter(adaptador);

        // Configurar el evento del botón "Importe a abonar"
        button.setOnClickListener(view -> abonarImporte());

        // Configurar el evento del botón "Reiniciar aplicación"
        button2.setOnClickListener(view -> resetApp());
    }

    private void abonarImporte() {
        String hora = et1.getText().toString();

        // Validaciones
        if (!r1.isChecked() && !r2.isChecked() && !r3.isChecked()) {
            Toast.makeText(this, "Error: no se seleccionó un tipo de bicicleta", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hora.isEmpty()) {
            Toast.makeText(this, "Error: no se ingresó la cantidad de horas", Toast.LENGTH_SHORT).show();
            return;
        }

        double horaNum;
        try {
            horaNum = Double.parseDouble(hora);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: ingrese un número válido de horas", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calcular el costo en base al tipo de bicicleta
        if (r1.isChecked()) {
            horaNum *= 1000; // Precio por hora de bicicleta de montaña
        } else if (r2.isChecked()) {
            horaNum *= 2000; // Precio por hora de bicicleta urbana
        } else if (r3.isChecked()) {
            horaNum *= 4000; // Precio por hora de bicicleta eléctrica
        }

        // Agregar costo adicional si se selecciona casco adicional
        if (checkBox.isChecked()) {
            horaNum += horaNum * 0.20;
        }

        // Aplicar descuento o incremento según el método de pago
        String seleccion = spinner.getSelectedItem().toString();
        switch (seleccion) {
            case "Efectivo":
                horaNum -= horaNum * 0.20;
                break;
            case "Transferencia":
                horaNum -= horaNum * 0.10;
                break;
            case "Tarjeta en cuotas":
                horaNum += horaNum * 0.10;
                break;
            default:
                Toast.makeText(this, "Error: no se seleccionó un método de pago", Toast.LENGTH_SHORT).show();
                return;
        }

        // Formatear el resultado
        DecimalFormat df = new DecimalFormat("0.00");
        String resultado;
        if (switch1.isChecked()) {
            horaNum /= 1000; // Convertir a USD
            resultado = df.format(horaNum);
            textView6.setText("$" + resultado + " USD");
        } else {
            resultado = df.format(horaNum);
            textView6.setText("$" + resultado + " ARS");
        }
    }

    private void resetApp() {
        // Reiniciar todos los elementos de la interfaz
        radioGroup.clearCheck();
        checkBox.setChecked(false);
        et1.setText("");
        spinner.setSelection(0);
        textView6.setText("$0.00");
        switch1.setChecked(false);
        Toast.makeText(this, "La aplicación se ha reiniciado", Toast.LENGTH_SHORT).show();
    }
}