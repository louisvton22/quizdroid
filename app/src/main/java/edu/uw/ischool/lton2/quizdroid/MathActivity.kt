package edu.uw.ischool.lton2.quizdroid

import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MathActivity : AppCompatActivity() {

    val nq: Int = 5
    val desc: String = "This math quiz contains mathematic concepts up to a college-level including some historical facts behind famous mathematicians."
    val questions: Map<Int, Array<String>> = mapOf(
        1 to arrayOf("What's 2+2", "4", "1", "3", "2", "1"),
        2 to arrayOf("what is the area under a function called", "derivative", "integral", "area", "perimeter", "2"),
        3 to arrayOf("What is the type of equilibrium that moves", "static", "moving", "dynamic", "chaotic", "3"),
        4 to arrayOf("Who created calculus", "Euler", "Bernoulli", "Pythagorous", "Newton", "4"),
        5 to arrayOf("What property describes x(y + z) = xy + xz", "Commutative", "Associative", "Transitive", "Distributive", "4")
    )

    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math)

        val titleFragment = TitleFragment()
        bundle.putString("description", desc)
        bundle.putInt("numQuestions", nq)
        bundle.putSerializable("questions", HashMap<Int, Array<String>>(questions))

//        titleFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, titleFragment)
            commit()
        }


    }
}