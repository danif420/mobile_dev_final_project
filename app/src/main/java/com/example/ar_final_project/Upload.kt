package com.example.ar_final_project

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException


class Upload : Fragment() {
    private val PICK_IMAGE_REQUEST = 1
    private val PICK_MODEL_REQUEST = 2
    private var imgb: AppCompatButton? = null
    private var modb: AppCompatButton? = null
    private var img: Uri? = null
    private var model: Uri? = null
    val STORAGE_PERMISSION_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ActivityCompat.requestPermissions(
            requireContext() as Activity,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
        val upload = view.findViewById<AppCompatButton>(R.id.uploadb)
        imgb = view?.findViewById(R.id.imageb)
        modb = view?.findViewById(R.id.modelb)
        val name = view.findViewById<EditText>(R.id.name)
        val price = view.findViewById<EditText>(R.id.price)
        val quant = view.findViewById<EditText>(R.id.quant)
        imgb?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        modb?.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, PICK_MODEL_REQUEST)
        }
        upload.setOnClickListener{
            lifecycleScope.launch {
                try {
                    val service = UploadRetrofitService.makeRetrofitService()

                    val imgFile = getFileFromUri(requireContext(), img!!)
                    val imgPart = MultipartBody.Part.createFormData("img", "image.png", imgFile.asRequestBody("image/png".toMediaTypeOrNull()))

                    val modelFile = getFileFromUri(requireContext(), model!!)
                    val modelPart = MultipartBody.Part.createFormData("model_3d", "model.glb", modelFile.asRequestBody("model/glb".toMediaTypeOrNull()))

                    val response = service.createProduct(
                        name.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                        price.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                        imgPart,
                        modelPart
                    )
                    if (response.isSuccessful) {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Error while uploading product", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Handle network or other errors
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    // Handle the selected image
                    img = data?.data
                    imgb?.text = img.toString()
                    Toast.makeText(requireContext(), "Image selected: $img", Toast.LENGTH_SHORT).show()
                }
                PICK_MODEL_REQUEST -> {
                    // Handle the selected 3D model
                    model = data?.data
                    // Do something with the selected model URI
                    modb?.text = model.toString()
                    Toast.makeText(requireContext(), "Model selected: $model", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun getFileFromUri(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val fileName: String? = getFileName(context, uri)
        val tempDir = context.cacheDir
        val tempFile = File.createTempFile("temp", fileName, tempDir)
        tempFile.deleteOnExit()

        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return result
    }
}