package com.cristianmunoz.realstateapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextContrasena;
    private Button btnIniciarSesion, btnCrearCuenta;
    private Usuario usuarioDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextContrasena = findViewById(R.id.editTextContrasenaLogin);
        btnIniciarSesion = findViewById(R.id.btn_iniciarsesion);
        btnCrearCuenta = findViewById(R.id.btn_crearcuenta);

        usuarioDb = new Usuario(this);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad de registro
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarSesion() {
        String email = editTextEmail.getText().toString().trim();
        String contrasena = editTextContrasena.getText().toString().trim();

        if (email.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
        } else {
            // Intenta verificar las credenciales en la base de datos
            boolean credencialesCorrectas = usuarioDb.verificarUsuario(email, contrasena);

            if (credencialesCorrectas) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                // Puedes redirigir a la actividad principal o hacer otras acciones aquí
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Para cerrar la actividad actual y evitar que el usuario regrese al inicio de sesión
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
