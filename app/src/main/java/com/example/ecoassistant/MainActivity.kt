package com.example.ecoassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecoassistant.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
        /*supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Eco Asistents"*/
    }

}