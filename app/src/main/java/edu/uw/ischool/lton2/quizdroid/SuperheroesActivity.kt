package edu.uw.ischool.lton2.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SuperheroesActivity : AppCompatActivity() {
    val nq: Int = 7
    val desc: String = "This superheroes quiz is about all the Marvel Superhero comic books,and movies that are and not produced by Marvel Studios"
    val questions: Map<Int, Array<String>> = mapOf(
        1 to arrayOf("How did spider-man get his powers?", "He was born with them", "He drank a spider-mutant potion", "radioactive spider bite","exposed to gamma radiation", "3"),
        2 to arrayOf("Which actor plays Thor in all Marvel Studio Movies?", "Chris Hemsworth", "Luke Hemsworth", "Chris Evans", "Robert Downy Jr.", "1"),
        3 to arrayOf("What is Iron Man's AI system called?", "Karen", "Jarvis", "Vision", "Candace", "2"),
        4 to arrayOf("Which team was the Black Panther a part of in Civil War?", "Team Captain America", "Team Hulk", "Independent", "Team Iron-Man", "4"),
        5 to arrayOf("Who held the time stone for most of the MCU movies?", "Thanos","Dr. Strange","Gamora","Vision", "2"),
        6 to arrayOf("What is Wolverine's real name?", "Thomas", "Xavier", "Logan", "Hank", "3"),
        7 to arrayOf("What is Daredevil's occupation aside from crime fighting?", "Lawyer", "Doctor", "Policeman", "Corporate CEO", "1")
    )

    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_superheroes)

        val titleFragment = TitleFragment()
//        bundle.putString("description", desc)
//        bundle.putInt("numQuestions", nq)
//        bundle.putSerializable("questions", HashMap<Int, Array<String>>(questions))
//
//        titleFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, titleFragment)
            commit()
        }


    }
}