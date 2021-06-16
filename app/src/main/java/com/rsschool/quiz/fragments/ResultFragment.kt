package com.rsschool.quiz.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.rsschool.quiz.R
import com.rsschool.quiz.databinding.FragmentResultBinding
import com.rsschool.quiz.db.Constants
import com.rsschool.quiz.db.Question
import java.lang.StringBuilder
import java.util.ArrayList

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var questions: MutableList<Question>
    private var mAnswersList: IntArray = intArrayOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().setTheme(R.style.Theme_Quiz_First)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.quiz_first_statusBarColor)
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        val args = ResultFragmentArgs.fromBundle(requireArguments())
        questions = Constants.getQuestions().toMutableList()
        mAnswersList = args.answersArray

        //Системная кнопка назад
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            view?.findNavController()
                ?.navigate(ResultFragmentDirections.actionResultFragmentToStartFragment3())
        }

        binding.apply {

            // Показать результат
            "${rightAnswers()} / ${questions.size} ".also { textViewResult.text = "Your result: $it"}

            buttonBack.setOnClickListener {
                view?.findNavController()
                    ?.navigate(ResultFragmentDirections.actionResultFragmentToStartFragment3())
            }

            buttonExit.setOnClickListener {
                ActivityCompat.finishAffinity(requireActivity())
            }

            buttonShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, createMessage())
               intent.setType("text/plain").putExtra(Intent.EXTRA_SUBJECT, "Quiz results")
                startActivity(intent)
            }

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createMessage(): String {
        var count = 1
        var starter = 0
        val stringBuilder = StringBuilder("")
        return stringBuilder.apply {
            append("Your result: ${rightAnswers()} из ${questions.size} \n\n")
            for (question in questions) {
                append(
                    "${count++}) ${question.question}\n" +
                            "Your answer: ${question.answers?.get(mAnswersList[starter++])} \n\n"
                )
            }
        }.toString()


    }

    private fun rightAnswers(): Int {
        var rightAnswersCount = 0
        for (i in 0 until mAnswersList.size)
        {
            if (mAnswersList[i] == questions[i].correctAnswer)
                rightAnswersCount++

        }
return rightAnswersCount
    }



}