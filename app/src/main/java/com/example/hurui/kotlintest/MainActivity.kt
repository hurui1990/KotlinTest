package com.example.hurui.kotlintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textview!!.text = "Kotlin Test"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = item?.itemId
        if (id == R.id.action_setting){
            toast("Setting", Toast.LENGTH_LONG)
        }else if (id == R.id.action_about){
            toast("About", Toast.LENGTH_SHORT)
        }
        return super.onOptionsItemSelected(item)
    }

    fun toast(message:String, length:Int){
        Toast.makeText(this, message, length).show()
    }
}
