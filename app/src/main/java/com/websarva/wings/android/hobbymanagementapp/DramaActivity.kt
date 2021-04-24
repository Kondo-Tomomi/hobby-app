package com.websarva.wings.android.hobbymanagementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class DramaActivity : AppCompatActivity() {
    private val _helper = DatabaseHelper(this)

    private val colorList = intArrayOf(
        R.color.genrePolice, R.color.genreMystery, R.color.genreLove,
        R.color.genreComedy, R.color.genreHorror, R.color.genreYouth,
        R.color.genreMusic, R.color.genreSF,
        R.color.genreFamily, R.color.genreMedical
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drama)
        Log.i("drama_activity","onCreate()")

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvProduction = findViewById<TextView>(R.id.tvProduction)
        val tvYear = findViewById<TextView>(R.id.tvYear)
        val tvActor1 = findViewById<TextView>(R.id.tvActor1)
        val tvActor2 = findViewById<TextView>(R.id.tvActor2)
        val tvActor3 = findViewById<TextView>(R.id.tvActor3)
        val tvRating = findViewById<TextView>(R.id.tvRating)
        val tvReview = findViewById<TextView>(R.id.tvReview)

        tvTitle.text = intent.getStringExtra("text")
        Log.i("drama_activity","title set")
        tvProduction.text = intent.getStringExtra("production")
        Log.i("drama_activity", "production set")
        tvYear.text = intent.getStringExtra("year")
        Log.i("drama_activity", "year set")

        var genreNum:Int? = intent.getStringExtra("genre1")?.toInt()
        Log.i("drama_activity", "genreNum = $genreNum")
        genreConfig(genreNum, 1)
        genreNum = intent.getStringExtra("genre2")?.toInt()
        Log.i("drama_activity", "genreNum = $genreNum")
        genreConfig(genreNum, 2)
        genreNum = intent.getStringExtra("genre3")?.toInt()
        Log.i("drama_activity", "genreNum = $genreNum")
        genreConfig(genreNum, 3)

        tvActor1.text = intent.getStringExtra("actor1")
        Log.i("drama_activity", "actor1 set")
        tvActor2.text = intent.getStringExtra("actor2")
        Log.i("drama_activity", "actor2 set")
        tvActor3.text = intent.getStringExtra("actor3")
        Log.i("drama_activity", "actor3 set")

        tvRating.text = intent.getStringExtra("rating")
        Log.i("drama_activity", "rating set")

        tvReview.text = intent.getStringExtra("review")
        Log.i("drama_activity", "review set")

    }

    fun onBackButtonClick(view: View){
        finish()
    }

    fun onEditButtonClick(view: View){
        val text = "編集"
        val intent = Intent(applicationContext, BlankActivity::class.java)
        intent.putExtra("text",text)
        startActivity(intent)
    }

    private fun genreConfig(genre:Int?, num:Int){
        var tvGenre: TextView = findViewById(R.id.tvGenre1)
        if (num==2) tvGenre = findViewById(R.id.tvGenre2)
        if (num==3) tvGenre = findViewById(R.id.tvGenre3)

        if(genre == null){
            tvGenre.text = ""
            return
        }

        val db = _helper.readableDatabase
        val sql = "SELECT * FROM genre_list WHERE _id = $genre"
        val cursor = db.rawQuery(sql, null)

        while(cursor.moveToNext()) {
            val idxNote = cursor.getColumnIndex("genre")
            tvGenre.text = cursor.getString(idxNote)
        }
        tvGenre.setBackgroundResource(colorList[genre-1])

        return
    }
}
