package com.example.fullcurrpro

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fullcurrpro.CurrencySingleton.bgColor
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import kotlinx.android.synthetic.main.activity_home.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class HomeActivity : AppCompatActivity() {

    val CONNECTION_TIMEOUT_MILLISECONDS = 60 * 1000
    val WURL = "https://www.boi.org.il/currency.xml"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val pb = findViewById(R.id.progress_Bar) as ProgressBar
        pb.setVisibility(View.VISIBLE)

        var sharedPreferences = getSharedPreferences("bg_color",Context.MODE_PRIVATE)
        var bgColor = sharedPreferences.getInt("bg_color",Color.WHITE)

        CurrencySingleton.bgColor = bgColor
        CurrencySingleton.guest = intent.getStringExtra("guest")
        var userWelcome = intent.getStringExtra("userName")?.toString()
        if(!(userWelcome.equals(null))){
            Toast.makeText(applicationContext, "Welcome ${userWelcome}", Toast.LENGTH_SHORT).show()
        }

        alertDialogAppVersion()

        //setupPermissions()
        setPointer()

    }

    private fun alertDialogAppVersion() {
        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity,R.style.Theme_AppCompat_Dialog)

        val mView = layoutInflater.inflate(R.layout.dialog_app_updates, null)

        val mCheckBox = mView.findViewById<CheckBox>(R.id.checkBox)

        mBuilder.setTitle("Pre-Alpha version 0.1")
        mBuilder.setMessage("correct for this time all the currency exchange are can be changed only by ₪(NIS)")
        mBuilder.setView(mView)

        mBuilder.setPositiveButton("OK") {
                dialogInterface, i -> dialogInterface.dismiss()
        }

        val mDialog = mBuilder.create()
        mDialog.show()

        mCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                storeDialogStatus(true)
            } else {
                storeDialogStatus(false)
            }
        }

        if (getDialogStatus()) {
            mDialog.hide()
        } else {
            mDialog.show()
        }

    }


    private fun getDialogStatus(): Boolean {
        val mSharedPreferences: android.content.SharedPreferences? = getSharedPreferences("CheckItem", MODE_PRIVATE)
        return mSharedPreferences!!.getBoolean("item", false)
    }

    private fun storeDialogStatus(isChecked: Boolean) {
        val mSharedPreferences: android.content.SharedPreferences? = getSharedPreferences("CheckItem", MODE_PRIVATE)
        val mEditor: android.content.SharedPreferences.Editor? = mSharedPreferences?.edit()
        mEditor!!.putBoolean("item", isChecked)
        mEditor!!.apply()
    }


    override fun onStart() {
        super.onStart()
        setBGColor()

    }

    private fun setBGColor() {
        val layoutInflater = LayoutInflater.from(applicationContext)
        var bgContainer = bg_container

        if(CurrencySingleton.bgColor!!.equals(null) || CurrencySingleton.bgColor!!.equals(0) ){
            bg_container.setBackgroundColor(Color.WHITE)
        }
        else{
            bg_container.setBackgroundColor(CurrencySingleton.bgColor!!)

        }
        //setting the right text color
        if(CurrencySingleton.bgColor!!.equals(Color.BLACK)){
            val layoutInflater = LayoutInflater.from(applicationContext)
            var bgContainer = bg_container
            bg_container.setBackgroundColor(bgColor!!)
            val rowMain = layoutInflater.inflate(R.layout.row_main, bgContainer, false)
            val rate = rowMain.findViewById<TextView>(R.id.curRate)
            val curCode = rowMain.findViewById<TextView>(R.id.curCode)
            val curName = rowMain.findViewById<TextView>(R.id.curName)
            val curCountry = rowMain.findViewById<TextView>(R.id.country)
            val curChange = rowMain.findViewById<TextView>(R.id.change)
            curCode.setTextColor(Color.WHITE)
            rate.setTextColor(Color.WHITE)
            curName.setTextColor(Color.WHITE)
            curCountry.setTextColor(Color.WHITE)
            curChange.setTextColor(Color.WHITE)
        }
        else{
            val layoutInflater = LayoutInflater.from(applicationContext)
            val rowMain = layoutInflater.inflate(R.layout.row_main, bgContainer, false)
            val rate = rowMain.findViewById<TextView>(R.id.curRate)
            val curCode = rowMain.findViewById<TextView>(R.id.curCode)
            val curName = rowMain.findViewById<TextView>(R.id.curName)
            val curCountry = rowMain.findViewById<TextView>(R.id.country)
            val curChange = rowMain.findViewById<TextView>(R.id.change)
            curCode.setTextColor(Color.BLACK)
            rate.setTextColor(Color.BLACK)
            curName.setTextColor(Color.BLACK)
            curCountry.setTextColor(Color.BLACK)
            curChange.setTextColor(Color.BLACK)

        }
    }


    private fun setPointer() {
        var myData: GetCurrencyAsyncTask = GetCurrencyAsyncTask()
        myData.execute(WURL)

        var icon = ImageView(this)
        icon.setImageResource(R.drawable.settingsicon)

        var actionButton = FloatingActionButton.Builder(this).setContentView(icon)

        var itemIcon1 = ImageView(applicationContext)
        itemIcon1.setImageResource(R.drawable.printer)

        var itemIcon2 = ImageView(applicationContext)
        itemIcon2.setImageResource(R.drawable.icon_save)

        var itemIcon3 = ImageView(applicationContext)
        itemIcon3.setImageResource(R.drawable.bg_activity)

        var itemBuilder1 = SubActionButton.Builder(this)
        var itemBuilder2 = SubActionButton.Builder(this)
        var itemBuilder3 = SubActionButton.Builder(this)

        var button1: SubActionButton = itemBuilder1.setContentView(itemIcon1).build()
        var button2: SubActionButton = itemBuilder2.setContentView(itemIcon2).build()
        var button3: SubActionButton = itemBuilder3.setContentView(itemIcon3).build()

        val actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(button1)
            .addSubActionView(button2)
            .addSubActionView(button3)
            .setRadius(200)
            .attachTo(actionButton.build())
            .build()

        //print activity
        button1.setOnClickListener {
            intent = Intent(applicationContext,WebActivity::class.java)
            startActivity(intent)

        }
        //saved data activity
        button2.setOnClickListener {
            intent = Intent(applicationContext,SharedPreferences::class.java)
            startActivity(intent)

        }
        //background changer activity
        button3.setOnClickListener {
            intent = Intent(applicationContext,BGchanger::class.java)
            startActivity(intent)

        }

    }

    inner class GetCurrencyAsyncTask : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg args: String?): String {
            lateinit var urlConnection: HttpURLConnection
            var inString = ""
            try {
                var url = URL(args[0])

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = CONNECTION_TIMEOUT_MILLISECONDS
                urlConnection.readTimeout = CONNECTION_TIMEOUT_MILLISECONDS

                //a function to handle our stream
                inString = streamToString(urlConnection.inputStream)

            } catch (e: Exception) {
                //todo handle exception
            } finally {
                urlConnection.disconnect()
            }
            return inString
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            //e("zeev",result)

            val currencies = XMLParser(MY_CURRENCY::class.java).fromXML(result)

            val lastUp = currencies.LAST_UPDATE!!.toString()
            currencies.CURRENCY!!.forEach { item ->
                Log.e("zeev", item.NAME)

            }
            //we have the entire currency collection in a list

            CurrencySingleton.currLastUpdate = lastUp//currencies.LAST_UPDATE.toString()
            CurrencySingleton.currListObj = currencies.CURRENCY!!.toList()
            //communicator.passDataCom(currencies.CURRENCY!!.toList(),currencies.LAST_UPDATE.toString())
            val pb = findViewById(R.id.progress_Bar) as ProgressBar
            listView.findViewById<ListView>(R.id.listView)
            pb.setVisibility(View.GONE)
            listView.adapter = ListAdapter(applicationContext, currencies.CURRENCY!!.toList())
            //setFabbAction(lastUp,currencyObj as ArrayList<Curr>)


        }
    }
}


fun streamToString(inputStream: InputStream): String {
    val bufferReader = BufferedReader(InputStreamReader(inputStream))
    lateinit var line: String
    var result = StringBuilder()
    try {
        do {
            line = bufferReader.readLine()
            if (line != null) {
                result.append(line)
            }
        } while (line != null)
    } catch (e: Exception) {
        //todo handle exception for line reader
    } finally {
        inputStream.close()
    }
    e("txt", line)
    return result.toString()
}

private class ListAdapter(context: Context, data: List<Curr>) : BaseAdapter(), View.OnClickListener {
    var context: Context
    var data: List<Curr>

    init {
        this.context = context
        this.data = data

    }


    override fun getItem(index: Int): Any {
        return data[index]
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {

        val layoutInflater = LayoutInflater.from(context)
        val rowMain = layoutInflater.inflate(R.layout.row_main, viewGroup, false)
        val rate = rowMain.findViewById<TextView>(R.id.curRate)
        val icon = rowMain.findViewById<ImageView>(R.id.curIcon)
        val curCode = rowMain.findViewById<TextView>(R.id.curCode)
        val curName = rowMain.findViewById<TextView>(R.id.curName)
        val curCountry = rowMain.findViewById<TextView>(R.id.country)
        val curChange = rowMain.findViewById<TextView>(R.id.change)
        val curSort = rowMain.findViewById<ImageView>(R.id.sort)

        curCode.text = data.get(index).CURRENCYCODE.toString()
        rate.text = data.get(index).RATE.toString()
        curName.text = data.get(index).NAME.toString()
        curCountry.text = data.get(index).COUNTRY.toString()
        curChange.text = data.get(index).CHANGE.toString()

        if(spSaves.backGroundColor == Color.BLACK){
            curCode.setTextColor(Color.WHITE)
            rate.setTextColor(Color.WHITE)
            curName.setTextColor(Color.WHITE)
            curCountry.setTextColor(Color.WHITE)
            curChange.setTextColor(Color.WHITE)
        }
        else{
            curCode.setTextColor(Color.BLACK)
            rate.setTextColor(Color.BLACK)
            curName.setTextColor(Color.BLACK)
            curCountry.setTextColor(Color.BLACK)
            curChange.setTextColor(Color.BLACK)

        }

        when (data.get(index).CURRENCYCODE) {
            "USD" -> {
                icon.setImageResource(R.drawable.icons8_usa_50)
            }
            "GBP" -> {
                icon.setImageResource(R.drawable.icons8_great_britain_50)
            }
            "JPY" -> {
                icon.setImageResource(R.drawable.icons8_japan_50)
            }
            "EUR" -> {
                icon.setImageResource(R.drawable.icons8_flag_of_europe_50)
            }
            "AUD" -> {
                icon.setImageResource(R.drawable.icons8_australia_50)
            }
            "CAD" -> {
                icon.setImageResource(R.drawable.icons8_canada_48)
            }
            "DKK" -> {
                icon.setImageResource(R.drawable.icons8_denmark_50)
            }
            "NOK" -> {
                icon.setImageResource(R.drawable.icons8_norway_50)
            }
            "ZAR" -> {
                icon.setImageResource(R.drawable.icons8_south_africa_50)
            }
            "SEK" -> {
                icon.setImageResource(R.drawable.icons8_sweden_50)
            }
            "CHF" -> {
                icon.setImageResource(R.drawable.icons8_switzerland_50)
            }
            "JOD" -> {
                icon.setImageResource(R.drawable.icons8_jordan_50)
            }
            "LBP" -> {
                icon.setImageResource(R.drawable.icons8_lebanon_50)
            }
            "EGP" -> {
                icon.setImageResource(R.drawable.icons8_egypt_50)
            }
        }

        for (i in 0 until data.size) {
            var mockData = data.get(index).CHANGE.toString()
            if (mockData.contains("-")) {

                curSort.setImageResource(R.drawable.icons8_sort_down_24)

            } else {
                curSort.setImageResource(R.drawable.icons8_sort_up_24)
            }
        }

        rowMain.setOnClickListener  { view: View ->
            var intent = Intent(view.context,ExchangeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("CURRENCYCODE",data.get(index).CURRENCYCODE)
            intent.putExtra("CHANGE",data.get(index).CHANGE)
            intent.putExtra("COUNTRY",data.get(index).COUNTRY)
            intent.putExtra("NAME",data.get(index).NAME)
            intent.putExtra("RATE",data.get(index).RATE).toString()
            intent.putExtra("UNIT",data.get(index).UNIT)

            e("intentCheck",intent.putExtra("RATE",data.get(index).RATE).toString(),null)
            context.startActivity(intent)

        }

        /*
        myRow.setTextSize(32F)
        rowMain.setText(data[index])
        rowMain.setTextColor(Color.BLACK)
        */

        return rowMain
    }

    override fun onClick(view: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class Curr {
    @JvmField
    var NAME: String? = null
    @JvmField
    var UNIT: Int = 0
    @JvmField
    var CURRENCYCODE: String? = null
    @JvmField
    var COUNTRY: String? = null
    @JvmField
    var RATE: Double = 0.0
    @JvmField
    var CHANGE: Double = 0.0

}

class MY_CURRENCY {
    @JvmField
    var CURRENCY: Array<Curr>? = null
    @JvmField
    var LAST_UPDATE: String? = null

}

