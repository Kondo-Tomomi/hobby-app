package com.websarva.wings.android.hobbymanagementapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.StringBuilder

class DatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "hobby_app.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sb = StringBuilder()
        sb.append("CREATE TABLE button_count (")
        sb.append("_id INTEGER PRIMARY KEY,")
        sb.append("count INTEGER")
        sb.append(");")
        val sql1 = sb.toString()

        sb.setLength(0)
        sb.append("CREATE TABLE already_watch_list (")
        sb.append("_id TEXT PRIMARY KEY,")
        sb.append("year INTEGER,")
        sb.append("production TEXT,")
        sb.append("rating INTEGER,")
        sb.append("review TEXT,")
        sb.append("actor1 TEXT,")
        sb.append("actor2 TEXT,")
        sb.append("actor3 TEXT,")
        sb.append("genre1 INTEGER,")
        sb.append("genre2 INTEGER,")
        sb.append("genre3 INTEGER,")
        sb.append("count INTEGER")
        sb.append(");")
        val sql2 = sb.toString()

        sb.setLength(0)
        sb.append("CREATE TABLE concern_list (")
        sb.append("_id TEXT PRIMARY KEY,")
        sb.append("year INTEGER,")
        sb.append("production TEXT,")
        sb.append("rating INTEGER,")
        sb.append("review TEXT,")
        sb.append("actor1 TEXT,")
        sb.append("actor2 TEXT,")
        sb.append("actor3 TEXT,")
        sb.append("genre1 INTEGER,")
        sb.append("genre2 INTEGER,")
        sb.append("genre3 INTEGER,")
        sb.append("count INTEGER")
        sb.append(");")
        val sql3 = sb.toString()

        sb.setLength(0)
        sb.append("CREATE TABLE genre_list (")
        sb.append("_id INTEGER PRIMARY KEY,")
        sb.append("genre TEXT,")
        sb.append("count INTEGER")
        sb.append(");")
        val sql4 = sb.toString()

        sb.setLength(0)
        sb.append("CREATE TABLE actor_list (")
        sb.append("_id TEXT PRIMARY KEY,")
        sb.append("count INTEGER")
        sb.append(");")
        val sql5 = sb.toString()

        db.execSQL(sql1)
        db.execSQL(sql2)
        db.execSQL(sql3)
        db.execSQL(sql4)
        db.execSQL(sql5)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}