package com.example.foodhub;

// IMPORTACIÓN NECESARIA PARA MANEJAR LISTAS DE OBJETOS
import java.util.List;

/**
 * Clase MealResponse
 * Representa la estructura de la respuesta JSON obtenida desde la API TheMealDB.
 * Esta clase encapsula una lista de objetos Meal, que corresponde a la clave "meals" del JSON.
 */
public class MealResponse {

    // Lista de objetos Meal que representan los resultados de la búsqueda
    private List<Meal> meals;

    // Método getter: devuelve la lista de comidas obtenida
    public List<Meal> getMeals() {
        return meals;
    }

    // Método setter: asigna una lista de comidas a este objeto
    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}

