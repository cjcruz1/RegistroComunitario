package com.grupo4.encuesta

class Questions {

    private val questions = arrayOf(    "1. Localidad:",
                                        "2. Población de 0 a 5 años:",
                                        "3. Población de 6 a 18 años:",
                                        "4. Población de 19 a 60 años:",
                                        "5. Población de 61 años o más:",
                                        "6. Cantidad de viviendas:",
                                        "7. Espacio para comentarios sobre populación:",
                                        "8. Discapacidad Física (Ej: Dificultad para caminar):",
                                        "9. Discapacidad Sensorial (Ej: Discapacidad visual):",
                                        "10. Discapacidad Intelectual (Ej: Perdida de la capacidad de analizar):",
                                        "11. Heridos:",
                                        "12. Muertos:",
                                        "13. Enfermedades Crónicas:",
                                        "14. Explicar situación de vurnerabilidad extrema (si aplica):",
                                        "15. Tipo de albergue:",
                                        "16. ¿Recibió ayuda por parte de alguna entidad?",
                                        "17. Necesidades Básicas:",
                                        "18. Mascotas:",
                                        "19. Teléfono:",
                                        "20. ¿Existe algún plan a seguir por parte de la comunidad?",
                                        "")



    public val options = arrayOf(
            /*1*/                     arrayOf("a: Comunidad", "b: Municipio", "c: Zip-Code"),
            /*2*/                     arrayOf("a: No. Niñas", "b: No. Niños"),
            /*3*/                     arrayOf("a: No. Niñas", "b: No. Niños"),
            /*4*/                     arrayOf("a: No. Mujeres", "b: No. Hombres"),
            /*5*/                     arrayOf("a: No. Mujeres", "b: No. Hombres"),
            /*6*/                     arrayOf("a: Total"),//
            /*7*/                     arrayOf("a: Comentarios"),//
            /*8*/                     arrayOf("a: No. Niñas", "b: No. Niños", "c: No. Mujeres", "d: No. Hombres", "e: No. Ancianas", "f: No. Ancianos"),
            /*9*/                     arrayOf("a: No. Niñas", "b: No. Niños", "c: No. Mujeres", "d: No. Hombres", "e: No. Ancianas", "f: No. Ancianos"),
            /*10*/                    arrayOf("a: No. Niñas", "b: No. Niños", "c: No. Mujeres", "d: No. Hombres", "e: No. Ancianas", "f: No. Ancianos"),
            /*11*/                    arrayOf("a: No. Niñas", "b: No. Niños", "c: No. Mujeres", "d: No. Hombres", "e: No. Ancianas", "f: No. Ancianos"),
            /*12*/                    arrayOf("a: No. Niñas", "b: No. Niños", "c: No. Mujeres", "d: No. Hombres", "e: No. Ancianas", "f: No. Ancianos"),
            /*13*/                    arrayOf("a: Escriba Enfermedades Crónicas"),
            /*14*/                    arrayOf("a: Comentarios:"),
            /*15*/                    arrayOf("a: Oficial", "b: Improvisado", "c: En casa de un familiar"),
            /*16*/                    arrayOf("a: Sí", "b: No"),
            /*17*/                    arrayOf("a: Agua", "b: Comida", "c: Materiales para construir", "d: Equipo para higiene"),
            /*18*/                    arrayOf("a: No. Perros", "b: No. Gatos", "c: No. Caballos", "d: No. Gallinas"),
            /*19*/                    arrayOf("a: Teléfono"),
            /*20*/                    arrayOf("a: Sí", "b: No", "c: No sé"),
            /*21*/                    arrayOf(""))

    // true means the user has to type the answer, false means multiple choice
    private val questionType = arrayOf(
            /*1*/ true,
            /*2*/ true,
            /*3*/ true,
            /*4*/ true,
            /*5*/ true,
            /*6*/ true,
            /*7*/ true,
            /*8*/ true,
            /*9*/ true,
            /*10*/ true,
            /*11*/ true,
            /*12*/ true,
            /*13*/ true,
            /*14*/ true,
            /*15*/ false,
            /*16*/ false,
            /*17*/ true,
            /*18*/ true,
            /*19*/ true,
            /*20*/ false,
            /*21*/ true)

    fun numberOfQuestions() :Int
    {
        return questions.size
    }

    fun numberOfOptions(x :Int) :Int
    {
        return options[x].size
    }

    fun isMultipleChoice(x :Int) :Boolean
    {
        return !questionType[x] //
    }

    fun getQuestion(x: Int): String
    {
        return questions[x]
    }
}