package com.example.hankki

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLite를 사용하여 장바구니 내용 저장
class CartDBHelper : SQLiteOpenHelper {
    constructor(context: Context) : super(context, "cart.db", null, 1)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE cart(name TEXT PRIMARY KEY, " + "price INTEGER, amount INTEGER);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS cart;")
    }
}