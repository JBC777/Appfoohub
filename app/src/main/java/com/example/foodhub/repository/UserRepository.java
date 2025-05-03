package com.example.foodhub.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodhub.dataaccess.ManagerDatabase;
import com.example.foodhub.entities.User;
import com.example.foodhub.util.HashUtils;

public class UserRepository {

    // Referencia al helper que gestiona la base de datos SQLite
    private final ManagerDatabase dbHelper;

    // Constructor que inicializa el helper con el contexto
    public UserRepository(Context context) {
        dbHelper = new ManagerDatabase(context);
    }

    // Inserta un nuevo usuario en la base de datos
    public boolean insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Se guarda el nombre de usuario y la contraseña hasheada
        values.put(ManagerDatabase.COLUMN_USERNAME, user.getUsername());
        values.put(ManagerDatabase.COLUMN_PASSWORD_HASH, HashUtils.hashPassword(user.getPassword()));

        // Inserta el registro en la tabla y verifica si fue exitoso
        long result = db.insert(ManagerDatabase.TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    // Verifica si un usuario ya existe en la base de datos
    public boolean userExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ManagerDatabase.TABLE_USER + " WHERE " +
                ManagerDatabase.COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean exists = cursor.moveToFirst(); // Devuelve true si hay resultados
        cursor.close();
        db.close();
        return exists;
    }

    // Recupera la contraseña hasheada asociada a un nombre de usuario
    public String getHashedPassword(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String hashedPassword = null;
        Cursor cursor = null;

        try {
            cursor = db.query(
                    ManagerDatabase.TABLE_USER,
                    new String[]{ManagerDatabase.COLUMN_PASSWORD_HASH},
                    ManagerDatabase.COLUMN_USERNAME + " = ?",
                    new String[]{username},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                hashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(ManagerDatabase.COLUMN_PASSWORD_HASH));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return hashedPassword;
    }

    // Valida las credenciales del usuario comparando las contraseñas
    public boolean validateLogin(String username, String password) {
        String hashedPasswordFromDB = getHashedPassword(username);
        return hashedPasswordFromDB != null && HashUtils.checkPassword(password, hashedPasswordFromDB);
    }

    // Actualiza la contraseña de un usuario existente
    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Guarda la nueva contraseña hasheada
        values.put(ManagerDatabase.COLUMN_PASSWORD_HASH, HashUtils.hashPassword(newPassword));

        // Ejecuta la actualización y devuelve true si se afectó al menos una fila
        int rowsAffected = db.update(
                ManagerDatabase.TABLE_USER,
                values,
                ManagerDatabase.COLUMN_USERNAME + " = ?",
                new String[]{username}
        );
        db.close();
        return rowsAffected > 0;
    }

    // Elimina un usuario de la base de datos
    public boolean deleteUser(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Ejecuta la eliminación y verifica si fue exitosa
        int rowsAffected = db.delete(
                ManagerDatabase.TABLE_USER,
                ManagerDatabase.COLUMN_USERNAME + " = ?",
                new String[]{username}
        );
        db.close();
        return rowsAffected > 0;
    }

    // Recupera un objeto User completo a partir del nombre de usuario
    public User getUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            String query = "SELECT * FROM " + ManagerDatabase.TABLE_USER +
                    " WHERE " + ManagerDatabase.COLUMN_USERNAME + " = ?";
            cursor = db.rawQuery(query, new String[]{username});

            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ManagerDatabase.COLUMN_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(ManagerDatabase.COLUMN_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(ManagerDatabase.COLUMN_PASSWORD_HASH)));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return user;
    }
}
