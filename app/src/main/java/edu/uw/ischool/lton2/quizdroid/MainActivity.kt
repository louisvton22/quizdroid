package edu.uw.ischool.lton2.quizdroid

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import kotlinx.coroutines.Dispatchers
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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
        topics.removeAll(topics)
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
    lateinit var settings: Pair<String, Int>
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Quiz App Created")
        quizRepository = TopicRepository(this)
    }

    fun getRepo():TopicRepository {
        return quizRepository as TopicRepository
    }

    fun setSetttings(settings:Pair<String, Int>) {
        this.settings = settings
    }
}
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    lateinit var topics: List<Topic>
    val mainActivity = this
    val mainHandler = Handler(Looper.getMainLooper())
    lateinit var quizApp: QuizApp
    val executor = Executors.newSingleThreadScheduledExecutor()
    val defaultSettings = Pair<String, Int>("tednewardsandbox.site44.com/questions.json", 1)
//    val defaultSettings = Pair<String, Int>("google.com/404", 1)
    val getFileRunnable = object : Runnable {
        override fun run() {
            try {
                Log.i("Side Thread", "fetching url")
                Log.i("Side Thread", "Retrieving file at ${filesDir}/questions.json")
                val outputFile = File(filesDir.toString() + "/questions.json").createNewFile()
                Log.i("Side Thread", "$outputFile")
                val split: (String) -> Pair<String, String>  = {
                    val file = it.slice(it.lastIndexOf("/")..<it.length)
                    val path = it.slice(0..<it.lastIndexOf("/"))
                    Pair(path, file)
                }
                Log.i("Side Thread", "${split(defaultSettings.first).toString()}")
                val resources = split(quizApp.settings.first)
                val url = URL("http",resources.first, 80, resources.second)
                // Throw IOException if url does not get an OK response
                val urlConnection = url.openConnection() as HttpURLConnection
                Log.i("Side thread", "bad url")
                // throw if download fails
                val inputStream = urlConnection.inputStream
                val reader = InputStreamReader(inputStream)
                // validates whether the input can be parsed into json or not
                // check if output file already exists.

                // Writes data retrieved from url to local file
                reader.use {
                    val input = it.readText()
                    PrintWriter(FileOutputStream(filesDir.toString()+"/questions.json", false))
                        .use {
                            it.print(input)
                            it.close()
                        }
                    it.close()
                }
                inputStream.close()
                urlConnection.disconnect()
                // read questions.json local file and update repo
                FileReader(filesDir.toString() + "/questions.json")
                    .use {
                        val jsonObj = JSONArray(it.readText())
                        Log.i(TAG, "Retrieved file and converted to JSON $jsonObj")
                        quizApp.getRepo().addTopics(jsonObj)
                    }

                // add delay for next update
                mainHandler.post {
                    Toast.makeText(mainActivity, "Downloading data from url: ${quizApp.settings.first}", Toast.LENGTH_LONG).show()
                    loadUI()
                }
            } catch (e: Exception) {
               handleIOException()
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "${defaultSettings.first}")

//        val executor: Executor = Executors.newSingleThreadExecutor()

        quizApp = (application as QuizApp)
        quizApp.settings = defaultSettings

//        var jsonObj:JSONArray


        executor.scheduleAtFixedRate(getFileRunnable, 0L, quizApp.settings.second * 1L, TimeUnit.MINUTES)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.i(TAG, "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG, "${item.title} item selected")
        val prefIntent = Intent(this, Preferences::class.java)
        startActivity(prefIntent)
        return super.onOptionsItemSelected(item)
    }

    fun loadUI() {
        val quizApp = (application as QuizApp)
        val repo = quizApp.getRepo()
        topics = repo.getAll()
        Log.i("QuizDroid", "Topics: ${topics}")
        val quizCategories: (List<Topic>) -> List<Pair<String, String>> =
            { topics -> topics.map { Pair(it.title, it.shortDesc) } }
        val listView = findViewById<ListView>(R.id.quizList)
        listView.adapter = null
        val arrayAdapter = TopicAdapter(this, quizCategories(topics))
        listView.adapter = arrayAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val clicked = parent.getItemAtPosition(position) as Pair<String, String>
            Log.i("QuizDroid", "clicked item ${clicked}")
            (application as QuizApp).selectedTopic = topics[position]
            var intent = Intent(this, MathActivity::class.java)
            startActivity(intent)
        }
    }

    fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.System.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

    // handle when there's no service or airplane mode is on
    // TODO: Need to handle case where network is good but url is bad
    fun handleIOException() {
        val builder = AlertDialog.Builder(mainActivity)
        builder.setTitle("No Internet Connection")
        val connectivityManager = mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetwork == null) {
            builder.setMessage("No signal available")
            builder.setNegativeButton("Close") {dialog, which ->
                dialog.dismiss()
            }
        }
        else if (isAirplaneModeOn(mainActivity)) {
            Log.i(TAG, "Airplane is on. Alerting")
            builder.setMessage("Airplane mode is currently on. Turn off in settings")
            builder.setNegativeButton("Close") {dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton("Open") {dialog, which ->
                val intent = Intent(Intent.ACTION_MAIN)
                intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting")
                startActivity(intent)
                dialog.dismiss()
            }


        } else {
            builder.setTitle("Download Failed")
            builder.setMessage("Download from invalid URL. Please try again or quit the application")
            builder.setPositiveButton("Retry") {dialog, which ->
                executor.execute(getFileRunnable)
                dialog.dismiss()
            }
            builder.setNegativeButton("Quit") {dialog, which ->
                dialog.dismiss()
                mainActivity.finish()
            }
        }
        mainHandler.post {
            builder.show()
        }
    }

    //handle when the download fails
    fun handleMalformedURLException() {
        val builder = AlertDialog.Builder(mainActivity)

        mainHandler.post {
            builder.show()
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