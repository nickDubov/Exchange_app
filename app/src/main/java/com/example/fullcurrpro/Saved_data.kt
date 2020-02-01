package com.example.fullcurrpro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault

import kotlinx.android.synthetic.main.row_sp.view.*
import kotlinx.android.synthetic.main.saved_data.*

class SharedPreferences : AppCompatActivity(),View.OnClickListener{

    lateinit var savedExchange:List<Saved_exchange>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_data)

        this.btn_registerNow.setOnClickListener(this)

        if(CurrencySingleton.guest.equals("") || CurrencySingleton.guest.equals(null)){
            btn_registerNow.setVisibility(View.GONE)
            setPointer()
        }
        else{
            Toast.makeText(applicationContext,"Only registered user can see saved data",Toast.LENGTH_SHORT).show()
            btn_registerNow.setVisibility(View.VISIBLE)
        }
    }

    private fun setPointer() {

            Backendless.Data.of(Saved_exchange::class.java).find(object :AsyncCallback<List<Saved_exchange>> {
                override fun handleResponse(response: List<Saved_exchange>) {
                    savedExchange = response
                    val adapter = ListDataAdapter(applicationContext, savedExchange)
                    lstViewSp.setAdapter(adapter)
                    
                }

                override fun handleFault(fault: BackendlessFault) {
                    Toast.makeText(applicationContext, fault.message, Toast.LENGTH_LONG).show()
                }
            })
        }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_registerNow -> {
                intent = Intent(applicationContext,RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //lstViewSp.adapter = ListSharedPreferances(applicationContext, spList!!)
}



private class ListDataAdapter(context: Context,data: List<Saved_exchange>): BaseAdapter() {
    var context:Context
    var data: List<Saved_exchange>

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
        val rowSp = layoutInflater.inflate(R.layout.row_sp,null)
        var unitBdless = data.get(index).unit.toString()
        var resultBdless = data.get(index).result.toString()

        var date = data.get(index).createdDate

        //getting date from backendless
        //var date = data.get(index).created


        rowSp.getUnit.text = "Unit:$unitBdless"
        rowSp.getResult.text = "Result:$resultBdless"
        rowSp.getDate.text = date.toString()
        rowSp.counterRow.text = index.toString()
        
        return rowSp
    }
}

class PrefClass(unit:String,result: String,date: String){
    var unit:String = "None"
    var result:String = "None"
    var date:String? = "None"

    init {
        this.unit = unit
        this.result = result
        this.date = date
    }

}






