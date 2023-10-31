package edu.uw.ischool.lton2.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    val quizCategories = arrayOf("Math", "Physics", "Marvel Superheroes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.quizList)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, quizCategories)
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { parent, view, position, id->
            val clicked = parent.getItemAtPosition(position)
            Log.i("QuizDroid", "clicked item ${clicked as String}")
            var intent = Intent()
            when (clicked) {
                "Math" -> intent = Intent(this, MathActivity::class.java)
                "Physics" -> intent = Intent(this, PhysicsActivity::class.java)
                "Marvel Superheroes" -> intent = Intent(this, SuperheroesActivity::class.java)
            }
            startActivity(intent)

        }
    }
}