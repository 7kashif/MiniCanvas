package com.kashif.minicanvas

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kashif.minicanvas.databinding.ActivityMainBinding

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
    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addListeners()
    }

    private fun addListeners() {
        binding.apply {
//            cleanCanvas.setOnClickListener {
//                myCanvasView.clearCanvas()
//            }
            fabEdit.setOnClickListener {
                onEditFabClicked()
            }
            fabPathColor.setOnClickListener {
                Toast.makeText(this@MainActivity, "Path Color", Toast.LENGTH_LONG).show()
            }
            fabBgColor.setOnClickListener {
                Toast.makeText(this@MainActivity, "Background Color", Toast.LENGTH_LONG).show()
            }
            fabStrokeSize.setOnClickListener {
                Toast.makeText(this@MainActivity, "Stroke size", Toast.LENGTH_LONG).show()
            }
            fabClearCanvas.setOnClickListener {
                Toast.makeText(this@MainActivity, "Clear Canvas", Toast.LENGTH_LONG).show()
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
            fabPathColor.isVisible = !clicked
            fabBgColor.isVisible = !clicked
            fabStrokeSize.isVisible = !clicked
            fabClearCanvas.isVisible = !clicked
            fabPathColor.isEnabled = !clicked
            fabClearCanvas.isEnabled = !clicked
            fabBgColor.isEnabled = !clicked
            fabStrokeSize.isEnabled = !clicked
        }
    }

    private fun setAnimation() {
        binding.apply {
            if (!clicked) {
                fabPathColor.startAnimation(fromBottom)
                fabBgColor.startAnimation(fromBottom)
                fabStrokeSize.startAnimation(fromBottom)
                fabClearCanvas.startAnimation(fromBottom)
                fabEdit.startAnimation(rotateOpen)
            } else {
                fabPathColor.startAnimation(toBottom)
                fabBgColor.startAnimation(toBottom)
                fabStrokeSize.startAnimation(toBottom)
                fabClearCanvas.startAnimation(toBottom)
                fabEdit.startAnimation(rotateClose)
            }
        }
    }

}