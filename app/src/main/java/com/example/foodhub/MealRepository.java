package com.example.foodhub;

// BLOQUE DE IMPORTACIONES NECESARIAS PARA RETROFIT
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase MealRepository
 * Encargada de manejar la lógica de acceso a datos desde la API externa (TheMealDB).
 * Implementa el patrón de repositorio para abstraer el origen de los datos.
 */
public class MealRepository {

    // URL base del endpoint de la API de TheMealDB
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    // Instancia de la interfaz que define los endpoints del servicio
    private final MealApiService mealApiService;

    /**
     * Constructor del repositorio
     * Configura Retrofit para realizar llamadas HTTP a la API
     * y crea la implementación de la interfaz MealApiService.
     */
    public MealRepository() {
        // Configuración de Retrofit con la URL base y un convertidor Gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Establece la URL base
                .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a objetos Java
                .build();

        // Crea la instancia del servicio API a partir de Retrofit
        mealApiService = retrofit.create(MealApiService.class);
    }

    /**
     * Método para buscar recetas por nombre
     * @param mealName Nombre del platillo a buscar
     * @return Llamada (Call) que devuelve una respuesta de tipo MealResponse
     */
    public Call<MealResponse> searchMeals(String mealName) {
        // Llama al método definido en la interfaz y retorna el resultado
        return mealApiService.searchMeals(mealName);
    }
}
