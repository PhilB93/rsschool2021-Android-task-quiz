package com.rsschool.quiz.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
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
        //НАЗНАЧАЮ ТЕМУ ПРИ СОЗДАНИИ ФРАГМЕНТА
        requireContext().setTheme(setTheme())
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        //НАЗНАЧИЛИ ВОПРОС

        mQuestionsList = Constants.getQuestions()
        currentQuestion = args.currentQuestion
        mAnswersList = args.answersArray


        //............СТАНДАРТНЫЕ НАСТОЙКИ (ВОПРОС, СОТСОЯНИЕ КНОПОК ПЕРЕКЛЮЧЕНИЯ)
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

            // кнопка Вверх на toolbar
            toolbar.setNavigationOnClickListener {
                previousQuestion()
            }

            //Если выбран вариант ответа в RadioGroup
            radioGroup.setOnCheckedChangeListener { _, _ ->
                binding.nextButton.visibility = View.VISIBLE
            }

            //  Кнопка Previous
            previousButton.setOnClickListener {
                previousQuestion()
            }
        }


        //...................НАЖАТИЕ ДАЛЕЕ
        binding.nextButton.setOnClickListener {

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

                } else Toast.makeText(requireContext(), "Check answer", Toast.LENGTH_SHORT).show()
            } else {
                getNumberOfAnswers()
                mAnswersList[currentQuestion] = mSelectedOptionPosition


                view?.findNavController()?.navigate(
                    QuizFragmentDirections.actionQuizFragmentToResultFragment(mAnswersList)
                )
            }


        }

        //...............НАЖАТИЕ НАЗАД
        binding.previousButton.setOnClickListener {
            previousQuestion()
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
    private fun setTheme(): Int {
        return when (args.currentQuestion) {
            0 -> R.style.Theme_Quiz_First
            1 -> R.style.Theme_Quiz_Second
            2 -> R.style.Theme_Quiz_Three
            3 -> R.style.Theme_Quiz_Fourth
            else -> R.style.Theme_Quiz_Fifth
        }
    }

    @SuppressLint("ResourceType")
    private fun showQuestion(answer: Int) {
        binding.radioGroup.clearCheck()
        checkVisibilityOfButton()

        val question = mQuestionsList!!.get(currentQuestion)
        binding.question.text = question.question
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

    fun getNumberOfAnswers() {
        mSelectedOptionPosition = binding.radioGroup.checkedRadioButtonId

        // Do nothing if nothing is checked (id == -1)
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
        if (currentQuestion == 0) {
            binding.previousButton.visibility = View.INVISIBLE
            binding.toolbar.navigationIcon = null
        }
        else binding.previousButton.visibility = View.VISIBLE

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}