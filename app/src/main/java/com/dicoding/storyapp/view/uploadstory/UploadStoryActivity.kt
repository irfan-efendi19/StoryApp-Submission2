package com.dicoding.storyapp.view.uploadstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.response.UploadResponse
import com.dicoding.storyapp.databinding.ActivityStoryBinding
import com.dicoding.storyapp.util.getImageUri
import com.dicoding.storyapp.util.reduceFileImage
import com.dicoding.storyapp.util.uriToFile
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UploadStoryActivity : AppCompatActivity() {

    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val _viewModel by viewModels<UploadStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupAction()
    }

    private fun setupAction() {
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPhotoPreview.setImageURI(it)
        }
    }


    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val description = binding.edAddDescription.text.toString()
            if (description.isNotEmpty()) {
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                val requestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
                try {
                    _viewModel.uploadStory(multipartBody, requestBody)
                    intentMainActivity()
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                    showToast(errorResponse.message.toString())
                }
            } else {
                showToast(getString(R.string.invalid_file))
            }
        }

    }

//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
//                    getMyLastLocation()
//                }
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
//                    getMyLastLocation()
//                }
//                permissions[Manifest.permission.CAMERA] ?: false -> {}
//                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false -> {}
//                else -> {
//                    Toast.makeText(this@UploadStoryActivity, R.string.permission, Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        }

//    private fun getMyLastLocation() {
//        if (allPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
//            allPermissionsGranted(Manifest.permission.ACCESS_COARSE_LOCATION) &&
//            allPermissionsGranted(Manifest.permission.CAMERA) &&
//            allPermissionsGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
//        ) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    latitude = location.latitude
//                    longitude = location.longitude
//                } else {
//                    Toast.makeText(
//                        this@UploadStoryActivity,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        } else {
//            requestPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.CAMERA
//                )
//            )
//        }
//    }


    private fun intentMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}