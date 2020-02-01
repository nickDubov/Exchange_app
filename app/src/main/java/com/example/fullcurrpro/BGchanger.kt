package com.example.fullcurrpro

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList


class BGchanger : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bgchanger)

        setPointer()
    }

    private fun setPointer() {
        val sharedPreference =  getSharedPreferences("bg_color",Context.MODE_PRIVATE)
        val gridView: GridView = findViewById(R.id.myGrid)
        val mImageIds: ArrayList<Int> = ArrayList(Arrays.asList(android.graphics.Color.WHITE,android.graphics.Color.BLACK,android.graphics.Color.CYAN
        ,android.graphics.Color.RED,android.graphics.Color.YELLOW,Color.DKGRAY,Color.MAGENTA))

        gridView.setAdapter(ImageAdapter(mImageIds,applicationContext,sharedPreference))

    }


private class ImageAdapter(mThumbIds:List<Int>, context: Context, sharedPreferences: SharedPreferences) :BaseAdapter() {
    var context = context
    var mThumbIds = mThumbIds
    var sharedPreferences = sharedPreferences

    override fun getItem(index: Int): Any {
        return mThumbIds[index]
    }

    override fun getItemId(index: Int): Long {
        return mThumbIds.get(index).toLong()
    }

    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
        var imageView: ImageView? = convertView as ImageView?

        if (imageView == null) {
            imageView = ImageView(context)
            imageView.setLayoutParams(AbsListView.LayoutParams(300, 450))
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)

        }

        imageView.setColorFilter(mThumbIds[index])
        imageView.setBackgroundColor(mThumbIds[index])

        imageView.setOnClickListener {

            var selectedColor:Int = mThumbIds[index]
            Log.e("BGchanger", selectedColor.toString(), null)

            val editor = sharedPreferences.edit()
            editor.putInt("bg_color",selectedColor)
            editor.commit()
            CurrencySingleton.bgColor = selectedColor


            Log.e("BGchanger", "after selectedColor", null)

            imageView.setBackgroundColor(mThumbIds[index])

            Toast.makeText(context,"The BackGround Has Been Changed",Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }


        return imageView
        }
    }
}







