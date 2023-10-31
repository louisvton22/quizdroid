package edu.uw.ischool.lton2.quizdroid

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        data = arguments
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.questionNumber).setText("Question $questionNumber")
        val questions = data?.getSerializable("questions") as HashMap<Int, Array<String>>
        view.findViewById<TextView>(R.id.question).text = questions[questionNumber]?.get(0)

        val radioGroup = view.findViewById<RadioGroup>(R.id.options)
        view.findViewById<Button>(R.id.next).isEnabled = false
        for (i in 0..radioGroup.childCount) {
            val radio: RadioButton? = radioGroup.getChildAt(i) as? RadioButton
            radio?.text = questions[questionNumber]?.get(i+1)

        }
        radioGroup.setOnCheckedChangeListener {group, checkedId ->
            var checked  = view.findViewById<RadioButton>(checkedId)
            Log.i("Quiz Fragment", "option changed to: ${checked.text}")
            view.findViewById<Button>(R.id.next).isEnabled = true
        }

        view.findViewById<>()
        super.onCreate(savedInstanceState)
    }
}
