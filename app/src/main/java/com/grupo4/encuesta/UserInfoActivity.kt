package com.grupo4.encuesta

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class UserInfoActivity : AppCompatActivity() {

    // Activity Attributes
    private lateinit var encuestaItem: EncuestaItem
    private lateinit var nameEditText :EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var leaderRadioButton: RadioButton
    private lateinit var indvRadioButton: RadioButton
    private lateinit var dir1EditText: EditText
    private lateinit var dir2EditText: EditText
    private lateinit var puebloEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var role: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        // Ask for permission to obtain the user's location
        requestPermission()

        // Initialize Attributes
        radioGroup = findViewById(R.id.radioGroup)
        leaderRadioButton = findViewById(R.id.leaderRadioButton)
        indvRadioButton = findViewById(R.id.indvRadioButton)
        nameEditText = findViewById(R.id.nameEditText)
        dir1EditText = findViewById(R.id.dir1EditText)
        dir2EditText = findViewById(R.id.dir2EditText)
        puebloEditText = findViewById(R.id.puebloEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        continueButton = findViewById(R.id.continueButton)
        role = "individuo" // default value

        // Define what happens when the continueButton is pressed
        continueButton.setOnClickListener(){

            val valid :Boolean = validateInput()

            // if every editText (input field) contains valid info then move to next activity
            if (valid) {
                encuestaItem.name = nameEditText.text.toString()
                //encuestaItem.role = role
                encuestaItem.userAddress = dir1EditText.text.toString() + " " +  dir2EditText.text.toString() + " " + puebloEditText.text.toString()
                encuestaItem.phoneNum = phoneEditText.text.toString()
                encuestaItem.gpsAddress = obtainGPSAddress()
                /*Side note: The time and date attributes of encuestaItem are obtained after answering the last question of the next activity*/

                nextActivity()
            }

            else{
                //Toast.makeText(applicationContext, "Todos los campos son requeridos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function that defines what happens when a radioButton is pressed.
    public fun radioButtonAction( v :View) {

        val radioId :Int = radioGroup.checkedRadioButtonId // obtain the id of the checked button
        val radioButton :RadioButton = findViewById(radioId)

        when {
            radioButton.text == leaderRadioButton.text -> role = "líder"// Toast.makeText(applicationContext, "lider", Toast.LENGTH_SHORT).show()
            radioButton.text == indvRadioButton.text -> role = "individuo"//Toast.makeText(applicationContext, "Indv", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(applicationContext, "Error: No se pudo capturar role de los radioButtons.", Toast.LENGTH_SHORT).show()
        }

        /*It shouldn't be possible to get inside the else clause since one of the options
        * is selected by default but its of good practice to always end with an else to capture any other possible bugs*/

            //role ="Líder" // error aquí "Could not execute method for android:onClick", var4
            //role = "Individuo" //error aquí "Could not execute method for android:onClick", var4)
    }

    // Function that verifies that every editText has valid information
    public fun validateInput() :Boolean{

        // validate name
        val valid1 :Boolean = validateField(nameEditText.text.toString())

        // validate the address for both text fields
        val valid2 :Boolean = validateField(dir1EditText.text.toString() + dir2EditText.text.toString())

        // validate the pueblo
        val valid3 :Boolean = validateField(puebloEditText.text.toString())

        // validate the phone number
        val valid4 :Boolean = validatePhone(phoneEditText.text.toString())

        // Return true if all the above were true and false otherwise.
        return valid1 && valid2 && valid3 && valid4
    }


    public fun validateField( input :String) :Boolean{

        // if the input is an empty string then the user has not provided data yet.
        if (input == ""){
            return false
        }

        else {

            // Verify if the input contains at least 1 letter
            for (letter in input) {
                if (letter.isLetter()) {
                    return true
                }
            }

            // if no letter, then the input is either only digits, blank spaces or symbols
            return false
        }
    }


    public fun validatePhone(pNumber :String) :Boolean{

        if (pNumber == ""){
            return false
        }

        // If pNumber doesn't have 10 chars or 12 chars then is not a valid input
        // "0123456789".length == 10 "012-345-6789".length == 12
        else if (pNumber.length != 10 && pNumber.length != 12){
            return false
        }

        else{

            for (char in pNumber) {
                if (char == ' ' || char.isLetter()) {
                    Toast.makeText(applicationContext, "Solo números enteros y guiones son permitidos bajo teléfono.", Toast.LENGTH_SHORT).show()
                    return false
                }
            }

            return true
        }
    }

    public fun nextActivity(){

        val radioId :Int = radioGroup.checkedRadioButtonId
        val radioButton :RadioButton = findViewById(radioId)
        val intent = Intent(this, EncuestaActivity::class.java)

        when {
            radioButton.text == leaderRadioButton.text -> { // aquí va moverse para activity para lider
                intent.putExtra("key","something")
                startActivity(intent)
            }
            radioButton.text == indvRadioButton.text -> { // aquí va moverse para activity para individuo
                intent.putExtra("key", "something")
                startActivity(intent)
            }
            else -> Toast.makeText(applicationContext, "Error: No se pudo pasar a la próxima actividad.", Toast.LENGTH_SHORT).show()
        }
    }

    //////////////////////////////////////////////////////////////
    public fun obtainGPSAddress() :String {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // The condition is true if the user did not give permission to obtain its location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return "El permiso para obtener la localización fue denegado por el usuario."
        }

        var latitude :String = ""
        var longitude :String = ""

        //@SuppressLint("MissingPermission")
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    latitude = location?.latitude.toString()
                    longitude = location?.longitude.toString()
                }

        // The condition is true if the phone has no internet access to obtain location
        if (latitude == "null" || longitude == "null"){
            return "null"
        }

        // if latitude && longitude were not null, then change them to an address
        else {

            return "test obtainAdrress method is not finished"

        }
    }

    private fun requestPermission(){
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)
    }


}