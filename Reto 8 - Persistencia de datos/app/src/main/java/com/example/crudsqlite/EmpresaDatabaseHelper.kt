package com.example.crudsqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Empresa(
    val id: Int,
    val name: String,
    val url: String,
    val phone: String,
    val email: String,
    val products: String,
    val classification: String
)

class EmpresaDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "EmpresasDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE empresa (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                url TEXT,
                phone TEXT,
                email TEXT,
                products TEXT,
                classification TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS empresa")
        onCreate(db)
    }

    fun getAllEmpresas(): List<Empresa> {
        val empresas = mutableListOf<Empresa>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM empresa", null)

        if (cursor.moveToFirst()) {
            do {
                val empresa = Empresa(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    url = cursor.getString(cursor.getColumnIndexOrThrow("url")),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    products = cursor.getString(cursor.getColumnIndexOrThrow("products")),
                    classification = cursor.getString(cursor.getColumnIndexOrThrow("classification"))
                )
                empresas.add(empresa)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return empresas
    }

    fun getFilteredEmpresas(name: String, classification: String?): List<Empresa> {
        val empresas = mutableListOf<Empresa>()
        val db = readableDatabase
        val query = StringBuilder("SELECT * FROM empresa WHERE name LIKE ?")
        val args = mutableListOf("%$name%")
        if (classification != null) {
            query.append(" AND classification = ?")
            args.add(classification)
        }

        val cursor = db.rawQuery(query.toString(), args.toTypedArray())
        if (cursor.moveToFirst()) {
            do {
                val empresa = Empresa(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    url = cursor.getString(cursor.getColumnIndexOrThrow("url")),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    products = cursor.getString(cursor.getColumnIndexOrThrow("products")),
                    classification = cursor.getString(cursor.getColumnIndexOrThrow("classification"))
                )
                empresas.add(empresa)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return empresas
    }

    fun addEmpresa(empresa: Empresa) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", empresa.name)
            put("url", empresa.url)
            put("phone", empresa.phone)
            put("email", empresa.email)
            put("products", empresa.products)
            put("classification", empresa.classification)
        }
        db.insert("empresa", null, values)
    }

    fun updateEmpresa(empresa: Empresa) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", empresa.name)
            put("url", empresa.url)
            put("phone", empresa.phone)
            put("email", empresa.email)
            put("products", empresa.products)
            put("classification", empresa.classification)
        }
        db.update("empresa", values, "id = ?", arrayOf(empresa.id.toString()))
    }

    fun deleteEmpresa(id: Int) {
        val db = writableDatabase
        db.delete("empresa", "id = ?", arrayOf(id.toString()))
    }
}
