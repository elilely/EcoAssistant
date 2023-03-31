package com.example.ecoassistant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
//import com.example.ecoassistant.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(){

    //private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //NAVIGATION VIEW

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        //navigationView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, GuideFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_guide)
        }

        navigationView.setNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_guide -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, GuideFragment()).commit()
                R.id.nav_map -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MapFragment()).commit()
                R.id.nav_scanner -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ScannerFragment()).commit()
                R.id.nav_sign_out -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(applicationContext, StartActivity::class.java))
                    finish()}
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    //To close the Navigation drawer not the entire App
    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else{
            super.onBackPressed()
        }
    }
}
