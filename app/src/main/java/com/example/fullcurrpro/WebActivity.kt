package com.example.fullcurrpro

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    lateinit var currList : List<Curr>
    lateinit var myWebView: WebView
    lateinit var lastDate: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        currList = CurrencySingleton.currListObj!!.toList()
        lastDate = CurrencySingleton.currLastUpdate
        Log.e("WebView", currList.toString(), null)

        setPointer()
        showWebData()
    }

    private fun setPointer(){

    }

    private fun showWebData(){

        var htmlDocument = "<html><body>"
        htmlDocument+="<h1><center>${lastDate}</center></h1></hr>"
        for(i in 0 until currList.size){
            htmlDocument+="<h3><center><i>${currList.get(i).COUNTRY}</center></h3></br>"
            htmlDocument+="<ul><li>Currency : ${currList.get(i).CURRENCYCODE}</li><li>Rate : ${currList.get(i).RATE} </li><li>Currency : ${currList.get(i).CHANGE}</li></ul>"
        }

        htmlDocument+="</body></html>"
        printWebView(htmlDocument)

    }

    private fun printWebView(html: String) {
        webView.loadData(html,"text/HTML","UTF-8")
        myWebView=webView
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.print,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.printForm->{
                //create the menu
                createWebPrintJob(myWebView)
            }

        }
        return true
    }

    private fun createWebPrintJob(view: WebView) {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = view.createPrintDocumentAdapter("Currency Report")

        val jobName = "android : currency report"

        printManager.print(jobName,printAdapter,PrintAttributes.Builder().build())
    }
}
