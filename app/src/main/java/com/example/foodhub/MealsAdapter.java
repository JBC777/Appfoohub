package com.example.foodhub;

// IMPORTACIONES NECESARIAS PARA VISTAS Y ADAPTADOR
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Adaptador personalizado para mostrar una lista de comidas (Meal) en un RecyclerView.
 * Utiliza el patrón ViewHolder para optimizar el rendimiento.
 */
public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    // Lista de objetos Meal que se mostrarán en el RecyclerView
    private List<Meal> mealList;

    /**
     * Constructor del adaptador.
     * @param mealList Lista de comidas que se utilizará para poblar el RecyclerView
     */
    public MealsAdapter(List<Meal> mealList) {
        this.mealList = mealList;
    }

    /**
     * Método que se llama cuando se necesita crear una nueva vista para un ítem.
     * @param parent El ViewGroup padre al que se añadirá la nueva vista
     * @param viewType Tipo de vista (no se usa en este caso)
     * @return Un nuevo ViewHolder que contiene la vista del ítem
     */
    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño de cada ítem de la lista desde XML (item_meal.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    /**
     * Método que se llama para mostrar los datos de un ítem en una posición específica.
     * @param holder El ViewHolder que debe actualizarse con los datos del ítem
     * @param position La posición del ítem en la lista
     */
    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        // Obtiene el objeto Meal correspondiente a la posición
        Meal meal = mealList.get(position);

        // Establece el nombre de la comida en el TextView
        holder.textMealName.setText(meal.getStrMeal());

        // Carga la imagen de la comida en el ImageView utilizando Glide
        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb()) // URL de la imagen
                .into(holder.imageMealThumb); // ImageView destino
    }

    /**
     * Devuelve el número total de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return mealList.size();
    }

    /**
     * Clase interna que representa el patrón ViewHolder.
     * Contiene las referencias a los elementos de la vista para cada ítem.
     */
    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView textMealName;
        ImageView imageMealThumb;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializa los elementos visuales que se usarán para cada ítem
            textMealName = itemView.findViewById(R.id.textMealName);
            imageMealThumb = itemView.findViewById(R.id.imageMealThumb);
        }
    }
}
