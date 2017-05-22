package com.example.hurui.kotlintest

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*//kotlin-android-extensions插件的作用，可以直接引用该布局中的id

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.first_page)

        fab.setOnClickListener { view -> Snackbar.make(view, "Snackbar", Snackbar.LENGTH_SHORT).show() }

    }

    //创建菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //每个菜单选项的操作
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
