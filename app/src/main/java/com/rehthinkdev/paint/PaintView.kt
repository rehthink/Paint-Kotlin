package com.rehthinkdev.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View


class PaintView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var startPoint: PointF?= null
    private var endPoint: PointF?= null
    private var mPath: Path? = null
    private val mPaint: Paint = Paint()
    private val paths: ArrayList<InkPath> = ArrayList()
    private var currentColor = 0
    private val backgroundColor = DEFAULT_BG_COLOR
    private var strokeWidth = 0
    private var emboss = false
    private var blur = false
    private var isDrawing = false
    private var isCircle = false
    private var isLine = false
    private var mEmboss: MaskFilter? = null
    private var mBlur: MaskFilter? = null
    private lateinit var mBitmap: Bitmap

    private var isRed = false
    private var isBlue = false
    private var isGreen = false
    private var isBlack = false




    //private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mBitmapPaint: Paint = Paint(Paint.DITHER_FLAG)

    constructor(context: Context?) : this(context, null) {}

    companion object {
        var BRUSH_SIZE = 20
        val DEFAULT_COLOR: Int = Color.BLACK
        val DEFAULT_BG_COLOR: Int = Color.WHITE
        private const val TOUCH_TOLERANCE = 4f
    }

    init {
        mPaint.setAntiAlias(true)
        mPaint.setDither(true)
        mPaint.setColor(DEFAULT_COLOR)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setStrokeJoin(Paint.Join.ROUND)
        mPaint.setStrokeCap(Paint.Cap.ROUND)
        mPaint.setXfermode(null)
        mPaint.setAlpha(0xff)

        mEmboss = EmbossMaskFilter(floatArrayOf(1f, 1f, 1f), 0.4f, 6F, 3.5f)
        mBlur = BlurMaskFilter(5F, BlurMaskFilter.Blur.NORMAL)

    }

    fun init(metrics: DisplayMetrics) {
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
        currentColor = DEFAULT_COLOR
        strokeWidth = BRUSH_SIZE
    }

    fun red() {
        isRed = true
        isGreen = false
        isBlack = false
        isBlue = false
    }


    fun green() {
        isRed = false
        isGreen = true
        isBlack = false
        isBlue = false
    }

    fun blue() {
        isRed = false
        isGreen = false
        isBlack = false
        isBlue = true
    }


    fun black() {
        isRed = false
        isGreen = false
        isBlack = true
        isBlue = false

    }


    fun normal() {
        isCircle = false
        emboss = false
        isLine = false
        isDrawing = false
    }

    fun emboss() {
        isCircle = false
        emboss = true
        blur = false
        isDrawing = false
    }

    fun line() {
        isCircle = false
        emboss = false
        isDrawing = false
        isLine = true

    }

    fun drawRect() {
        isLine = false
        emboss = false
        isCircle = false
        isDrawing = true
    }

    fun drawArc() {
        isLine = false
        emboss = false
        isDrawing = false
        isCircle = true
    }

    /*fun clear() {
        backgroundColor
        paths.clear()
        normal()
        invalidate()
    }*/


    fun onColor(color: Color) {
        if (isBlack){
            mPaint.setColor(Color.BLACK)
        }
        else if (isBlue){
            mPaint.setColor(Color.BLUE)
        }
        else if (isGreen){
            mPaint.setColor(Color.GREEN)
        }
        else if (isRed) {
            mPaint.setColor(Color.RED)
        }

    }

    override fun onDraw(canvas: Canvas) {

        canvas.save()
        mCanvas!!.drawColor(backgroundColor)
        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mPaint.maskFilter = null
            if (fp.emboss)
                mPaint.maskFilter = mEmboss
            else if (fp.blur)
                mPaint.maskFilter = mBlur
            else if(isDrawing) {
                mCanvas!!.drawRect(startPoint!!.x,startPoint!!.y,endPoint!!.x,endPoint!!.y,mPaint!!)
            }else if(isCircle){
                mCanvas!!.drawCircle(startPoint!!.x,startPoint!!.y,endPoint!!.x,mPaint!!)
            }else if(isLine){
                mCanvas!!.drawLine(startPoint!!.x,startPoint!!.y,endPoint!!.x,y,mPaint!!)
            }
            else {
                mCanvas!!.drawPath(fp.path, mPaint)
            }
        }


        canvas.drawBitmap(mBitmap, 0f,0f,mBitmapPaint)
        canvas.restore()
    }

    /*
    private fun drawRectangle(x: Float, y: Float): MaskFilter? {
        val rectangle = Rect(
            (attr.x - 0.8 * mX).toInt(),
            (attr.y - 0.6 * mY).toInt(),
            (attr.x + 0.8 * mX).toInt(),
            (attr.y + 0.6 * mY).toInt()
        )

        mCanvas?.drawRect(rectangle, mBitmapPaint);

        return true

    }

     */

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val fp = InkPath(currentColor, emboss, blur, strokeWidth, mPath!!)
        paths.add(fp)
        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath!!.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mX, mY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                startPoint = PointF(event.x, event.y)
                endPoint = PointF()
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                endPoint!!.x = event.x
                endPoint!!.y = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {

                touchUp()
                endPoint!!.x = event.x
                endPoint!!.y = event.y
                invalidate()
            }
        }
        return true
    }


}
