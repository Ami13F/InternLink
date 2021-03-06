package com.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.utils.TAG


class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var bottomNav : BottomNavigationView
    private val secondActivityRequestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")

        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Hide the status bar.
        actionBar?.hide()
        AppPreferences.init(this)

        bottomNav = findViewById(R.id.bottom_navigation)

        val navController = findNavController(R.id.nav_host_fragment)
        bottomNav.setupWithNavController(navController = navController)

        bottomNav.setOnNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d(javaClass.name,"I'm back")
        val score = intent.getIntExtra("Score", 0)
        Log.d(javaClass.name, "Score from resume $score")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val score = intent.getIntExtra("Score", 0)
            Log.d(javaClass.name, "Score from activity $score")
        }
        bottomNav.menu.findItem(R.id.profile_fragment).isChecked = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(localClassName,"Item Selected...")
        when (item.itemId) {
            R.id.internship_fragment -> {
                Log.d(TAG, "Internships")
                findNavController(R.id.nav_host_fragment).navigate(R.id.internship_fragment)
            }
            R.id.student_profile_fragment -> {
                Log.d(TAG, "Student Profile")
                findNavController(R.id.nav_host_fragment).navigate(R.id.profile_fragment)
            }
            R.id.company_profile_fragment -> {
                Log.d(TAG, "Company profile")
                findNavController(R.id.nav_host_fragment).navigate(R.id.profile_fragment)
            }
            R.id.profile_fragment -> {
                Log.d(TAG, "Profile")
                findNavController(R.id.nav_host_fragment).navigate(R.id.profile_fragment)
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}
