package com.rsschool.quiz.db
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import java.util.ArrayList

@Parcelize
data class Question (

    val id: Int,
    val question: String,
    val answers: ArrayList<String>?,
    val correctAnswer: Int,
):Parcelable
