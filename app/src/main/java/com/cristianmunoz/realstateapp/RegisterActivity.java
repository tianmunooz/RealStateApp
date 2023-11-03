package com.cristianmunoz.realstateapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextNombre, editTextApellido, editTextFechaNacimiento, editTextContraseña, editTextRepetirContraseña;
    private Button btnCrearCuenta;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Date format watcher
        editTextFechaNacimiento.addTextChangedListener(new DateFormatWatcher(editTextFechaNacimiento));
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        final String email = editTextEmail.getText().toString().trim();
        final String nombre = editTextNombre.getText().toString().trim();
        final String apellido = editTextApellido.getText().toString().trim();
        final String fechaNacimiento = editTextFechaNacimiento.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();
        String repetirContraseña = editTextRepetirContraseña.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) ||
                TextUtils.isEmpty(fechaNacimiento) || TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(repetirContraseña)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(repetirContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user with Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, now store additional details in Firestore
                            FirebaseUser user = mAuth.getCurrentUser(); // Get the newly created user

                            // Create a user object or a Map to store additional information
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("email", email);
                            userMap.put("nombre", nombre);
                            userMap.put("apellido", apellido);
                            userMap.put("fechaNacimiento", fechaNacimiento);

                            // Add a new document with the user's UID as the document ID
                            db.collection("users").document(user.getUid())
                                    .set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                                        // Redirect to Login Activity
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error al guardar datos del usuario.", Toast.LENGTH_SHORT).show());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Error de autenticación.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
