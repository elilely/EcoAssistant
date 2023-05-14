package com.example.ecoassistant

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ecoassistant.databinding.ActivitySignInBinding
import com.example.ecoassistant.databinding.FragmentScannerBinding


class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // param1 = it.getString(ARG_PARAM1)
            // param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)

        val openScannerButton = view.findViewById<Button>(R.id.openScannerBtn)
        openScannerButton.setOnClickListener {
            val intent = Intent(activity, ScannerActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}

/*private lateinit var database: DatabaseReference
//private lateinit var storage: FirebaseStorage
private lateinit var storage: StorageReference
private lateinit var scanner: BarcodeScanner
private lateinit var barcodeDetector: BarcodeDetector


override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    return inflater.inflate(R.layout.fragment_scanner, container, false)
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    database = Firebase.database.getReferenceFromUrl("https://ecoassistant-em-default-rtdb.europe-west1.firebasedatabase.app/")
    storage = Firebase.storage.getReferenceFromUrl("gs://ecoassistant-em.appspot.com")

    scanner = BarcodeScanning.getClient()

    binding = FragmentScannerBinding.inflate(layoutInflater)

    barcodeDetector = BarcodeDetector.Builder(requireContext())
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    if (!barcodeDetector.isOperational) {
        Toast.makeText(requireContext(), "Barcode detector not operational", Toast.LENGTH_SHORT).show()
        return
    }

    val takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Обработка успешного результата
            val data = result.data
            // Вы можете получить изображение здесь с помощью data.extras.get("data") или каким-то другим способом
        } else {
            // Обработка неудачного результата
        }
    }
    // Запускаем камеру для снятия изображения
    binding.scanButton.setOnClickListener {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            takePicture.launch(takePictureIntent)
        }
    }
}


@SuppressLint("SetTextI18n")
private fun scanBarcode(bitmap: Bitmap) {
    val image = InputImage.fromBitmap(bitmap, 0)

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            if (barcodes.isNotEmpty()) {
                val barcode = barcodes[0].rawValue
                checkBarcodeInDatabase(barcode?.toString() ?: "") { exists ->
                    if (exists) {
                        binding.scanResult.text = "Barcode found: $barcode"
                    } else {
                        binding.scanResult.text = "Barcode not found"
                    }
                    binding.progressBar.visibility = View.GONE
                }
            } else {
                binding.scanResult.text = "No barcode found"
                binding.progressBar.visibility = View.GONE
            }
        }
        .addOnFailureListener { exception ->
            Log.e(TAG, "Error scanning barcode", exception)
            binding.scanResult.text = "Error scanning barcode"
            binding.progressBar.visibility = View.GONE
        }
    val detector = BarcodeDetector.Builder(requireContext())
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()
    if (!detector.isOperational) {
        // Detector is not operational
        Toast.makeText(requireContext(), "Detector is not operational", Toast.LENGTH_SHORT).show()
        return
    }
    val frame = Frame.Builder().setBitmap(bitmap).build()
    val barcodes = detector.detect(frame)
    if (barcodes.size() > 0) {
        val barcode = barcodes.valueAt(0)
        val barcodeValue = barcode.rawValue
        checkBarcodeInDatabase(barcodeValue) { exists ->
            if (exists) {
                // Barcode exists in database
                Toast.makeText(requireContext(), "Barcode found in database", Toast.LENGTH_SHORT).show()
            } else {
                // Barcode does not exist in database
                Toast.makeText(requireContext(), "Barcode not found in database", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        // No barcode detected
    }
}

private fun checkBarcodeInDatabase(barcode: String, callback: (Boolean) -> Unit) {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.getReference("barcodes")
    val query: Query = ref.orderByChild("code").equalTo(barcode)
    /*query.get()
        .addOnSuccessListener { data ->
            callback(data.exists())
        }
        .addOnFailureListener { exception ->
            Log.e(TAG, "Error checking barcode in database", exception)
            callback(false)
        }*/
    ref.orderByChild("code").equalTo(barcode)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Barcode found in database
                    callback(true)
                } else {
                    // Barcode not found in database
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error reading from database", error.toException())
                callback(false)
            }
        })

    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val exists = dataSnapshot.exists()
            callback(exists)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            callback(false)
        }
    })
}

private fun uploadBitmap(
    ref: StorageReference,
    bitmap: Bitmap,
    callback: (String?) -> Unit
) {
    val baos = ByteArrayOutputStream()
    bitmap.compress(
        Bitmap.CompressFormat.JPEG, 100, baos)

    val data = baos.toByteArray()

    ref.putBytes(data)
        .addOnSuccessListener {
            ref.downloadUrl
                .addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting download URL", exception)
                    callback(null)
                }
        }
        .addOnFailureListener { exception ->
            Log.e(TAG, "Error uploading bitmap", exception)
            callback(null)
        }
}
companion object {
    private const val TAG = "ScannerFragment"
    private const val REQUEST_IMAGE_CAPTURE = 1
}

@Deprecated("Deprecated in Java")
override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
    val takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.getString("data") as Bitmap

                val frame = Frame.Builder().setBitmap(imageBitmap).build()
                val barcodes = barcodeDetector.detect(frame)

                if (barcodes.size() > 0) {
                    val barcode = barcodes.valueAt(0).displayValue

                    checkBarcodeInDatabase(barcode) { exists ->
                        if (exists) {
                            Toast.makeText(
                                requireContext(),
                                "Barcode exists in database",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Barcode not found in database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "No barcodes detected", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Error scanning barcode", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    binding.scanButton.setOnClickListener {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            takePicture.launch(takePictureIntent)
        }
    }
}
}*/


// private lateinit var cameraButton: MaterialButton
// private lateinit var galleryButton: MaterialButton
// private lateinit var imageScanner: ImageView
// private lateinit var scanButton: MaterialButton
// private lateinit var scanResult: TextView
//
// private lateinit var cameraPermissions: Array<String>
// private lateinit var storagePermissions: Array<String>
//
// private var imageUri: Uri? = null
//
// override fun onCreate(savedInstanceState: Bundle?) {
// super.onCreate(savedInstanceState)
//
// }
//
// override fun onCreateView(
// inflater: LayoutInflater, container: ViewGroup?,
// savedInstanceState: Bundle?
// ): View? {
// // Inflate the layout for this fragment
// return inflater.inflate(R.layout.fragment_scanner, container, false)
// }
//
// companion object {
//
// private const val CAMERA_REQUEST_CODE = 100
// private const val STORAGE_REQUEST_CODE = 101
//
// private const val TAG = "MAIN_TAG"
//
// // TODO: Rename and change types and number of parameters
// @JvmStatic
// fun newInstance(param1: String, param2: String) =
// ScannerFragment().apply {
//
// }
// }
//
// override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
// super.onViewCreated(view, savedInstanceState)
//
// cameraButton = view.findViewById(R.id.cameraButton)
// galleryButton = view.findViewById(R.id.galleryButton)
// imageScanner = view.findViewById(R.id.imageScanner)
// scanButton = view.findViewById(R.id.scanButton)
// scanResult = view.findViewById(R.id.scanResult)
//
// cameraPermissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
// storagePermissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
// cameraButton.setOnClickListener {
//
// }
// galleryButton.setOnClickListener {
//
// }
// scanButton.setOnClickListener {
//
// }
// }
//
// private fun pickImageGallery(){
// val intent = Intent(Intent.ACTION_PICK)
//
// intent.type = "image/*"
// galleryActivityResultLauncher.launch(intent)
// }
//
// private val galleryActivityResultLauncher = registerForActivityResult(
// ActivityResultContracts.StartActivityForResult()
// ){result ->
// if (result.resultCode == Activity.RESULT_OK){
// val data = result.data
// imageUri = data.data
//
// Log.d(TAG, ": imageUri: $imageUri")
// imageScanner.setImageURI(imageUri)
// }else {
// showToast("Anulēts!")
// }
// }
//
// private fun pickImageCamera() {
// val contentValues = ContentValues()
// contentValues.put(MediaStore.Images.Media.TITLE, "Sample Image")
// contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description")
//
// imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
// val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
// }
//
// private fun showToast(message:String){
// Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
// }
// */