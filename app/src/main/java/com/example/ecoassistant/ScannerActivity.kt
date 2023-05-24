package com.example.ecoassistant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import com.example.ecoassistant.databinding.ActivityScannerBinding
import com.google.firebase.database.*
import androidx.activity.result.ActivityResultLauncher
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dataclass: BarcodeDataClass

    private lateinit var barcodeLauncher: ActivityResultLauncher<ScanOptions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        binding.scanButton.setOnClickListener {
            checkCameraPermission()
        }

        barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
            if (result.contents != null) {
                val scannedCode = result.contents
                if (isBarcodeInDatabase(scannedCode)) {
                    val dataclass = getBarcodeInfo(scannedCode)
                    showBarcodeInfo(dataclass)
                } else {
                    showBarcodeNotFound()
                }
            } else {
                Toast.makeText(this, "Skenēšana atcelta", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            scanBarcode()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanBarcode()
            } else {
                Toast.makeText(this, "Kameras lietošanas atļauja noraidīta", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun scanBarcode() {
        val options = ScanOptions()
        options.setPrompt("Svītrkoda skenēšana")
        barcodeLauncher.launch(options)
    }

    private fun isBarcodeInDatabase(barcode: String): Boolean {
        databaseReference = database.getReference("Barcode")
        val task = databaseReference.child(barcode).get()
        task.addOnSuccessListener { dataSnapshot ->
            clearBarcodeInfo()
            if (dataSnapshot.exists()) {
                val id = dataSnapshot.child("id").value.toString()
                val materials = dataSnapshot.child("materials").value.toString()
                val konteiners = dataSnapshot.child("konteiners").value.toString()
                val depozits = dataSnapshot.child("depozits").value.toString()
                dataclass = BarcodeDataClass(id, materials, konteiners, depozits)
                showBarcodeInfo(dataclass)
            } else {
                showBarcodeNotFound()
            }
        }.addOnFailureListener { exception ->
        }
        return false
    }

    private fun getBarcodeInfo(barcode: String): BarcodeDataClass {
        databaseReference = database.getReference("Barcode")
        val dataSnapshot = databaseReference.get().result
        val id = dataSnapshot.child("id").value.toString()
        val materials = dataSnapshot.child("materials").value.toString()
        val konteiners = dataSnapshot.child("konteiners").value.toString()
        val depozits = dataSnapshot.child("depozits").value.toString()
        return BarcodeDataClass(id, materials, konteiners, depozits)
    }

    private fun showBarcodeInfo(dataclass: BarcodeDataClass) {
        binding.scanInfo.text = "Rezultāts pieejams zemāk!"
        binding.scanInfo.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.barcodeId.text = dataclass.id
        binding.barcodeMaterials.text = dataclass.materials
        val konteinersText = dataclass.konteiners?.split(";")?.joinToString("\n")
        val newkonteinersText = konteinersText?.replace(Regex("^", RegexOption.MULTILINE), "• ")
        binding.barcodeKonteiners.text = newkonteinersText
        binding.barcodeDepozits.text = dataclass.depozits
    }


    private fun showBarcodeNotFound() {
        binding.scanInfo.text = "Svītrkods nav atrasts datu bāzē"
        binding.scanInfo.setTextColor(ContextCompat.getColor(this, R.color.red))
    }

    private fun clearBarcodeInfo() {
        binding.barcodeId.text = ""
        binding.barcodeMaterials.text = ""
        binding.barcodeKonteiners.text = ""
        binding.barcodeDepozits.text = ""
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}