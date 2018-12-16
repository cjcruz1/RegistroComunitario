package com.example.coquistudio.encuestacomunitaria.dummy

import com.grupo4.encuesta.Questions
import java.util.*


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 *
 */
object DummyContent {

    var answer :String? = ""
    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<DummyItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, DummyItem> = mutableMapOf()

    val q : Questions = Questions()

    val random=  Random()
    fun rand(from: Int, to: Int) : Int{
        return random.nextInt(to - from) + from
    }

    public val COUNT = 4

    init {
//        // Add some sample items.
//        for (i in 0..COUNT) {
//            addItem(createDummyItem(i))
//        }
    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): DummyItem {
        return DummyItem("", "Encuesta " + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        val derp = spliter(answer!!)
        val s = derp.size

        for (i in 0..s-1)
        {
            builder.append("\n")
            val worker = derp[i]
            for (j in 0..worker.length - 1) {


                if (worker.get(j).toString() == "@")
                {
                    builder.append(" ")
                    if(worker.get((j+1)%worker.length).toString() in "abcdefghijk" )
                        builder.append("\n" + "\t\t\t\t")
                }
                else
                    builder.append(worker.get(j).toString())
            }//nested
        }//big

        return builder.toString()
    }

    fun spliter( args: String): List<String> {

        val reg = Regex("#")

        var list = args.split(reg)

        return list
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val id: String,
                         val content: String,
                         val details: String) {
        override fun toString(): String = content
    }
}
