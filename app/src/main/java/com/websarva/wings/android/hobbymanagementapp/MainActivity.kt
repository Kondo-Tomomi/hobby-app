package com.websarva.wings.android.hobbymanagementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class MainActivity : AppCompatActivity() {
    var mConstraintSet = ConstraintSet()
    private var _buttonId = -1

    private val _helper = DatabaseHelper(this@MainActivity)
    val dbOperation = DatabaseOperation(_helper)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mConstraintSet.clone(this, R.layout.activity_main)

        val buttonAlreadyWatch = findViewById<Button>(R.id.btDone)
        val buttonConcern = findViewById<Button>(R.id.btConcern)
        val buttonPlus = findViewById<ImageButton>(R.id.ibPlus)

        val listener = HelloListener()
        buttonAlreadyWatch.setOnClickListener(listener)
        buttonConcern.setOnClickListener(listener)
        buttonPlus.setOnClickListener(listener)
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val tvTitle = findViewById<TextView>(R.id.tvStartTitle)
        val buttonAlreadyWatch = findViewById<Button>(R.id.btDone)
        val buttonConcern = findViewById<Button>(R.id.btConcern)
        val buttonPlus = findViewById<ImageButton>(R.id.ibPlus)
        homeButtonPlacement(buttonAlreadyWatch,buttonConcern,buttonPlus,tvTitle)
    }

    private inner class HelloListener : View.OnClickListener{
        override fun onClick(view: View) {

            when(view.id) {
                R.id.btDone -> {
                    _buttonId = 101
                }
                R.id.btConcern -> {
                    _buttonId = 102
                }
                R.id.ibPlus -> {
                    _buttonId = 103
                }
            }
            dbOperation.buttonData(_buttonId)

            when(view.id) {
                R.id.btDone -> {
                    val intent = Intent(applicationContext, AlreadyWatchListActivity::class.java)
                    startActivity(intent)
                }
                R.id.btConcern -> {
                    val intent = Intent(applicationContext, ConcernListActivity::class.java)
                    startActivity(intent)
                }
                R.id.ibPlus -> {
                    val text = "追加"
                    val intent = Intent(applicationContext, BlankActivity::class.java)
                    intent.putExtra("text",text)
                    startActivity(intent)
                }
            }
        }
    }

    private fun homeButtonPlacement(already:Button, concern:Button, plus:ImageButton, title:TextView){
        val db = this@MainActivity._helper.writableDatabase
        val a = dbOperation.takeCount(101,db) //already_watch
        val c = dbOperation.takeCount(102,db) //concern
        val p = dbOperation.takeCount(103,db) //plus
        val param = 3

        val mConstraintLayout = findViewById<ConstraintLayout>(R.id.activity_main)

        Log.i("hobby_app","homeButtonPlacement()")
        Log.i("hobby_app", "blue=$a  green=$c  plus=$p")

        mConstraintSet.clear(already.id, ConstraintSet.START)
        mConstraintSet.clear(already.id, ConstraintSet.END)
        mConstraintSet.clear(concern.id, ConstraintSet.START)
        mConstraintSet.clear(concern.id, ConstraintSet.END)
        if(a >= c+param) {
            alreadyConcernSwap(concern.id, already.id)
        }else{
            alreadyConcernSwap(already.id, concern.id)
        }

        mConstraintSet.clear(already.id, ConstraintSet.TOP)
        mConstraintSet.clear(already.id, ConstraintSet.BOTTOM)
        mConstraintSet.clear(concern.id, ConstraintSet.TOP)
        mConstraintSet.clear(concern.id, ConstraintSet.BOTTOM)
        mConstraintSet.clear(plus.id, ConstraintSet.TOP)
        mConstraintSet.clear(plus.id, ConstraintSet.BOTTOM)
        if(p+param <= a+c){
            mConstraintSet.connect(
                already.id, ConstraintSet.TOP, plus.id, ConstraintSet.BOTTOM
            )
            mConstraintSet.connect(
                concern.id, ConstraintSet.TOP, plus.id, ConstraintSet.BOTTOM
            )
            mConstraintSet.connect(
                already.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
            )
            mConstraintSet.connect(
                concern.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
            )
            mConstraintSet.connect(
                plus.id, ConstraintSet.BOTTOM, already.id, ConstraintSet.TOP
            )
            mConstraintSet.connect(
                plus.id, ConstraintSet.TOP, title.id, ConstraintSet.BOTTOM
            )
        }else{
            mConstraintSet.connect(
                already.id, ConstraintSet.TOP, title.id, ConstraintSet.BOTTOM
            )
            mConstraintSet.connect(
                concern.id, ConstraintSet.TOP, title.id, ConstraintSet.BOTTOM
            )
            mConstraintSet.connect(
                already.id, ConstraintSet.BOTTOM, plus.id, ConstraintSet.TOP
            )
            mConstraintSet.connect(
                concern.id, ConstraintSet.BOTTOM, plus.id, ConstraintSet.TOP
            )
            mConstraintSet.connect(
                plus.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
            )
            mConstraintSet.connect(
                plus.id, ConstraintSet.TOP, concern.id, ConstraintSet.BOTTOM
            )
        }
        mConstraintSet.applyTo(mConstraintLayout)
    }

    private fun alreadyConcernSwap(left:Int, right:Int){
        mConstraintSet.connect(
            right, ConstraintSet.START, left, ConstraintSet.END
        )
        mConstraintSet.connect(
            right, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END
        )
        mConstraintSet.connect(
            left, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START
        )
        mConstraintSet.connect(
            left, ConstraintSet.END, right, ConstraintSet.START
        )
    }
}