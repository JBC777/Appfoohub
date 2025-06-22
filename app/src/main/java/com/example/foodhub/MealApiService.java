package com.example.foodhub;

// BLOQUE DE IMPORTACIONES
// Se importan las clases necesarias para definir el servicio API con Retrofit
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// DEFINICIÓN DE INTERFAZ PARA EL SERVICIO API
public interface MealApiService {

    // MÉTODO PARA BUSCAR RECETAS POR NOMBRE
    // Define una solicitud GET al endpoint "search.php" del API
    // Usa el parámetro de consulta "s" para especificar el nombre del platillo a buscar
    // Devuelve un objeto Call que encapsula una respuesta de tipo MealResponse
    @GET("search.php")
    Call<MealResponse> searchMeals(@Query("s") String mealName);

}
