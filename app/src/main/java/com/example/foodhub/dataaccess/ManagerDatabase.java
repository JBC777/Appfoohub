package com.example.foodhub.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Clase que extiende SQLiteOpenHelper para manejar la creación y mantenimiento de la base de datos local
public class ManagerDatabase extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    public static final String DATABASE_NAME = "foodhub.db";
    public static final int DATABASE_VERSION = 1;

    // Constantes de tabla y columnas
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD_HASH = "password_hash";

    // Sentencia SQL para crear la tabla de usuarios
    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PASSWORD_HASH + " TEXT NOT NULL);";

    // Constructor que pasa los parámetros al super de SQLiteOpenHelper
    public ManagerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Se llama al crear la base de datos por primera vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE); // Ejecuta la sentencia de creación de tabla
    }

    // Se llama cuando se actualiza la versión de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borra la tabla existente y la vuelve a crear (pérdida de datos, estrategia simple)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // -------------------------------
    // MÉTODOS CRUD PARA LA TABLA USER
    // -------------------------------

    // Inserta un nuevo usuario con su contraseña hasheada
    public long insertUser(String username, String passwordHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD_HASH, passwordHash);

        long id = db.insert(TABLE_USER, null, values);
        db.close();
        return id; // Devuelve el ID generado, o -1 si falló
    }

    // Verifica si un nombre de usuario ya existe en la base de datos
    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USER,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Obtiene la contraseña hasheada de un usuario específico
    public String getHashedPassword(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USER,
                new String[]{COLUMN_PASSWORD_HASH},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null
        );
        String passwordHash = null;
        if (cursor.moveToFirst()) {
            passwordHash = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return passwordHash;
    }

    // Actualiza la contraseña de un usuario existente
    public int updateUserPassword(String username, String newPasswordHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD_HASH, newPasswordHash);

        int rowsAffected = db.update(
                TABLE_USER,
                values,
                COLUMN_USERNAME + "=?",
                new String[]{username}
        );
        db.close();
        return rowsAffected; // Número de filas modificadas
    }

    // Elimina un usuario de la base de datos
    public int deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(
                TABLE_USER,
                COLUMN_USERNAME + "=?",
                new String[]{username}
        );
        db.close();
        return rowsAffected; // Número de filas eliminadas
    }
}
