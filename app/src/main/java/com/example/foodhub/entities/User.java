package com.example.foodhub.entities;

// Clase de modelo que representa a un usuario en la aplicación
public class User {
    private int id;             // Identificador único del usuario (clave primaria)
    private String username;    // Nombre de usuario (único en la base de datos)
    private String password;    // Contraseña del usuario (se almacenará hasheada)

    // Constructor vacío requerido por muchos frameworks (por ejemplo, para deserialización)
    public User() {
    }

    // Constructor sin ID (para crear un nuevo usuario antes de insertar en la base de datos)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor completo (útil para recuperar usuarios desde la base de datos)
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Métodos getter y setter para acceder y modificar los campos privados

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
