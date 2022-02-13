package com.rehthinkdev.paint

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.rehthinkdev.paint.databinding.ActivityMainBinding
import com.rehthinkdev.paint.databinding.DialogLayoutBinding


class MainActivity : AppCompatActivity() {
    private var paintView: PaintView? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: DialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paintView = findViewById<View>(R.id.paintView) as PaintView
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        paintView!!.init(metrics)

        binding.apply {
            pencil.setOnClickListener {
                paintView.normal()
                pencil.setBackgroundColor(Color.parseColor("#E4E4E4"))
                arrow.setBackgroundColor(Color.WHITE)
                rectangles.setBackgroundColor(Color.WHITE)
                circle.setBackgroundColor(Color.WHITE)

                color.setBackgroundColor(Color.WHITE)
            }

            arrow.setOnClickListener {
                paintView.line()
                arrow.setBackgroundColor(Color.parseColor("#E4E4E4"))
                pencil.setBackgroundColor(Color.WHITE)
                rectangles.setBackgroundColor(Color.WHITE)
                circle.setBackgroundColor(Color.WHITE)
                color.setBackgroundColor(Color.WHITE)
            }

            rectangles.setOnClickListener {
                paintView.drawRect()
                rectangles.setBackgroundColor(Color.parseColor("#E4E4E4"))
                arrow.setBackgroundColor(Color.WHITE)
                pencil.setBackgroundColor(Color.WHITE)
                circle.setBackgroundColor(Color.WHITE)
                color.setBackgroundColor(Color.WHITE)
            }

            circle.setOnClickListener {
                paintView.drawArc()
                circle.setBackgroundColor(Color.parseColor("#E4E4E4"))
                arrow.setBackgroundColor(Color.WHITE)
                rectangles.setBackgroundColor(Color.WHITE)
                pencil.setBackgroundColor(Color.WHITE)
                color.setBackgroundColor(Color.WHITE)
            }

            color.setOnClickListener {

             //   val colorPicker = ColorPicker
                showDialog()

                color.setBackgroundColor(Color.parseColor("#E4E4E4"))
                arrow.setBackgroundColor(Color.WHITE)
                rectangles.setBackgroundColor(Color.WHITE)
                circle.setBackgroundColor(Color.WHITE)
                pencil.setBackgroundColor(Color.WHITE)

            }

        }

    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.show()








    }
}
