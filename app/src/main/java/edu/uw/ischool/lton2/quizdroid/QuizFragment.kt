package edu.uw.ischool.lton2.quizdroid

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import org.w3c.dom.Text
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment(R.layout.fragment_quiz) {
    var data: Bundle? = null
    var questionNumber: Int = 1
    var correct:Int = 0
    var answerId: Int = 0
    // toggle between displaying a question or answer
    var isAnswer: Boolean = false

    lateinit var quizApp:QuizApp
    lateinit var repo:ITopicRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //data = arguments
        quizApp = (activity?.application as QuizApp)
        repo = quizApp.getRepo()
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val questions = data?.getSerializable("questions") as HashMap<Int, Array<String>>
        val topic = repo.getTopic(quizApp.selectedTopic)
        val questions = topic?.questions
        updateView(view)
        val radioGroup = view.findViewById<RadioGroup>(R.id.options)
        val next: Button = view.findViewById(R.id.next)
        radioGroup.setOnCheckedChangeListener {group, checkedId ->
            var checked  = view.findViewById<RadioButton>(checkedId)
            answerId = checkedId
            Log.i("Quiz Fragment", "option changed to: ${checked.text}")
            next.isEnabled = true
        }
        // change question when next button is clicked
        next.setOnClickListener {
            if (next.text == "Finish") {
                Log.i("Quiz Fragment", "Returning to main screen")
                val intMain = Intent(activity, MainActivity::class.java)
                startActivity(intMain)
            }
            isAnswer = !isAnswer
            //handle if we're ready to finish

            if (isAnswer) {
                next.text = "Next"
                if (questionNumber == topic?.getNumQuestions()) {
                    Log.i("Quiz Fragment", "Returning to main screen")
                    next.text = "Finish"
                }
                Log.i("Quiz Fragment", "updating score")
                if (radioGroup.indexOfChild(view.findViewById<RadioButton>(answerId)) == questions?.get(questionNumber)?.correctAnswer!!-1) {
                    correct += 1
                    Log.i("Quiz Fragment", "correct")
                }
                updateView(view)
            } else  {
                //check if they answered the question correctly
                view.findViewById<RadioButton>(answerId).isChecked = false
                questionNumber += 1
            }
            updateView(view)
        }

        super.onCreate(savedInstanceState)
    }

    private fun updateView(view: View) {
        //val questions = data?.getSerializable("questions") as HashMap<Int, Array<String>>
        val questions = repo.getTopic(quizApp.selectedTopic)?.questions
        Log.i("QuizFragment", "Question: $questionNumber, ${questions?.get(questionNumber)}")
        if (isAnswer) {

            val radioButton = view.findViewById<RadioButton>(answerId)
            radioButton.setTextColor(Color.RED)
            val radioGroup = view.findViewById<RadioGroup>(R.id.options)
            val answer = radioGroup.getChildAt(questions?.get(questionNumber)?.correctAnswer!!-1) as RadioButton
            answer.setTextColor(Color.GREEN)
            for (i in 0..radioGroup.childCount) {
                val radio: RadioButton? = radioGroup.getChildAt(i) as? RadioButton
                radio?.isEnabled = false
            }
            if (questionNumber == questions.size) {
                view.findViewById<Button>(R.id.next).text = "Finish"
            } else {
                view.findViewById<Button>(R.id.next).text = "Next"
            }
        } else {
            view.findViewById<TextView>(R.id.questionNumber).setText("Question $questionNumber")
            //val questions = data?.getSerializable("questions") as HashMap<Int, Array<String>>
            val questions = repo.getTopic(quizApp.selectedTopic)?.questions
            view.findViewById<TextView>(R.id.question).text = questions?.get(questionNumber)?.text

            val radioGroup = view.findViewById<RadioGroup>(R.id.options)
            view.findViewById<Button>(R.id.next).isEnabled = false
            for (i in 0..radioGroup.childCount) {
                val radio: RadioButton? = radioGroup.getChildAt(i) as? RadioButton
                //radio?.text = questions[questionNumber]?.get(i + 1)
                radio?.text = questions?.get(questionNumber)?.answers?.get(i)
                radio?.setTextColor(Color.BLACK)
                radio?.isEnabled = true
            }
            view.findViewById<Button>(R.id.next).text = "Submit"
        }
        if (questionNumber >= 1) {
            if (questions != null) {
                view.findViewById<TextView>(R.id.correct).text =
                    "You've answered $correct of out ${questions.size} correctly"
            }
        } else {
            view.findViewById<TextView>(R.id.correct).text = ""
        }
    }
}
