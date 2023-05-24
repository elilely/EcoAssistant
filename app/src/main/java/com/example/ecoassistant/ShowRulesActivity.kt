package com.example.ecoassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecoassistant.databinding.ActivityShowRulesBinding

class ShowRulesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowRulesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowRulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent != null) {
            binding.tvName.text = intent.getStringExtra("name")
            binding.tvDrikst.text = intent.getStringExtra("drikst")
            binding.tvNedrikst.text = intent.getStringExtra("nedrikst")
            binding.tvKonteiners.text = intent.getStringExtra("konteiners")
            binding.tvNoderidiMaksa.text = intent.getStringExtra("noderigi")
            val bundle = intent.extras
            binding.tvNoderidiMaksaTitle.text = bundle?.getString("maksaTitle")

        }
    }
}