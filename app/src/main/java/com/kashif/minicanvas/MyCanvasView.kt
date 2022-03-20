package com.kashif.minicanvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

class MyCanvasView(context: Context, attr: AttributeSet) : View(context, attr) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private var myStrokeWidth = 12f
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    //when the user stops touch, these will be the starting point for next segment to draw
    private var currentX = 0f
    private var currentY = 0f

    private var bgColor = ResourcesCompat.getColor(resources, R.color.white, null)
    private var drawColor = ResourcesCompat.getColor(resources, R.color.black, null)

    //while drawing we do not have to draw every pixel and make the screen refresh to set that change
    //instead we draw between two points, and by touch tolerance we define how much of touch movement
    //to ignore for drawing.
    //e.g if a user moves his finger very slightly there is no need to draw and only draw when the touch exceeds the
    //tolerance amount
    private var touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

//    private lateinit var frame: Rect

    private var paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true //reduces the colors to 256 bit range or even lower
        style = Paint.Style.STROKE // default: FILL, defines the painting is to be done
        strokeJoin =
            Paint.Join.ROUND // default: MITER, defines how the joint of two lines will look like
        strokeCap = Paint.Cap.ROUND // default: BUTT, defines how the end of a line will look like
        strokeWidth =
            myStrokeWidth // default: Hairline-width (really thin), defines the width of line being drawn
    }
    private var path = Path()

    /***
     * this function is called every time the view changes it's size
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        /**
         * as this function is called every time the view changes it's size so every time new bitmap
         * and canvas object is created which causes memory leaks.
         * To over come this issue we recycle the old bitmaps
         */
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        //ARGB_8888 stores each color in 4 bytes and is also recommended
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(bgColor)
//        val inset = 40 //just like padding
//        frame = Rect(inset,inset,w-inset,h-inset)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
//        canvas?.drawRect(frame,paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { ev ->
            motionTouchEventX = ev.x
            motionTouchEventY = ev.y

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> touchStart()
                MotionEvent.ACTION_MOVE -> touchMove()
                MotionEvent.ACTION_UP -> touchUp()
            }
        }
        return true
    }

    fun setDrawingColor(color: Int) {
        drawColor = color
        paint.color = drawColor
    }

    fun setBgColor(color: Int) {
        bgColor = color
        extraCanvas.save()
        extraCanvas.drawColor(bgColor)
        this.invalidate()
        extraCanvas.restore()
    }

    fun setStrokeWidth(width: Float) {
        myStrokeWidth = width
        paint.strokeWidth = myStrokeWidth
    }

    fun getStrokeWidth() = myStrokeWidth

    fun clearCanvas() {
        extraCanvas.drawColor(bgColor)
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        //calculate the distance that has been moved
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        //then we check either of the movement was greater than tolerance
        //with this it will not allow to draw a dot, simply move it out of 'if' to make it do so
        if (dx >= touchTolerance || dy >= touchTolerance) {
            //adding segment to the path, and using quadTo instead of lineTo because it creates smoothly drawn lines without corners
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            //setting the starting point of the next segment to end point of the current segment
            currentX = motionTouchEventX
            currentY = motionTouchEventY

            //draw the path in extra bitmap to cache it
            extraCanvas.drawPath(path, paint)
            extraCanvas.save()
        }
        //calling invalidate to eventually call onDraw and redraw the view
        invalidate()
    }

    private fun touchUp() {
        //reset the path so it does not get drawn again
        path.reset()
    }


}