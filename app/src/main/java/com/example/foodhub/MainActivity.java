package com.example.foodhub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // Declaración de vistas y variables necesarias
    private EditText editTextBuscar;
    private Button buttonBuscar;
    private TextView textViewResultado;
    private TextView textViewInstrucciones;
    private ImageView imageViewComida;
    private Button buttonLogout;

    // Repositorio para consultar la API de comidas
    private MealRepository mealRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de vistas de la interfaz
        editTextBuscar = findViewById(R.id.editTextBuscar);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        textViewResultado = findViewById(R.id.textViewResultado);
        textViewInstrucciones = findViewById(R.id.textViewInstrucciones);
        imageViewComida = findViewById(R.id.imageViewComida);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Inicialización del repositorio de datos
        mealRepository = new MealRepository();

        // Listener para botón de búsqueda
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarComida();  // Llamada al método de búsqueda
            }
        });

        // Listener para cerrar sesión
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpia la sesión del usuario
                SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

                // Redirecciona al LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Cierra MainActivity
            }
        });
    }

    // Método principal para buscar una comida
    private void buscarComida() {
        String comida = editTextBuscar.getText().toString().trim();
        if (comida.isEmpty()) {
            Toast.makeText(this, "Ingresa un nombre de comida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamada a la API usando Retrofit
        Call<MealResponse> call = mealRepository.searchMeals(comida);
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MealResponse mealResponse = response.body();

                    // Verifica si se obtuvieron resultados
                    if (mealResponse.getMeals() != null && !mealResponse.getMeals().isEmpty()) {
                        Meal meal = mealResponse.getMeals().get(0);

                        // Muestra el nombre del platillo
                        textViewResultado.setText("Nombre: " + meal.getStrMeal());

                        // Carga la imagen con Glide
                        Glide.with(MainActivity.this)
                                .load(meal.getStrMealThumb())
                                .into(imageViewComida);

                        // Traduce las instrucciones al español
                        String instruccionesEnIngles = meal.getStrInstructions();
                        traducirInstrucciones(instruccionesEnIngles);

                        Toast.makeText(MainActivity.this, "Comida encontrada: " + meal.getStrMeal(), Toast.LENGTH_SHORT).show();
                    } else {
                        // Si no se encontraron resultados
                        Toast.makeText(MainActivity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                        textViewResultado.setText("");
                        textViewInstrucciones.setText("");
                        imageViewComida.setImageResource(0);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    Log.e("API_RESPONSE", "Error en la respuesta: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                // Error de red o de conexión con la API
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Error en la llamada: ", t);
            }
        });
    }

    // Método para traducir instrucciones usando un script de Google Apps
    private void traducirInstrucciones(String instruccionesEnIngles) {
        String url = "https://script.google.com/macros/s/AKfycbyxz6pACnjTzNiL62n8p-2SlYDSwdaoWDlMwtEdvAtbP8gUBBAMzLm6MYtjyM2T0CRZ/exec?text="
                + instruccionesEnIngles + "&source=en&target=es";

        // Se ejecuta en segundo plano para no bloquear la UI
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String result = getHttpResponse(url);  // Obtiene el texto traducido
            runOnUiThread(() -> updateInstructions(result)); // Actualiza UI en el hilo principal
        });
    }

    // Realiza la solicitud HTTP y obtiene la respuesta en formato String
    private String getHttpResponse(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // Lee la respuesta línea por línea
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            Log.e("TranslateTextTask", "Error traduciendo", e);
        }
        return result.toString();
    }

    // Parsea el JSON recibido y muestra el texto traducido
    private void updateInstructions(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String translatedText = jsonObject.getString("translatedText");
            textViewInstrucciones.setText(translatedText);
        } catch (Exception e) {
            Log.e("TranslateTextTask", "Error al parsear la respuesta", e);
        }
    }
}
