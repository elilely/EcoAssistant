package com.example.ecoassistant

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoassistant.databinding.DatabaseBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Database : AppCompatActivity() {

    private lateinit var binding: DatabaseBinding

    private lateinit var database: DatabaseReference

    var imageURL: String? = null
    var uri: Uri? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        binding.buttonSave.setOnClickListener {

            val storageReference = FirebaseStorage.getInstance().reference.child("Images")
                .child(uri!!.lastPathSegment!!)

            storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageURL = urlImage.toString()
            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

            val name = binding.edName.text.toString()
            val der = binding.edDrikst.text.toString()
            val neder = binding.ednedrikst.text.toString()
            val sagatavo = binding.edMaksa.text.toString()
            val parstrade = binding.edKonteiners.text.toString()

            val dataClass = DataClass(name, der, neder, sagatavo, parstrade, imageURL)

            database =
                FirebaseDatabase.getInstance()
                    .getReference("Materials")

            database.child(name).setValue(dataClass).addOnCompleteListener {

                binding.edName.text?.clear()
                binding.edDrikst.text?.clear()
                binding.ednedrikst.text?.clear()
                binding.edMaksa.text?.clear()
                binding.edKonteiners.text?.clear()

                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

}