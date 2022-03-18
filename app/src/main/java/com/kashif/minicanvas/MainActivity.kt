package com.kashif.minicanvas

import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.kashif.minicanvas.databinding.ActivityMainBinding
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val myCanvasView = MyCanvasView(this)
        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)
        setContentView(binding.root)
    }

}