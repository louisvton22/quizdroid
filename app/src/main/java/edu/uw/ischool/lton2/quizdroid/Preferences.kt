package edu.uw.ischool.lton2.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Preferences : AppCompatActivity() {
    val TAG = "Preferences"
    override fun onCreate(savedInstanceState: Bundle?) {
        val quizApp = (application as QuizApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        findViewById<TextView>(R.id.txtUrl).text = "Current download URL: ${quizApp.settings.first}"
        findViewById<TextView>(R.id.txtDuration).text = "Current Duration ${quizApp.settings.second} minute"

        val submit = findViewById<Button>(R.id.btnSettings)
        submit.setOnClickListener {

            if (!findViewById<EditText>(R.id.editUrl).text.toString().isEmpty() && !findViewById<EditText>(R.id.editUrl).text.toString().isEmpty()) {

                val newURL = findViewById<EditText>(R.id.editUrl).text.toString()
                val newDuration = findViewById<EditText>(R.id.editDuration).text.toString()
                quizApp.setSetttings(Pair<String, Int>(newURL, newDuration.toInt()))
                Log.i(TAG, "Preferences updated")
                Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show()
                val returnMain = Intent(this, MainActivity::class.java)

                returnMain.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(returnMain)
            } else {
                Toast.makeText(this, "One of the parameters was left blank. Try again", Toast.LENGTH_SHORT).show()
            }
        }


    }
}