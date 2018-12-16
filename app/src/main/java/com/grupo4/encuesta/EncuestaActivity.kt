package com.grupo4.encuesta

import android.app.Activity
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.widget.*
import android.widget.TextView
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent




class EncuestaActivity : AppCompatActivity(), View.OnClickListener {

    // Questions Data Member
    private var questionnaire = Questions()    // Object that contains Questions with options

    // UI Data Members
    private lateinit var questionTextView: TextView
    private lateinit var counterTextView: TextView
    private lateinit var ll : LinearLayout
    private var buttons = mutableListOf<Button>()
    private var textViews = mutableListOf<TextView>()
    private var plainTexts = mutableListOf<EditText>()
    private lateinit var backButton: Button
    private lateinit var cancelButton: Button

    // Other Data Members
    private var qNumber: Int = 0 // Number of question being answered

    // Creates an array of strings for storing user answers with size equal to the amount of questions
    private var answers :Array<String?> = arrayOfNulls<String?>(questionnaire.numberOfQuestions())

    // Data members for storing in memory the results once the entire encuesta has been answered.
    private lateinit var mPreferences : SharedPreferences
    private lateinit var mEditor :SharedPreferences.Editor


    // This function is called if back, cancel, next or any other button is pressed
    override fun onClick(v: View?) {
        val id = v!!.id
        when (id) {

            /* 0 is executed when back button is pressed*/
            0 -> { prevQuestion()
            }

            /* 1 is executed when cancel button is pressed*/
            1 -> { finish()
            }

            /* 2 is executed when next button is pressed*/
            2 -> {
                captureAnswersFromTextViews()
                nextQuestion()
            }

            // else is executed when any other button on the screen is pressed
            // It only enters here if is the current question is a multiple choice question
            else -> {
                captureAnswerFromMultipleChoice(id)
                nextQuestion()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encuesta)

        // Initialize data members
        for (i in 0 until answers.size)
            answers[i] = ""
        findViewsByIDs()

        // Update the UI to have the question to be answered with
        // the corresponding button choices or textViews
        updateScreen()

        // All the answers are handled by the onClick method
    }

    /********************** User Interface Logic ****************************/

    // Update the screen to contain new questions and its logic(multiple choice or fill the blank)
    private fun updateScreen() {

        // Remove all the buttons, textviews, layouts, etc from the screen
        cleanScreen()

        /*Add to the screen:
        *   Preguntas por contestar: Counter
        *   The Question*/
        addHeader()

        // If the question is of type MultipleChoice, then update the screen to have buttons to
        // simulate a multiple choice question
        if (questionnaire.isMultipleChoice(qNumber)){
            multipleChoiceScreen()
        }

        // Otherwise, add textviews and edittext so the user can provide the answer
        else{
            fillTheBlankScreen()
        }

        // Set onClickListeners for all the current buttons on the screen
        setOnClickListeners()

    }

    // Move to previous question
    private fun prevQuestion() {

        if(qNumber == 0)
            Toast.makeText(this, "Estás en la primera pregunta.", Toast.LENGTH_SHORT).show()

        else{
            qNumber--       // Move to previous question
            updateScreen()
        }
    }


    // Move to the next QUESTION
    private fun nextQuestion() {
        qNumber++

        // if the user answered the last question, then save it and go back to the main screen activity (itemListActivity)
        if (qNumber == questionnaire.numberOfQuestions() - 1){

            // Get the date and time of when the encuesta was finished
            val sdf = SimpleDateFormat("dd/MMM/yyyy hh:mm")
            val currentDate = sdf.format(Date())
            answers[qNumber] = "Date:@" + currentDate + "#"

            // Save the answers in memory and close this activity
            managePreferences()

                        /* Remove comment if you desire to know how the answers are being stored in memory
                        var accumulator: String = ""
                        for (i in 0 until answers.size){
                            accumulator += answers[i]
                        }

                        println(accumulator)
                        */
            val returnIntent = Intent()
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        // if qNumber is the last question, then remind him
        else if(qNumber == questionnaire.numberOfQuestions() - 2){
            Toast.makeText(this, "Estás en la última pregunta.", Toast.LENGTH_SHORT).show()
        }
        updateScreen()
    }

    // Initialize find view by ids
    private fun findViewsByIDs(){
        questionTextView = findViewById(R.id.questionTextView) as TextView
        counterTextView = findViewById(R.id.counterTextView) as TextView
        counterTextView.setText((questionnaire.numberOfQuestions()).toString())
        ll = findViewById(R.id.LinearLayoutID)
        backButton = findViewById(R.id.backButton) as Button
        cancelButton = findViewById(R.id.cancelButton) as Button
        // object received from the previous activity
    }

    // Create a fill the blank screen
    private fun fillTheBlankScreen(){

        val numberOfOptions :Int = questionnaire.numberOfOptions(qNumber) // This tells me how many textviews and plaintext I need to create
        val width :Int = LinearLayout.LayoutParams.MATCH_PARENT
        val height :Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val params :LinearLayout.LayoutParams = LinearLayout.LayoutParams(width, height)


        for (i in 0 until numberOfOptions){
            textViews.add(TextView(this))
            textViews[i].setText(questionnaire.options[qNumber][i])
            textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            textViews[i].layoutParams = params
            ll.addView(textViews[i])


            plainTexts.add(EditText(this))
            plainTexts[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            plainTexts[i].layoutParams = params
            ll.addView(plainTexts[i])

        }

        addBottomButtons2()
    }

    // Create a multiple choice screen
    private fun multipleChoiceScreen(){

        val numberOfOptions :Int = questionnaire.numberOfOptions(qNumber) // This tells me how many buttons I need to create
        val width :Int = LinearLayout.LayoutParams.MATCH_PARENT
        val height :Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val params1 :LinearLayout.LayoutParams = LinearLayout.LayoutParams(width, height)

        for (i in 0 until numberOfOptions){
            buttons.add(Button(this))
            buttons[i].setText(questionnaire.options[qNumber][i])
            buttons[i].layoutParams = params1
            buttons[i].id = i + 3
            ll.addView(buttons[i])
            // if you wish to change button styles it needs to be done here as well
        }

        addBottomButtons1()
    }

    // Remove every child from the linear layout and clean the arrays that contains them
    private fun cleanScreen(){

        ll.removeAllViews()

        //
        var tmp = buttons.size
        for (i in 0 until buttons.size)
            buttons.removeAt(tmp - i -1)

        tmp = textViews.size
        for (i in 0 until textViews.size)
            textViews.removeAt(tmp - i -1)

        tmp = plainTexts.size
        for (i in 0 until plainTexts.size)
            plainTexts.removeAt(tmp - i -1)
    }

    // Add the header. (Preguntas por contestar, counter and question
    private fun addHeader(){

        // In the relative layout goes tv1 and tv2
        // Set up relative layout
        val rl :RelativeLayout = RelativeLayout(this)
        val params1 = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        rl.layoutParams = params1

        // tv1 is the textView for Preguntas por contestar string literal.
        // Set up tv1 and add it to the rl (relative layout)
        val tv1 : TextView = TextView(this)
        val params2 = FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        tv1.text = "Preguntas por contestar: "//questionnaire.getQuestion(qNumber)
        tv1.layoutParams = params2
        rl.addView(tv1)

        // tv2 is the counter for the user to know how many questions he needs to answer before he finishes
        // Set up tv2 and add it to the rl
        val tv2 : TextView = TextView(this)
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        tv2.text = (questionnaire.numberOfQuestions()- qNumber - 1).toString() // Update counter
        tv2.layoutParams = params2
        tv2.setPadding(800, 0,0 , 0)
        rl.addView(tv2)

        // Add the relative layout to the linear layout (Parent Layout of the activity)
        ll.addView(rl)

        // tv3 is the textView for the Question to be answered by the user
        // Set up tv3
        val tv3 : TextView = TextView(this)
        val params3 = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
        tv3.text = questionnaire.getQuestion(qNumber)
        tv3.layoutParams = params3

        // Add the tv3 (question) to the linear layout (Parent layout of the activity)
        ll.addView(tv3)
    }


    // Add back and cancel buttons at the bottom of the screen
    // This function is used for multiple choice questions
    private fun addBottomButtons1(){

        val numberOfOptions :Int = questionnaire.numberOfOptions(qNumber)

        // Create layout for the back, cancel, next buttons
        val ll2 :LinearLayout = LinearLayout(this)
        ll2.orientation = LinearLayout.HORIZONTAL
        val params1 = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        ll2.layoutParams = params1


        // Obtain phone screen width and to set the width and height of the buttons
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width2 = displayMetrics.widthPixels/2
        val height2 :Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val params2 :LinearLayout.LayoutParams = LinearLayout.LayoutParams(width2, height2)
        params2.gravity = Gravity.BOTTOM

        val lastTwoButtons = listOf<String>("Back", "Cancel")
        var index = 0

        // Create and insert the buttons
        for (i in numberOfOptions until numberOfOptions + 2){ //+ 2 because we are about to add 2 buttons
            buttons.add(Button(this))
            buttons[i].setText(lastTwoButtons[index])
            buttons[i].id = index// IDs 0, 1 are saved for back, and cancel buttons
            buttons[i].layoutParams = params2
            ll2.addView(buttons[i])
            index++
            // If you wish to change the style of the button it needs to be done in this loop
        }

        ll.addView(ll2)
    }

    // Add back, cancel, next buttons at the bottom of the screen
    // This function is used for fill the blank screen
    private fun addBottomButtons2(){

        val numberOfOptions :Int = questionnaire.numberOfOptions(qNumber)

        // Create layout for the back, cancel, next buttons
        val ll2 :LinearLayout = LinearLayout(this)
        ll2.orientation = LinearLayout.HORIZONTAL
        val params1 = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        ll2.layoutParams = params1


        // Obtain phone screen width and to set the width and height of the buttons
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width2 = displayMetrics.widthPixels/3
        val height2 :Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val params2 :LinearLayout.LayoutParams = LinearLayout.LayoutParams(width2, height2)
        params2.gravity = Gravity.BOTTOM

        val lastThreeButtons = listOf<String>("Back", "Cancel", "Next")
        var index = 0

        // Create and insert the buttons
        for (i in 0 until 3){
            buttons.add(Button(this))
            buttons[i].setText(lastThreeButtons[index])
            buttons[i].id = i // IDs 0, 1, 2 are saved for next, back and cancel
            buttons[i].layoutParams = params2
            ll2.addView(buttons[i])
            index++
            // If you wish to change the style of the button it needs to be done in this loop
        }

        ll.addView(ll2)
    }

    // Create a setOnClickListener for every button on the screen
    private fun setOnClickListeners(){

        for (i in 0 until buttons.size){
            buttons[i].setOnClickListener(this)
        }
    }
    /********************** End of User Interface Logic ****************************/


    /********************* Capturing User Answers **********************************/

    // This function is used to capture answers from a screen that is fillTheBlankScreen
    private fun captureAnswersFromTextViews(){

        answers[qNumber] = "" // This is done to reset the answer for that question incase the user pressed back
        val question :String = questionnaire.getQuestion(qNumber)

        answers[qNumber] = questionnaire.getQuestion(qNumber) + "@"
        for (i in 0 until  textViews.size){
            answers[qNumber] += textViews[i].text.toString() + "@" + plainTexts[i].text.toString() + "@"
        }
        answers[qNumber] += "#"
    }

    // This function is used when the user answers a multiple choice question
    // It looks for which choice was selected then saves it in a container
    private fun captureAnswerFromMultipleChoice(id :Int){

        // search which button was clicked
        var i :Int = 0
        while(i < buttons.size - 2) {
            if (id == buttons[i].id) {
                break
            }
            i++
        }

        // Store answer
        answers[qNumber] += questionnaire.getQuestion(qNumber) + "@" + buttons[i].text.toString() + "#"
    }
    /********************* End of Capturing User Answers **********************************/

    // Save the Answers
    private fun managePreferences(){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        mEditor = mPreferences.edit()
        var accumulator: String = ""
        for (i in 0 until answers.size){
            accumulator += answers[i]
        }
        val n = countPref(mPreferences)
        mEditor.putString("Encuesta" + n.toString(), accumulator)
        mEditor.apply()
        println(n)
    }

    private fun countPref(p :SharedPreferences) :Int{
        var index :Int = 0
        var answer :String?
        var key :String

        while (true){

            key = "Encuesta" + index.toString()
            answer = mPreferences.getString(key, "false")

            // if true is because there is nothing in memory by the name key or it read every encuesta
            if (answer == "false")
                break
            index++
        }

        return index
    }
}