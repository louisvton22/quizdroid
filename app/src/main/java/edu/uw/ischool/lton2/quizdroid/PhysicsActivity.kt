package edu.uw.ischool.lton2.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PhysicsActivity : AppCompatActivity() {
    val nq: Int = 6
    val desc: String = "This math quiz contains physics concepts related to motion, kinematics, magnetism, and gas laws."
    val questions: Map<Int, Array<String>> = mapOf(
        1 to arrayOf("What's the difference between speed and velocity?", "they mean the same thing", "speed's a vector and velocity's a scalar", "speed's a scalar and velocity's a vector", "velocity can only be positive", "3"),
        2 to arrayOf("Which of the following is NOT one of Newton's law of motion?", "moving equates to doing work", "force is equal to mass times acceleration", "an object in motion stays in motion", "every action has an opposite or equal reaction", "1"),
        3 to arrayOf("What phenomenon describes how screwdrivers are better than hand screwing", "angular momentum", "leverage", "grip", "chaotic", "2"),
        4 to arrayOf("What is a tool that refracts light into visible wavelengths", "square", "lens", "light splitter", "prism", "4"),
        5 to arrayOf("Which of the following describes work", "Thinking about your homework","Isometric exercises","Standing in place with a box","Carrying a box across the street", "4"),
        6 to arrayOf("What phenomenon prevents everything from sliding all around the place", "Gravity", "Friction", "Glue", "Weight", "2")
    )

    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physics)

        val titleFragment = TitleFragment()
//        bundle.putString("description", desc)
//        bundle.putInt("numQuestions", nq)
//        bundle.putSerializable("questions", HashMap<Int, Array<String>>(questions))

    //    titleFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, titleFragment)
            commit()
        }


    }
}