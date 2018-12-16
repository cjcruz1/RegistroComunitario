package com.grupo4.encuesta
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.View
import com.grupo4.encuesta.R
import com.grupo4.encuesta.tabulacion
import kotlinx.android.synthetic.main.search_layout.*
import java.nio.channels.InterruptibleChannel

class Searchactivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)

        //val zipcode = findViewById<TextInputLayout>(R.id.searchInputIDz)
        //val comunidad = findViewById<TextInputLayout>(R.id.searchInputIDc)


       searchbuttonID.setOnClickListener{

           val intentTab = Intent(this, tabulacion::class.java)
           //Code to introduce

           intentTab.putExtra("zipcode", searchInputIDz.text.toString())
           intentTab.putExtra("comunidad", searchInputIDc.text.toString())
           startActivity(intentTab)

       }


    }
}
