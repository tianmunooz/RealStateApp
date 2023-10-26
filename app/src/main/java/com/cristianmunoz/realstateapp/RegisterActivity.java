package com.cristianmunoz.realstateapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextNombre, editTextApellido, editTextFechaNacimiento, editTextContraseña, editTextRepetirContraseña;
    private Button btnCrearCuenta;
    private Usuario usuarioDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextFechaNacimiento = findViewById(R.id.editTextFechaNacimiento);
        editTextContraseña = findViewById(R.id.editTextContraseña);
        editTextRepetirContraseña = findViewById(R.id.editTextRepetirContraseña);
        btnCrearCuenta = findViewById(R.id.btn_crear);

        usuarioDb = new Usuario(this);

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String fechaNacimiento = editTextFechaNacimiento.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();
        String repetirContraseña = editTextRepetirContraseña.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) ||
                TextUtils.isEmpty(fechaNacimiento) || TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(repetirContraseña)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        } else if (!contraseña.equals(repetirContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else {
            // Intenta agregar el usuario a la base de datos
            boolean exitoRegistro = usuarioDb.agregarUsuario(email, nombre, apellido, fechaNacimiento, contraseña);

            if (exitoRegistro) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Puedes redirigir a la actividad de inicio de sesión o hacer otras acciones aquí
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();  // Para cerrar la actividad actual y evitar que el usuario regrese al registro
            } else {
                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
