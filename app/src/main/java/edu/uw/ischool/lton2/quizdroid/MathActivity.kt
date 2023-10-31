package edu.uw.ischool.lton2.quizdroid

import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MathActivity : AppCompatActivity() {

    val nq: Int = 5
    val desc: String = "This is the description for the math quiz"
    val questions: Map<Int, Array<String>> = mapOf(
        1 to arrayOf("What's 2+2", "option 1", "option 2", "option 3", "option 4", "1"),
        2 to arrayOf("what is the area under a function called", "option 1", "option 2", "option 3", "option 4", "1"),
        3 to arrayOf("What is the type of equilibrium that moves", "option 1", "option 2", "option 3", "option 4", "1"),
        4 to arrayOf("Who created calculus", "option 1", "option 2", "option 3", "option 4", "1")
    )

    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math)

        val titleFragment = TitleFragment()
        bundle.putString("description", desc)
        bundle.putInt("numQuestions", nq)
        bundle.putSerializable("questions", HashMap<Int, Array<String>>(questions))

        titleFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, titleFragment)
            commit()
        }


    }
}