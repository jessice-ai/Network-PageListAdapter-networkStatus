package com.example.sun8


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.sun8.data.viewmodel.MyViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * Fragment 页面中返回按钮 第二部分
         * fragment2 是 res\layout\activity_main.xml 中
         */
        NavigationUI.setupActionBarWithNavController(this,findNavController(R.id.sunNavhostfragment))
    }
    //Fragment 页面中返回按钮 第一部分
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || findNavController(R.id.sunNavhostfragment).navigateUp()
    }
    /**
     * Menubar 作用： 显示 menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.sunmenu, menu)
        return true
    }


}