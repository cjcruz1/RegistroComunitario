package com.grupo4.encuesta


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.grupo4.encuesta.R
import com.example.coquistudio.encuestacomunitaria.dummy.DummyContent
import com.example.coquistudio.encuestacomunitaria.dummy.GsonHandler
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import java.io.File
import java.io.InputStream
import android.support.v4.content.ContextCompat.startActivity
import java.io.FileWriter


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [itemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class itemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private lateinit var mPreferences : SharedPreferences

//    override fun onStart() {
//        super.onStart()
//        // Load from memory the encuestas
//        manageSharedPreferences()
//        setupRecyclerView(item_list)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)


        setSupportActionBar(toolbar)
        toolbar.title = title

        // Load from memory the encuestas
        manageSharedPreferences()
        setupRecyclerView(item_list)

        //if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            //twoPane = true
        //}//Cambio de vista

        //fetchJSON()


    }//Final del OnCreate#############################

    fun  writeFile(){
        val text:String = resources.openRawResource(R.raw.dummy).bufferedReader().use { it.readText()}
        println(text)

        try{
            var fo = FileWriter("Datos.txt")
            fo.write(text)
            fo.close()

        }catch (ex:Exception){
            print(ex.message)
        }

    }

    //Json Catcher
    fun fetchJSON(){

        val test = File(this.getFilesDir() , "/" + "working");

        val gson = GsonBuilder().create()

        val text:String = resources.openRawResource(R.raw.dummy).bufferedReader().use { it.readText()}

        val texto = gson.fromJson(text, GsonHandler::class.java)
    }

    /*fun fetchJSON(){

        // val test = File(this.getFilesDir() , "/" + "working");
    /*
        var json: String? = null
        try {
            val inputStream = assets.open("dummy.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, UTF_8)

        } catch (e: IOException) {
            e.printStackTrace()
        }*/


        val file = File("Datos.txt")
        val ins:InputStream = file.inputStream()
        var text = ins.readBytes().toString(Charset.defaultCharset())

        val gson = GsonBuilder().create()


        val texto = gson.fromJson(text, GsonHandler::class.java)


    }*/

    //Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.main_menu, menu)
        return true
    }//Menu


    //Opciones del Menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item ?: return false

        val id = item.getItemId();

        if(id == R.id.CrearE) {
            val intentTab = Intent(this, EncuestaActivity::class.java)
            startActivityForResult(intentTab, 1)
        }

        else if(id == R.id.DeleteE) {
            Toast.makeText(getApplicationContext(), "Borrando", Toast.LENGTH_SHORT).show()
        }

        else if(id == R.id.Tabu) {
            Toast.makeText(getApplicationContext(), "Tabulando", Toast.LENGTH_SHORT).show()
            val  intent = Intent(this, Searchactivity::class.java)
            startActivity(intent)

        }

        return true
    }//Opciones del Menu


    // The following function loads every encuesta stored in the shared preferences into the DummyContent
    // object so that it can then be displayed on the screen
    private fun manageSharedPreferences(){

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val tmp = DummyContent.ITEMS.size

        // Remove every item from the dummy content
        for (i in 0 until DummyContent.ITEMS.size)
            DummyContent.ITEMS.removeAt(tmp - i - 1)

        var index :Int = 0
        var answer :String?
        var key :String
        var dummyItem :DummyContent.DummyItem

        while (true){

            key = "Encuesta" + index.toString()
            answer = mPreferences.getString(key, "false")

            // if true is because there is nothing in memory by the name key or it read every eancuesta
            if (answer == "false")
                break

            // else
            else{
                dummyItem = DummyContent.DummyItem("", key, answer)
                DummyContent.ITEMS.add(dummyItem)
            }
            index++
        }
    }

    // This method will be called after finish() is executed on the EncuestaActivity
    // What this function does is update the screen to contain the new encuesta answered.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                println("OK")
                manageSharedPreferences()
                setupRecyclerView(item_list)
            }
            if (resultCode == Activity.RESULT_CANCELED)
                println("Canceled")
        }

    }//onActivityResult



    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mPreferences)
    }





    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: itemListActivity,
        private val values: List<DummyContent.DummyItem>,
        private val mPreferences: SharedPreferences
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                println(item.id)

                DummyContent.answer = mPreferences.getString(item.id, "")

                val intent = Intent(v.context, itemDetailActivity::class.java).apply {
                        putExtra(itemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)

            }
        }/* OnCLick de cada elemento del Recycler. */

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
