package com.example.foodhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodhub.repository.UserRepository;
import com.example.foodhub.util.HashUtils;

/**
 * Actividad de inicio de sesión de usuario.
 * Permite loguearse, registrarse o ir a la configuración de la cuenta.
 */
public class LoginActivity extends AppCompatActivity {

    // Campos de entrada y botones
    EditText editUsername, editPassword;
    Button buttonLogin, buttonRegister, buttonAccountSettings;

    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonAccountSettings = findViewById(R.id.buttonAccountSettings);

        // Inicializar repositorio de usuario
        userRepository = new UserRepository(this);

        // Listener para botón de inicio de sesión
        buttonLogin.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // Validación de campos
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar credenciales
            String hashedPasswordFromDB = userRepository.getHashedPassword(username);

            if (hashedPasswordFromDB != null && HashUtils.checkPassword(password, hashedPasswordFromDB)) {
                Toast.makeText(this, "Bienvenido, " + username, Toast.LENGTH_SHORT).show();

                // Guardar sesión del usuario (opcional, pero necesario para eliminar cuenta después)
                SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.apply();

                // Ir a pantalla principal
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener para ir a pantalla de registro
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Listener para ir a configuración de cuenta
        buttonAccountSettings.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, AccountSettingsActivity.class);
            startActivity(intent);
        });
    }
}
