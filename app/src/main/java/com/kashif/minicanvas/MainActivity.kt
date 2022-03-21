package com.kashif.minicanvas

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kashif.minicanvas.databinding.ActivityMainBinding
import top.defaults.colorpicker.ColorPickerPopup

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom)
    }
    private val fromBottomHorizontal: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_horizontal)
    }
    private val toBottomHorizontal: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_horizontal)
    }
    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addListeners()
    }

    private fun addListeners() {
        binding.apply {
            fabEdit.setOnClickListener {
                onEditFabClicked()
            }
            fabPathColor.setOnClickListener {
                Utils.colorPickerPopUp(this@MainActivity)
                    .show(it, object : ColorPickerPopup.ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {
                            myCanvasView.setDrawingColor(color)
                        }
                    })
            }
            fabBgColor.setOnClickListener {
                Utils.colorPickerPopUp(this@MainActivity)
                    .show(it, object : ColorPickerPopup.ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {
                            myCanvasView.setBgColor(color)
                        }
                    })
            }
            fabStrokeSize.setOnClickListener {
                Utils.strokeSizeDialog(
                    layoutInflater,
                    this@MainActivity,
                    myCanvasView
                )
            }
            fabClearCanvas.setOnClickListener {
                myCanvasView.clearCanvas()
            }
            fabSaveDrawing.setOnClickListener {
                val bitmap = myCanvasView.getBitmap()
                val isSaved = Utils.saveDrawing(bitmap,this@MainActivity)
                if(isSaved)
                    Toast.makeText(this@MainActivity,"Drawing Saved.",Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this@MainActivity,"Drawing Saving Failed.",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onEditFabClicked() {
        setVisibility()
        setAnimation()
        clicked = !clicked
    }

    private fun setVisibility() {
        binding.apply {
            //toggling visibility of buttons
            fabPathColor.isVisible = !clicked
            fabBgColor.isVisible = !clicked
            fabStrokeSize.isVisible = !clicked
            fabClearCanvas.isVisible = !clicked
            fabSaveDrawing.isVisible = !clicked
            //enabling/disabling buttons
            fabPathColor.isEnabled = !clicked
            fabClearCanvas.isEnabled = !clicked
            fabBgColor.isEnabled = !clicked
            fabStrokeSize.isEnabled = !clicked
            fabSaveDrawing.isEnabled = !clicked
        }
    }

    private fun setAnimation() {
        binding.apply {
            if (!clicked) {
                fabSaveDrawing.startAnimation(fromBottomHorizontal)
                fabPathColor.startAnimation(fromBottom)
                fabBgColor.startAnimation(fromBottom)
                fabStrokeSize.startAnimation(fromBottom)
                fabClearCanvas.startAnimation(fromBottom)
                fabEdit.startAnimation(rotateOpen)
            } else {
                fabSaveDrawing.startAnimation(toBottomHorizontal)
                fabPathColor.startAnimation(toBottom)
                fabBgColor.startAnimation(toBottom)
                fabStrokeSize.startAnimation(toBottom)
                fabClearCanvas.startAnimation(toBottom)
                fabEdit.startAnimation(rotateClose)
            }
        }
    }

}