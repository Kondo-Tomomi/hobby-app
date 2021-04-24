package com.websarva.wings.android.hobbymanagementapp

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet

class DatabaseOperation(private val helper: DatabaseHelper){

    fun takeCount(buttonId: Int, db:SQLiteDatabase):Int{
        val sql = "SELECT * FROM button_count WHERE _id = $buttonId"
        val cursor = db.rawQuery(sql, null)
        var count = 0

        while(cursor.moveToNext()){
            val idxNote = cursor.getColumnIndex("count")
            count = cursor.getInt(idxNote)
        }
        return count
    }

    fun buttonData(buttonId: Int):Int{
        val db = helper.writableDatabase

        var count = takeCount(buttonId,db)
        count++

        val sqlDelete = "DELETE FROM button_count WHERE _id = ?"
        var stmt = db.compileStatement(sqlDelete)
        stmt.bindLong(1, buttonId.toLong())
        stmt.executeUpdateDelete()

        val sqlInsert = "INSERT INTO button_count (_id, count) VALUES (?,?)"
        stmt = db.compileStatement(sqlInsert)
        stmt.bindLong(1, buttonId.toLong())
        stmt.bindLong(2, count.toLong())

        stmt.executeInsert()

        return count
    }

    fun listButtonPlacement( btA :Button, btB:Button, btC:Button, type:Int, mConstraintSet:ConstraintSet): ConstraintSet {
        val db = helper.writableDatabase

        Log.i("hobby_app", "listButtonPlacement")

        val a = takeCount(type+2,db)
        val b = takeCount(type+3,db)
        val c = takeCount(type+4,db)
        val param = 3

        Log.i("hobby_app", "delete=$a  sort=$b  search=$c")

        val array = arrayOf(Pair(btA,a),Pair(btB,b),Pair(btC,c))
        for (i in 1 downTo 0){
            for (j in 0..i)
                if(array[j].second > array[j+1].second){
                    val tmp = array[j]
                    array[j] = array[j+1]
                    array[j+1] = tmp
                }
        }
        mConstraintSet.clear(btA.id, ConstraintSet.START)
        mConstraintSet.clear(btA.id, ConstraintSet.END)
        mConstraintSet.clear(btB.id, ConstraintSet.START)
        mConstraintSet.clear(btB.id, ConstraintSet.END)
        mConstraintSet.clear(btC.id, ConstraintSet.START)
        mConstraintSet.clear(btC.id, ConstraintSet.END)

        mConstraintSet.connect(
            array[0].first.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START
        )
        mConstraintSet.connect(
            array[0].first.id, ConstraintSet.END, array[1].first.id, ConstraintSet.START
        )
        mConstraintSet.connect(
            array[1].first.id, ConstraintSet.START, array[0].first.id, ConstraintSet.END
        )
        mConstraintSet.connect(
            array[1].first.id, ConstraintSet.END, array[2].first.id, ConstraintSet.START
        )
        mConstraintSet.connect(
            array[2].first.id, ConstraintSet.START, array[1].first.id, ConstraintSet.END
        )
        mConstraintSet.connect(
            array[2].first.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END
        )

        return mConstraintSet
    }

    fun listData(name: String, listName: String){
        val db = helper.writableDatabase

        val sql = "SELECT * FROM $listName WHERE _id = $name"
        val cursor = db.rawQuery(sql, null)
        var count = 0

        while(cursor.moveToNext()){
            val idxNote = cursor.getColumnIndex("count")
            count = cursor.getInt(idxNote)
        }

        count++

        val sqlUpdate = "UPDATE $listName SET count = ? WHERE _id = ?"
        var stmt = db.compileStatement(sqlUpdate)
        stmt.bindLong(1, count.toLong())
        stmt.bindString(2, name)
        stmt.executeUpdateDelete()

        return
    }

}