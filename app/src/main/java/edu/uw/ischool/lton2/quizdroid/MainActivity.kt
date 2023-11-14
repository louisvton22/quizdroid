package edu.uw.ischool.lton2.quizdroid

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.forEach

interface ITopicRepository {
    fun insert(topic: Topic)

    fun getAll():List<Topic>

    fun getTopic(topic:Topic):Topic?
    fun delete(topic: Topic)

}

class TopicRepository(val context: Context): ITopicRepository {
    val TAG = "TopicRepository"
    var jsonObj: JSONArray
    var topics: MutableList<Topic>

    init {
        // Creating Math topic object
        topics = mutableListOf()
        jsonObj = JSONArray()
        // Superheroes topic object

        // Physics topic object
    }

    override fun insert(topic: Topic) {
        topics.add(topic)
    }

    override fun getAll(): List<Topic> {

        return topics
    }

    override fun getTopic(topic:Topic): Topic? {
        for (t in topics) {
            if (t == topic) {
                return t
            }
        }
        return null
    }

    override fun delete(topic: Topic) {
        topics.remove(topic)
    }

    fun addTopics(jsonObj: JSONArray) {
        // Grab JSON file from internet
            for (i in 0 until jsonObj.length()) {
                val topicObj = jsonObj.getJSONObject(i)
                var questionsList:MutableList<Quiz> = mutableListOf()
                val questions = topicObj.getJSONArray("questions")
                for (j in 0 until questions.length()) {
                    val questionObj = questions.getJSONObject(j)
                    val answers = (0 until questionObj.getJSONArray("answers").length()).map { questionObj.getJSONArray("answers").getString(it)} as MutableList<String>
                    Log.i(TAG, "answers list : $answers")
                    val question = Quiz(questionObj.getString("text"), answers, questionObj.getString("answer").toInt())
                    questionsList.add(question)
                }

                val topic = Topic(topicObj.getString("title"), topicObj.getString("desc"), "", questionsList)
                topics.add(topic)
            }

        }


        var questionsList:MutableList<Quiz> = mutableListOf()
//        var topic: Topic


//        //Math
//        questionsList = mutableListOf()
//        for (question in arrayOf(
//            arrayOf("What's 2+2", "4", "1", "3", "2", "1"),
//            arrayOf("what is the area under a function called", "derivative", "integral", "area", "perimeter", "2"),
//            arrayOf("What is the type of equilibrium that moves", "static", "moving", "dynamic", "chaotic", "3"),
//            arrayOf("Who created calculus", "Euler", "Bernoulli", "Pythagorous", "Newton", "4"),
//            arrayOf("What property describes x(y + z) = xy + xz", "Commutative", "Associative", "Transitive", "Distributive", "4"))) {
//            val newQuestion = Quiz(question[0], question.slice(1 until question.size - 1).toList() as MutableList , question.last().toInt())
//            questionsList.add(newQuestion)
//        }
//
//        topic = Topic("Math", "Test your skills across various math concepts", "This math quiz contains mathematic" +
//                "concepts up to a college-level including some historical facts behind famous mathematicians.", questionsList)
//        Log.i(TAG, "${topic.getNumQuestions()}")
//        topics.add(topic)
//
//        // Physics
//
//        questionsList = mutableListOf()
//        for (question in arrayOf(
//            arrayOf("What's the difference between speed and velocity?", "they mean the same thing", "speed's a vector and velocity's a scalar", "speed's a scalar and velocity's a vector", "velocity can only be positive", "3"),
//            arrayOf("Which of the following is NOT one of Newton's law of motion?", "moving equates to doing work", "force is equal to mass times acceleration", "an object in motion stays in motion", "every action has an opposite or equal reaction", "1"),
//            arrayOf("What phenomenon describes how screwdrivers are better than hand screwing", "angular momentum", "leverage", "grip", "chaotic", "2"),
//            arrayOf("What is a tool that refracts light into visible wavelengths", "square", "lens", "light splitter", "prism", "4"),
//            arrayOf("Which of the following describes work", "Thinking about your homework","Isometric exercises","Standing in place with a box","Carrying a box across the street", "4"),
//            arrayOf("What phenomenon prevents everything from sliding all around the place", "Gravity", "Friction", "Glue", "Weight", "2"))) {
//            val newQuestion = Quiz(
//                question[0],
//                question.slice(1 until question.size - 1).toList() as MutableList,
//                question.last().toInt()
//            )
//            questionsList.add(newQuestion)
//        }
//        topic = Topic("Physics", "Learn more about real-life phenomenon", "This math quiz contains physics concepts related to motion," +
//                " kinematics, magnetism, and gas laws.", questionsList)
//        Log.i(TAG, "${topic.getNumQuestions()}")
//        topics.add(topic)
//
//        // Superheroes
//        questionsList = mutableListOf()
//        for (question in arrayOf(
//            arrayOf("How did spider-man get his powers?", "He was born with them", "He drank a spider-mutant potion", "radioactive spider bite","exposed to gamma radiation", "3"),
//            arrayOf("Which actor plays Thor in all Marvel Studio Movies?", "Chris Hemsworth", "Luke Hemsworth", "Chris Evans", "Robert Downy Jr.", "1"),
//            arrayOf("What is Iron Man's AI system called?", "Karen", "Jarvis", "Vision", "Candace", "2"),
//            arrayOf("Which team was the Black Panther a part of in Civil War?", "Team Captain America", "Team Hulk", "Independent", "Team Iron-Man", "4"),
//            arrayOf("Who held the time stone for most of the MCU movies?", "Thanos","Dr. Strange","Gamora","Vision", "2"),
//            arrayOf("What is Wolverine's real name?", "Thomas", "Xavier", "Logan", "Hank", "3"),
//            arrayOf("What is Daredevil's occupation aside from crime fighting?", "Lawyer", "Doctor", "Policeman", "Corporate CEO", "1"))) {
//            val newQuestion = Quiz(question[0], question.slice(1 until question.size - 1).toList() as MutableList , question.last().toInt())
//            questionsList.add(newQuestion)
//        }
//        topic = Topic("Marvel Superheroes", "How big of a marvel fan are you?", "This superheroes quiz is about all the Marvel Superhero" +
//                " comic books,and movies that are and not produced by Marvel Studios", questionsList)
//        Log.i(TAG, "${topic.getNumQuestions()}")
//        topics.add(topic)

}


data class Quiz(val text: String, val answers: MutableList<String>, val correctAnswer: Int)

data class Topic(val title: String, val shortDesc: String, val longDesc: String, val questions: List<Quiz>) {
    fun getNumQuestions():Int {
        return questions.size
    }
}

class QuizApp: Application() {
    lateinit var quizRepository : ITopicRepository
    val TAG = "QuizApp"
    lateinit var selectedTopic: Topic
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Quiz App Created")
        quizRepository = TopicRepository(this)
    }

    fun getRepo():TopicRepository {
        return quizRepository as TopicRepository
    }
}
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    lateinit var topics: List<Topic>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainActivity = this
        val executor: Executor = Executors.newSingleThreadExecutor()
        val quizApp = (application as QuizApp)
        executor.execute {
            val url = URL("http", "tednewardsandbox.site44.com", 80, "/questions.json")
            val urlConnection = url.openConnection() as HttpURLConnection
            val inputStream = urlConnection.inputStream
            val reader = InputStreamReader(inputStream)
            reader.use {
                val jsonObj = JSONArray(it.readText())
                Log.i(TAG, "Retrieved file and converted to JSON $jsonObj")
                quizApp.getRepo().addTopics(jsonObj)

                mainActivity.runOnUiThread {
                    val repo = quizApp.getRepo()
                    topics = repo.getAll()
                    Log.i("QuizDroid", "Topics: ${topics}")
                    val quizCategories: (List<Topic>) -> List<Pair<String, String>> =
                        { topics -> topics.map { Pair(it.title, it.shortDesc) } }

                    val listView = findViewById<ListView>(R.id.quizList)
                    val arrayAdapter = TopicAdapter(this, quizCategories(topics))
                    listView.adapter = arrayAdapter

                    listView.setOnItemClickListener { parent, view, position, id ->
                        val clicked = parent.getItemAtPosition(position) as Pair<String, String>
                        Log.i("QuizDroid", "clicked item ${clicked}")
                        (application as QuizApp).selectedTopic = topics[position]
                        var intent = Intent()
                        when (clicked.first) {
                            "Math" -> intent = Intent(this, MathActivity::class.java)
                            "Physics" -> intent = Intent(this, PhysicsActivity::class.java)
                            "Marvel Superheroes" -> intent =
                                Intent(this, SuperheroesActivity::class.java)
                        }
                        startActivity(intent)
                    }
                    // Iterate over the items in the list, creating the question objects and putting them in a topic object
                }


            }
        }
    }
}


class TopicAdapter(context: Context, data:List<Pair<String,String>>) : ArrayAdapter<Pair<String,String>>(context, R.layout.topic_list_item, data) {
    val TAG = "TopicAdapter"
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(R.layout.topic_list_item, parent, false)

        val item = getItem(position) as Pair<String, String>
        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewSubheading = view.findViewById<TextView>(R.id.textViewSubheading)
        Log.i(TAG, "${item.first}")
        Log.i(TAG, "${item.second}")
        textViewTitle.text = item.first
        textViewSubheading.text = item.second

        return view
    }
}