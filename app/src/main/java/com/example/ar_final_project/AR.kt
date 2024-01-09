package com.example.ar_final_project

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ar.core.ArCoreApk
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.coroutines.launch

class AR : AppCompatActivity() {
    private var yourRenderable: ModelRenderable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        if (checkARCoreAvailability()) {
            lifecycleScope.launch {
                loadAndRenderModel(downloadModel(intent.getIntExtra("productId", -1)))
            }
        } else {
            // ARCore is not available on this device, handle accordingly
            // You can display a message or fallback to a non-AR experience
            Toast.makeText(this, "ARCore is not supported on this device", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkARCoreAvailability(): Boolean {
        val availability = ArCoreApk.getInstance().checkAvailability(this)
        return availability == ArCoreApk.Availability.SUPPORTED_INSTALLED
    }
    private suspend fun downloadModel(productId: Int): String {
        val retrofitService = ModelRetrofitService.ModelRetrofitServiceFactory.makeRetrofitService()
        val response = retrofitService.downloadModel(productId.toString())
        return response.modeluri
    }
    private fun loadAndRenderModel(modelUri: String) {
        ModelRenderable.builder()
            .setSource(
                this, RenderableSource.builder().setSource(
                    this,
                    Uri.parse(modelUri),
                    RenderableSource.SourceType.GLB
                )
                    .setScale(1.0f)
                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                    .build()
            )
            .setRegistryId(modelUri)
            .build()
            .thenAccept {
                loadModel(it)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(it.message)
                    .setTitle("error!")
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }
    }

    private fun loadModel(modelRenderable: ModelRenderable) {
        val arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult: HitResult, _, _ ->
            // Handle the tap on a plane (surface)

            // Create an anchor at the hit location
            val anchor = hitResult.createAnchor()

            // Use the anchor to place a node in the scene
            val node = AnchorNode(anchor)

            node.setParent(arFragment.arSceneView.scene)

            node.renderable = modelRenderable
            node.localPosition = Vector3(0.0f, 0.0f, 0.0f)

            //arFragment.arSceneView.scene.addChild(node)
        }


    }

    override fun onBackPressed() {
        finish() // Close ARActivity and go back to MainActivity
    }
}
