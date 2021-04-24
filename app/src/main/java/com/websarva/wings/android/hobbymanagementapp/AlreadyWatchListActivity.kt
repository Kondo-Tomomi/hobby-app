package com.websarva.wings.android.hobbymanagementapp

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet


class AlreadyWatchListActivity : AppCompatActivity() , SimpleCursorAdapter.ViewBinder {
    private var _buttonId = -1
    var mConstraintSet = ConstraintSet()
    private val _helper = DatabaseHelper(this@AlreadyWatchListActivity)

    private val colorList = intArrayOf(
        R.color.genrePolice, R.color.genreMystery, R.color.genreLove,
        R.color.genreComedy, R.color.genreHorror, R.color.genreYouth,
        R.color.genreMusic, R.color.genreSF,
        R.color.genreFamily, R.color.genreMedical
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_already_watch_list)
        mConstraintSet.clone(this, R.layout.activity_already_watch_list)

        val buttonHome = findViewById<Button>(R.id.btHome)
        val buttonDelete = findViewById<Button>(R.id.btDelete)
        val buttonSearch = findViewById<Button>(R.id.btAlreadySearch)
        val buttonSort = findViewById<Button>(R.id.btAlreadySort)
        val listener = ButtonClickListener()
        buttonHome.setOnClickListener(listener)
        buttonDelete.setOnClickListener(listener)
        buttonSearch.setOnClickListener(listener)
        buttonSort.setOnClickListener(listener)

        val db = _helper.writableDatabase
        val dramaList = findViewById<ListView>(R.id.lvAlreadyWatch)
        val c = db.query("already_watch_list", null, null, null, null, null, "count DESC", null)
        c.moveToFirst()
        val from = arrayOf(
            "_id",
            "production",
            "year",
            "rating",
            "genre1",
            "genre2",
            "genre3",
            "actor1",
            "actor2",
            "actor3"
        )
        val to = intArrayOf(
            R.id.tvListTitle,
            R.id.tvProduction,
            R.id.tvYear,
            R.id.tvRating,
            R.id.tvGenre1,
            R.id.tvGenre2,
            R.id.tvGenre3,
            R.id.tvActor1,
            R.id.tvActor2,
            R.id.tvActor3
        )
        val adapter = SimpleCursorAdapter(
            this,
            R.layout.already_watch_list_layout,
            c, from, to, 0
        )
        adapter.viewBinder = this
        dramaList.adapter = adapter
        dramaList.onItemClickListener = ListItemClicklistner()
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val dbOperation = DatabaseOperation(_helper)
        val buttonDelete = findViewById<Button>(R.id.btDelete)
        val buttonSearch = findViewById<Button>(R.id.btAlreadySearch)
        val buttonSort = findViewById<Button>(R.id.btAlreadySort)
        mConstraintSet = dbOperation.listButtonPlacement(
            buttonDelete,
            buttonSort,
            buttonSearch,
            200,
            mConstraintSet
        )
        val mConstraintLayout = findViewById<ConstraintLayout>(R.id.activity_already_watch)
        mConstraintSet.applyTo(mConstraintLayout)
    }

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val dbOperation = DatabaseOperation(_helper)
            //val sb = StringBuilder()

            when (view.id) {
                R.id.btHome -> {
                    _buttonId = 201
                    //sb.append("ホーム")
                }
                R.id.btAlreadySort -> {
                    _buttonId = 203
                    //sb.append("並べ替え")
                }
                R.id.btAlreadySearch -> {
                    _buttonId = 204
                    //sb.append("絞り込み")
                }
                R.id.btDelete -> {
                    _buttonId = 202
                    //sb.append("削除")
                }
            }
            dbOperation.buttonData(_buttonId)

            when (view.id){
                R.id.btHome -> {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.btAlreadySort -> {
                    val text = "並べ替え"
                    val intent = Intent(applicationContext, BlankActivity::class.java)
                    intent.putExtra("text",text)
                    startActivity(intent)
                }
                R.id.btAlreadySearch -> {
                    val text = "絞り込み"
                    val intent = Intent(applicationContext, BlankActivity::class.java)
                    intent.putExtra("text",text)
                    startActivity(intent)
                }
                R.id.btDelete -> {
                    val text = "削除"
                    val intent = Intent(applicationContext, BlankActivity::class.java)
                    intent.putExtra("text",text)
                    startActivity(intent)
                }
            }
        }
    }

    private inner class ListItemClicklistner:AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = parent?.getItemAtPosition(position) as SQLiteCursor
            Log.i("HobbyManagementApp", "item : "+item?.javaClass)

            //val dbOperation = DatabaseOperation(_helper)
            //dbOperation.listData(title, "already_watch_list")
            val intent = Intent(applicationContext, DramaActivity::class.java)
            intent.putExtra("text",item.getString(0))
            intent.putExtra("year",item.getString(1))
            intent.putExtra("production",item.getString(2))
            intent.putExtra("rating",item.getString(3))
            intent.putExtra("review",item.getString(4))
            intent.putExtra("actor1",item.getString(5))
            intent.putExtra("actor2",item.getString(6))
            intent.putExtra("actor3",item.getString(7))
            intent.putExtra("genre1",item.getString(8))
            intent.putExtra("genre2",item.getString(9))
            intent.putExtra("genre3",item.getString(10))
            startActivity(intent)
        }
    }

    override fun setViewValue(view: View?, cursor: Cursor, columnIndex: Int): Boolean {
        Log.i("HobbyManagementApp", "setViewValue() called. ")
        Log.i("HobbyManagementApp", "columnIndex : $columnIndex")
        if(columnIndex == 8 ||columnIndex == 9 ||columnIndex == 10){
            if (cursor.getInt(columnIndex)==0) return false
            val color = colorList[cursor.getInt(columnIndex) - 1]
            val db = _helper.readableDatabase
            val sql = "SELECT * FROM genre_list WHERE _id = " + cursor.getInt(columnIndex)
            val c = db.rawQuery(sql, null)
            var text = ""
            val tv = view as TextView

            while (c.moveToNext()) {
                val idxNote = c.getColumnIndex("genre")
                text = c.getString(idxNote)
            }
            Log.i("HobbyManagementApp", "text : $text")
            tv.setBackgroundResource(color)
            tv.text = text
            Log.i("HobbyManagementApp", "return true.")
            return true
        }
        return false
    }
}