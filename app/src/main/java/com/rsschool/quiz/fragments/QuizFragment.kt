package com.rsschool.quiz.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.rsschool.quiz.R
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.db.Constants
import com.rsschool.quiz.db.Question
import kotlinx.android.synthetic.main.fragment_quiz.*
import java.util.*


class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val args: QuizFragmentArgs by navArgs()

    private var mQuestionsList: ArrayList<Question>? = null
    private var mAnswersList = intArrayOf()

    private var mSelectedOptionPosition: Int = -1
    private var mSelectedOption: String = ""
    private var currentQuestion: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentQuestion = args.currentQuestion
        //НАЗНАЧАЮ ТЕМУ ПРИ СОЗДАНИИ ФРАГМЕНТА
        setTheme()
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        //НАЗНАЧИЛИ ВОПРОС
        mQuestionsList = Constants.getQuestions()

        mAnswersList = args.answersArray
        showQuestion(mAnswersList[currentQuestion])
        checkVisibilityOfButton()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            previousQuestion()
        }
        binding.apply {
            // Меню в toolbar
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_test -> {
                        view?.findNavController()
                            ?.navigate(QuizFragmentDirections.actionQuizFragmentToStartFragment3())
                        true
                    }
                    else -> true
                }
            }
            // кнопка Вверх
            toolbar.setNavigationOnClickListener {
                previousQuestion()
            }
            //Если выбран вариант ответа в RadioGroup
            radioGroup.setOnCheckedChangeListener { _, _ ->
                nextButton.isEnabled = true
            }
            //...................НАЖАТИЕ ДАЛЕЕ
            nextButton.setOnClickListener {
                if (currentQuestion != Constants.getQuestions().size - 1) {
                    getNumberOfAnswers()
                    if (mSelectedOptionPosition != -1) {
                        mAnswersList[currentQuestion] = mSelectedOptionPosition
                        currentQuestion++
                        view?.findNavController()?.navigate(
                            QuizFragmentDirections.actionQuizFragmentSelf(
                                mAnswersList, currentQuestion
                            )
                        )
                    } else Toast.makeText(requireContext(), "Check answer", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    getNumberOfAnswers()
                    mAnswersList[currentQuestion] = mSelectedOptionPosition
                    view?.findNavController()?.navigate(
                        QuizFragmentDirections.actionQuizFragmentToResultFragment(mAnswersList)
                    )
                }
            }
            //...............НАЖАТИЕ НАЗАД
            previousButton.setOnClickListener {
                previousQuestion()
            }
        }
        return binding.root
    }

    private fun previousQuestion() {
        if (currentQuestion != 0) {
            currentQuestion--
            view?.findNavController()?.navigate(
                QuizFragmentDirections.actionQuizFragmentSelf(
                    mAnswersList, currentQuestion

                )
            )
        }
    }

    //выбор темы
    private fun setTheme() {
        val theme: Int
        val color: Int
        when (currentQuestion) {
            0 -> {
                theme = R.style.Theme_Quiz_First
                color = R.color.quiz_first_statusBarColor
            }
            1 -> {
                theme = R.style.Theme_Quiz_Second
                color = R.color.quiz_second_statusBarColor
            }
            2 -> {
                theme = R.style.Theme_Quiz_Three
                color = R.color.quiz_three_statusBarColor
            }
            3 -> {
                theme = R.style.Theme_Quiz_Fourth
                color = R.color.quiz_fourth_statusBarColor
            }
            else -> {
                theme = R.style.Theme_Quiz_Fifth
                color = R.color.quiz_fifth_statusBarColor
            }
        }
        requireContext().setTheme(theme)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), color)
    }

    @SuppressLint("ResourceType")
    private fun showQuestion(answer: Int) {
        binding.radioGroup.clearCheck()
        checkVisibilityOfButton()

        val question = mQuestionsList!!.get(currentQuestion)
        binding.questionTv.text = question.question
        binding.optionOne.text = question.answers?.get(0) ?: ""
        binding.optionTwo.text = question.answers?.get(1) ?: ""
        binding.optionThree.text = question.answers?.get(2) ?: ""
        binding.optionFour.text = question.answers?.get(3) ?: ""
        binding.optionFive.text = question.answers?.get(4) ?: ""
        binding.toolbar.title = getString(
            R.string.title_android_trivia_question,
            currentQuestion + 1,
            mQuestionsList?.size
        )

        binding.apply {
            if (currentQuestion == mQuestionsList?.size?.minus(1))
                nextButton.text = "Submit"
            else
                nextButton.text = "Next"

            when (answer) {
                0 -> radioGroup.check(R.id.option_one)
                1 -> radioGroup.check(R.id.option_two)
                2 -> radioGroup.check(R.id.option_three)
                3 -> radioGroup.check(R.id.option_four)
                4 -> radioGroup.check(R.id.option_five)
            }
        }
    }

    private fun getNumberOfAnswers() {
        mSelectedOptionPosition = binding.radioGroup.checkedRadioButtonId

        if (-1 != mSelectedOptionPosition) {

            when (mSelectedOptionPosition) {
                R.id.option_one -> mSelectedOptionPosition = 0
                R.id.option_two -> mSelectedOptionPosition = 1
                R.id.option_three -> mSelectedOptionPosition = 2
                R.id.option_four -> mSelectedOptionPosition = 3
                R.id.option_five -> mSelectedOptionPosition = 4
            }
        }
        if (-1 != mSelectedOptionPosition) {

            when (mSelectedOptionPosition) {
                R.id.option_one -> mSelectedOption = binding.optionOne.text.toString()
                R.id.option_two -> mSelectedOption = binding.optionTwo.text.toString()
                R.id.option_three -> mSelectedOption = binding.optionThree.text.toString()
                R.id.option_four -> mSelectedOption = binding.optionFour.text.toString()
                R.id.option_five -> mSelectedOption = binding.optionFive.text.toString()
            }
        }
    }

    fun checkVisibilityOfButton() {
        getNumberOfAnswers()
        if (currentQuestion == 0) {
            binding.toolbar.navigationIcon = null
            binding.previousButton.isEnabled = false
        }
        binding.nextButton.isEnabled = mSelectedOptionPosition != -1
        if (currentQuestion == mQuestionsList?.size?.minus(1))
            binding.nextButton.text = getString(R.string.button_next2)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}