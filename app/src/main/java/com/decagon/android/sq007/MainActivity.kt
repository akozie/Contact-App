package com.decagon.android.sq007

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.data.Contact
import com.decagon.android.sq007.ui.ContactAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

}
