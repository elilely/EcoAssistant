package com.example.ecoassistant


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.ecoassistant.databinding.ActivityMaterialBinding

class MaterialActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listData: List<String>
    private lateinit var binding: ActivityMaterialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image: ImageView = findViewById(R.id.materialImage)

        val intent = intent
        if (intent != null) {
            val imageUrl = intent.getStringExtra("image")
            Glide.with(this)
                .load(imageUrl)
                .into(image)
            binding.material.text = intent.getStringExtra("name")
            binding.textDer.text = intent.getStringExtra("der")
            binding.textNeder.text = intent.getStringExtra("neder")
            binding.textSagatavo.text = intent.getStringExtra("sagatavo")
            binding.textParstrade.text = intent.getStringExtra("parstrade")
        }

        binding.buttonLZP.setOnClickListener {
            val i = Intent(this, ReadRulesActivity::class.java)
            startActivity(i)
        }
        binding.buttonCleanR.setOnClickListener {
            val i2 = Intent(this, ReadRulesCleanRActivity::class.java)
            startActivity(i2)
        }
        binding.buttonZaao.setOnClickListener {
            val i3 = Intent(this, ReadRulesZaaoActivity::class.java)
            startActivity(i3)
        }
    }
}