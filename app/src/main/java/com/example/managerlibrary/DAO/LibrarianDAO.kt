package com.example.managerlibrary.DAO

import android.content.ContentValues
import android.content.Context
import com.example.managerlibrary.DTO.LibrarianDTO
import com.example.managerlibrary.DataBaseHelper.ManagerBookDataBase
import java.sql.SQLDataException

class LibrarianDAO(context: Context) {
    private val db: ManagerBookDataBase = ManagerBookDataBase(context)

    fun addLibrarian(librarianDTO: LibrarianDTO): Long {
        val values = ContentValues()
        values.put("librarianID", librarianDTO.id)
        values.put("librarianName", librarianDTO.name)
        values.put("password", librarianDTO.password)
        values.put("role", librarianDTO.role)
        var result: Long = -1L
        val dbWritable = db.writableDatabase
        try {
            result = dbWritable.insert("Librarian", null, values)
        } catch (e: SQLDataException) {
            e.printStackTrace()
        } finally {
            dbWritable.close()
        }
        return result
    }

    //get librarian username(id) by id return long  if not found return -1
    fun getLibrarianUsernameByID(id: String): Int {
        var result: Int = -1
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Librarian WHERE librarianID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id))
        if (cursor.count > 0) {
            cursor.moveToFirst()
            result = cursor.getShort(0).toByte().toInt()
        }
        cursor.close()
        dbReadable.close()
        return result
    }

    //check password librarian return true if correct else return false
    fun checkPasswordLibrarian(id: String, password: String): Int {
        var result: Int = -1
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Librarian WHERE librarianID = ? AND password = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id, password))
        if (cursor.count > 0) {
            cursor.moveToFirst()
            result = 1
        }
        cursor.close()
        dbReadable.close()
        return result
    }
}