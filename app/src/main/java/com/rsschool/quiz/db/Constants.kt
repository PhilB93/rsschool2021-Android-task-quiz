package com.rsschool.quiz.db

object Constants{

    fun getQuestions(): ArrayList<Question>{
        val questionsList=ArrayList<Question>()

        // 1
        val que1=Question(1,
            "1 + 1",

            arrayListOf("2",
                "3",
                "4",
                "5",
                "6"),
            0
        )
        questionsList.add(que1)
        // 2
        val que2 = Question(
            2, "8 + 5",

            arrayListOf(  "1", "3",
            "13", "5","9"), 2
        )

        questionsList.add(que2)

        // 3
        val que3 = Question(
            3, "6 - 5",

            arrayListOf("6", "2",
            "7", "1","9"), 3
        )

        questionsList.add(que3)

        // 4
        val que4 = Question(
            4, "0 + 4?",

            arrayListOf("9", "4",
            "7", "5","1"), 1
        )

        questionsList.add(que4)

        // 5
        val que5 = Question(
            5, "8 + 6",

            arrayListOf("10", "13",
            "14", "18","87"), 2
        )

        questionsList.add(que5)


        return questionsList

    }
}