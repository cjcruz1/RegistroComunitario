package com.grupo4.encuesta

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_tabulacion.*
import com.grupo4.encuesta.R
import kotlinx.android.synthetic.main.activity_item_list.*
import java.io.File

class tabulacion : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabulacion)


        // Zipcode number
        // Otra forma de buscar valores enviados
        // val ss:String = intent.getStringExtra("valor")
        val int = intent.extras.getString("zipcode")
        val com = intent.extras.getString("comunidad")

        //Desplego de Campos entrados
        tabSearch.setText(" Datos entrados: " + "Zipcode " + int + "\n Comunidad :" + com)


        //Dummy De como se deberia ver la Tabulacion
        var test = "1. Localidad:@a: Comunidad@Certenejas@b: Municipio@Cidra@c: Zip-Code@00739#2. Población de 0 a 5 años:@a: No. Niñas@400@b: No. Niños@200#3. Población de 6 a 18 años:@a: No. Niñas@200@b: No. Niños@400#4. Población de 19 a 60 años:@a: No. Mujeres@1@b: No. Hombres@2@5. Población de 61 años o más:@a: No. Mujeres@2@b: No. Hombres@3#6. Cantidad de viviendas:@a: Total@1111#7. Espacio para comentarios sobre populación:@a: Comentarios@Hello World#8. Discapacidad Física (Ej: Dificultad para caminar):@a: No. Niñas@1@b: No. Niños@2@c: No. Mujeres@3@d: No. Hombres@4@e: No. Ancianas@8@f: No. Ancianos@9#9. Discapacidad Sensorial (Ej: Discapacidad visual):@a: No. Niñas@1@b: No. Niños@1@c: No. Mujeres@1@d: No. Hombres@1@e: No. Ancianas@1@f: No. Ancianos@2#10. Discapacidad Intelectual (Ej: Perdida de la capacidad de analizar):@a: No. Niñas@1@b: No. Niños@2@c: No. Mujeres@3@d: No. Hombres@1@e: No. Ancianas@23@f: No. Ancianos@1#11. Heridos:@a: No. Niñas@1@b: No. Niños@2@c: No. Mujeres@33@d: No. Hombres@3@e: No. Ancianas@2@f: No. Ancianos@1#12. Muertos:@a: No. Niñas@1@b: No. Niños@1@c: No. Mujeres@1@d: No. Hombres@1@e: No. Ancianas@1@f: No. Ancianos@1#13. Enfermedades Crónicas:@a: Escriba Enfermedades Crónicas@Diabetes#14. Explicar situación de vurnerabilidad extrema (si aplica):@a: Comentarios:@Hola#15. Tipo de albergue:@a: Oficial#16. ¿Recibió ayuda por parte de alguna entidad?@a: Sí#17. Necesidades Básicas:@a: Agua@2@b: Comida@3@c: Materiales para construir@no@d: Equipo para higiene@si#18. Mascotas:#a: No. Perros@1@b: No. Gatos@2@c: No. Caballos@3@d: No. Gallinas@4#19. Teléfono:@a: Teléfono@0123456789@"


        val derp = spliter(test)
        val s = derp.size

        for (i in 0..s-1)
        {

            tabResult.append("\n")
            val worker = derp[i]
            for (j in 0..worker.length - 1) {


                if (worker.get(j).toString() == "@")
                {
                    tabResult.append(" ")
                    if(worker.get((j+1)%worker.length).toString() in "abcdefghijk" )
                        tabResult.append("\n" + "\t\t\t\t")
                }
                else
                    tabResult.append(worker.get(j).toString())
            }//nested
        }//big



    }//On create


    fun spliter( args: String): List<String> {

        val reg = Regex("#")

        var list = args.split(reg)

        return list
    }
}// Activity
