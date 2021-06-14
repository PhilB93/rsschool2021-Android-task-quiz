package com.rsschool.quiz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.rsschool.quiz.databinding.FragmentStartBinding


class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)


        //Системная кнопка назад
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            ActivityCompat.finishAffinity(requireActivity())
        }


        binding.buttonStart.setOnClickListener() {
             val mAnswersList: IntArray = intArrayOf(-1, -1, -1, -1, -1)

            view?.findNavController()
                ?.navigate(StartFragmentDirections.actionStartFragment3ToQuizFragment(mAnswersList, 0))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}