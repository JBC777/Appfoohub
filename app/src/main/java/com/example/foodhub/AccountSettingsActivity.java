package com.example.foodhub;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodhub.repository.UserRepository;
import com.example.foodhub.util.HashUtils;
import com.example.foodhub.entities.User;

/**
 * Pantalla de configuración de cuenta.
 * Permite actualizar la contraseña o eliminar la cuenta del usuario.
 */
public class AccountSettingsActivity extends AppCompatActivity {

    // Campos de entrada y botones
    private EditText editTextUsername;
    private EditText editTextNewPassword;
    private EditText editTextConfirmNewPassword;
    private Button buttonUpdatePassword;
    private Button buttonBackToLogin;
    private Button buttonDeleteAccount;

    private UserRepository userRepository; // Acceso a la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password); // Vista correspondiente

        // Inicializar campos de entrada
        editTextUsername = findViewById(R.id.editTextUsernameOrEmail);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmNewPassword = findViewById(R.id.editTextConfirmNewPassword);

        // Inicializar botones
        buttonUpdatePassword = findViewById(R.id.buttonUpdatePassword);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);
        buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);

        // Instanciar repositorio para operaciones de usuario
        userRepository = new UserRepository(this);

        // Listener para actualizar contraseña
        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        // Listener para volver a la pantalla de login
        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual
            }
        });

        // Listener para eliminar la cuenta del usuario
        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAccount();
            }
        });
    }

    /**
     * Actualiza la contraseña del usuario si todos los campos son válidos.
     */
    private void updatePassword() {
        String username = editTextUsername.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();

        // Validación básica
        if (username.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buscar usuario en la base de datos
        User user = userRepository.getUserByUsername(username);

        if (user != null) {
            // Hashear y actualizar contraseña
            String hashedPassword = HashUtils.hashPassword(newPassword);
            boolean updated = userRepository.updateUserPassword(user.getUsername(), hashedPassword);

            if (updated) {
                Toast.makeText(this, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se encontró ningún usuario con ese nombre de usuario", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Elimina la cuenta del usuario actual (según lo guardado en SharedPreferences).
     */
    private void deleteUserAccount() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", null);

        if (currentUsername != null) {
            // Confirmación mediante AlertDialog
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Cuenta")
                    .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción es irreversible.")
                    .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isDeleted = userRepository.deleteUser(currentUsername);

                            if (isDeleted) {
                                Toast.makeText(AccountSettingsActivity.this, "Cuenta eliminada exitosamente.", Toast.LENGTH_SHORT).show();

                                // Limpiar sesión guardada
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                // Finalizar actividad o redirigir a login
                                finish();
                            } else {
                                Toast.makeText(AccountSettingsActivity.this, "Error al eliminar la cuenta.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            Toast.makeText(this, "Error: No se pudo obtener el nombre de usuario para eliminar la cuenta.", Toast.LENGTH_SHORT).show();
        }
    }
}
