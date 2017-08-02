package com.example.hurui.news.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.hurui.news.R
import kotlinx.android.synthetic.main.main_toolbar.*

/**
 * Created by hurui on 2017/8/2.
 */
class MapActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        toolbar.title = "地图"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}