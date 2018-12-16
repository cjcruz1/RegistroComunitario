package com.grupo4.encuesta

/* The purpose of this class is to have an object that collects all the necessary information about
* the survey respondent that way it is easier to display the list of all survey respondents in the
* detail/master flow activity (MainActivity/AppHomeScreen) and tabulate the survey respondent information
* when an item is selected.*/

class EncuestaItem {

    // Attributes
    public var onDataBase :Boolean            // boolean flag indicating if this encuesta was uploaded to the data base
    public var name :String                   // name leader of the community or the home owner
    public var date :String                   // date of when the encuesta was answered
    public var time :String                   // time of when the encuesta was answered
    public var gpsAddress  :String            // User address obtained by the GPS
    public var userAddress :String            // User address self provided (Comunidad + municipio + zip-code)
    public var encuestaType :String           // Type of questionnaire (questionnaire for community or home)
    public var phoneNum :String               // Contact info
    public var answers = mutableListOf<String>() // Container for holding the person's answers


    // When an EncuestaItem is created it will initialize its attributes to the following values:
    init {
        onDataBase = false
        name = ""
        date = ""
        time = ""
        gpsAddress = ""
        userAddress = ""
        encuestaType = ""
        phoneNum = ""
    }
}