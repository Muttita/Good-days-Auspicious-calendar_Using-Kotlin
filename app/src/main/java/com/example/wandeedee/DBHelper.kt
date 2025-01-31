package com.example.wandeedee

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, "wandeedeedb.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun getFortuneData(targetDate: String): Cursor {
        val query = "SELECT * FROM fortuneDay WHERE Fdate = ?"
        val selectionArgs = arrayOf(targetDate)

        val cursor = readableDatabase.rawQuery(query, selectionArgs)
        Log.d("DatabaseQuery", "Rows count: ${cursor.count}")

        return cursor
    }


    fun getAusDays(startDate: String, endDate: String, selectedDesc: String): Cursor {
        val query = "SELECT * FROM auspiciousDay WHERE Adate BETWEEN ? AND ? AND ADesc = ?"
        val selectionArgs = arrayOf(startDate, endDate, selectedDesc)

        return readableDatabase.rawQuery(query, selectionArgs)
    }

    fun getCalenderData(targetDate: String): Cursor {
        val query = "SELECT * FROM auspiciousDay WHERE Adate = ?"
        val selectionArgs = arrayOf(targetDate)

        return readableDatabase.rawQuery(query, selectionArgs)
    }




}
