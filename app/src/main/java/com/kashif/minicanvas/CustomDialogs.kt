package com.kashif.minicanvas

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.kashif.minicanvas.databinding.StrokeSizeDialogBinding

object CustomDialogs {

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
                myCanvasView.setStrokeWidth(value)
            }
            btnSelect.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

}