package edu.uw.ischool.lton2.quizdroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 * Use the [TitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TitleFragment : Fragment(R.layout.fragment_title) {

    private var data: Bundle? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        data = arguments
        return inflater.inflate(R.layout.fragment_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view.findViewById<TextView>(R.id.txtDescription).setText(data?.getString("description"))
        view.findViewById<TextView>(R.id.txtNumQuestions).setText("Number of questions: ${data?.getInt("numQuestions")}")
        var quizFragment = QuizFragment()
        quizFragment.arguments = data
        view.findViewById<Button>(R.id.btnBegin).setOnClickListener {
            Log.i("Title Fragment", "begin button pressed")
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.flFragment, quizFragment)
                Log.i("Title Fragment","moving to quiz section")
                commit()
            }
        }
    }
}