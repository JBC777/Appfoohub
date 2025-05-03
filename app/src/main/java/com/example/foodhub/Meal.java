package com.example.foodhub;

public class Meal {
    private String idMeal;
    private String strMeal;
    private String strMealThumb;
    private String strInstructions;  // ← nuevo campo

    // Constructor completo
    public Meal(String idMeal, String strMeal, String strMealThumb, String strInstructions) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.strInstructions = strInstructions;
    }

    // Constructor vacío (para Gson)
    public Meal() { }

    // Getters
    public String getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    // Setters
    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }
}
