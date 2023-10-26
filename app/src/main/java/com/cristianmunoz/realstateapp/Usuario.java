package com.cristianmunoz.realstateapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Usuario extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final String TABLE_NAME = "user_table";
    private static final String COL_ID = "ID";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_NOMBRE = "NOMBRE";
    private static final String COL_APELLIDO = "APELLIDO";
    private static final String COL_FECHA_NACIMIENTO = "FECHA_NACIMIENTO";
    private static final String COL_CONTRASENA = "CONTRASENA";

    public Usuario(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT, " +
                COL_NOMBRE + " TEXT, " +
                COL_APELLIDO + " TEXT, " +
                COL_FECHA_NACIMIENTO + " TEXT, " +
                COL_CONTRASENA + " TEXT)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean agregarUsuario(String email, String nombre, String apellido, String fechaNacimiento, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_NOMBRE, nombre);
        contentValues.put(COL_APELLIDO, apellido);
        contentValues.put(COL_FECHA_NACIMIENTO, fechaNacimiento);
        contentValues.put(COL_CONTRASENA, contrasena);
        long result = db.insert(TABLE_NAME, null, contentValues);

        // Si se insertó correctamente, result será diferente de -1
        return result != -1;
    }
    public boolean verificarUsuario(String email, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL_EMAIL + " =? AND " + COL_CONTRASENA + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{email, contrasena});

        return cursor.getCount() > 0;
    }
}

