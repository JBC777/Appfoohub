package com.example.foodhub;

// IMPORTACIONES DE COMPONENTES DE UI Y FUNCIONALIDADES
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodhub.entities.User;
import com.example.foodhub.repository.UserRepository;

/**
 * Actividad para registrar un nuevo usuario en la aplicación.
 */
public class RegisterActivity extends AppCompatActivity {

    // Elementos de la interfaz gráfica
    EditText editUsername, editPassword, editConfirmPassword;
    Button buttonRegister;

    // Repositorio para operaciones de base de datos relacionadas con usuarios
    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Asocia esta actividad con el layout activity_register.xml
        setContentView(R.layout.activity_register);

        // Referencias a los campos de entrada y botón del layout
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Inicializa el repositorio para trabajar con la base de datos de usuarios
        userRepository = new UserRepository(this);

        // Configura la lógica del botón "Registrar"
        buttonRegister.setOnClickListener(v -> {

            // Obtiene los valores ingresados por el usuario
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();

            // Validación: Verifica que ningún campo esté vacío
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación: Verifica que las contraseñas coincidan
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación: Verifica si el usuario ya existe en la base de datos
            if (userRepository.userExists(username)) {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                return;
            }

            // Inserta el nuevo usuario en la base de datos
            boolean inserted = userRepository.insertUser(new User(username, password));

            // Verifica si la inserción fue exitosa
            if (inserted) {
                Toast.makeText(this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                finish(); // Finaliza la actividad y vuelve a la anterior (Login)
            } else {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
