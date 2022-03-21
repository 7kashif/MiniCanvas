package com.kashif.minicanvas

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.WindowManager
import com.kashif.minicanvas.databinding.StrokeSizeDialogBinding
import top.defaults.colorpicker.ColorPickerPopup
import java.io.File
import java.io.IOException

object Utils {

    fun strokeSizeDialog(
        inflater: LayoutInflater,
        context: Context,
        myCanvasView: MyCanvasView
    ) {
        val binding = StrokeSizeDialogBinding.inflate(inflater)
        val dialog = Dialog(context)
        dialog.apply {
            setContentView(binding.root)
            setCancelable(true)
            setCanceledOnTouchOutside(false)
            window?.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(R.color.transparent)
        }

        binding.apply {
            strokeSizeSlider.value = myCanvasView.getStrokeWidth()
            tvStrokeSize.text = myCanvasView.getStrokeWidth().toInt().toString()
            strokeSizeSlider.addOnChangeListener { _, value, _ ->
                tvStrokeSize.text = value.toInt().toString()
            }
            btnSelect.setOnClickListener {
                myCanvasView.setStrokeWidth(strokeSizeSlider.value)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private inline fun <T> sdk29AndUp(onSdk29:()->T): T? {
        return if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
            onSdk29()
        else null
    }


    fun saveDrawing(bitmap: Bitmap, context: Context):Boolean {
        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cachePath = File(context.cacheDir,"images")
        cachePath.mkdir()

        val contentValue = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return try {
            context.contentResolver.insert(imageCollection,contentValue)?.also {uri->
                context.contentResolver.openOutputStream(uri).use {stream->
                    if(!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream))
                        throw IOException("Could not save image file.")
                }
            }?: throw IOException("Could not create media store collection.")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun colorPickerPopUp(context: Context): ColorPickerPopup =
        ColorPickerPopup.Builder(context)
            .enableBrightness(false)
            .enableAlpha(false)
            .showIndicator(true)
            .okTitle("Select")
            .cancelTitle("Cancel")
            .showValue(true)
            .build()

}