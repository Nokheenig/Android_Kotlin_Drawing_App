package com.mydomain.drawingapp

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var  drawingView: DrawingView

    private lateinit var  purpleButton: ImageButton
    private lateinit var  redButton: ImageButton
    private lateinit var  orangeButton: ImageButton
    private lateinit var  greenButton: ImageButton
    private lateinit var  blueButton: ImageButton

    private lateinit var  brushButton: ImageButton
    private lateinit var  undoButton: ImageButton
    private lateinit var  galleryButton: ImageButton
    private lateinit var  saveButton: ImageButton
    private lateinit var  colorPickerButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        purpleButton = findViewById(R.id.purple_button)
        redButton = findViewById(R.id.red_button)
        orangeButton = findViewById(R.id.orange_button)
        greenButton = findViewById(R.id.green_button)
        blueButton = findViewById(R.id.blue_button)

        purpleButton.setOnClickListener(this)
        redButton.setOnClickListener(this)
        orangeButton.setOnClickListener(this)
        greenButton.setOnClickListener(this)
        blueButton.setOnClickListener(this)

        brushButton = findViewById(R.id.brush_button)
        undoButton = findViewById(R.id.undo_button)
        saveButton = findViewById(R.id.save_button)
        galleryButton = findViewById(R.id.gallery_button)
        colorPickerButton = findViewById(R.id.color_picker_button)

        brushButton.setOnClickListener{
            showBrushChooserDialog()
        }

        undoButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)
        galleryButton.setOnClickListener(this)
        colorPickerButton.setOnClickListener(this)

        drawingView = findViewById(R.id.drawing_view)
        drawingView.changeBrushSize(23.toFloat())

    }

    private fun showBrushChooserDialog(){
        val brushDialog = Dialog(this@MainActivity)
        brushDialog.setContentView(R.layout.dialog_brush)
        val seekBarProgress = brushDialog.findViewById<SeekBar>(R.id.dialog_seek_bar)
        val showProgressTv = brushDialog.findViewById<TextView>(R.id.dialog_text_view_progress)

        seekBarProgress.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, p1: Int, p2: Boolean) {
                drawingView.changeBrushSize(seekBar.progress.toFloat())
                showProgressTv.text = seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        brushDialog.show()
    }

    private fun showColorPickerDialog () {
        val dialog = AmbilWarnaDialog(this, Color.GREEN, object: OnAmbilWarnaListener{
            override fun onCancel(dialog: AmbilWarnaDialog?) {

            }

            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                drawingView.setColor(color)
            }

        })
        dialog.show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.purple_button -> {
                drawingView.setColor("#AA66CC")
            }
            R.id.red_button -> {
                drawingView.setColor("#ED4A4A")
            }
            R.id.orange_button -> {
                drawingView.setColor("#F8B837")
            }
            R.id.green_button -> {
                drawingView.setColor("#77E350")
            }
            R.id.blue_button -> {
                drawingView.setColor("#36B9E9")
            }
            /*R.id.brush_button -> {
                view.showBrushChooserDialog()
            }*/
            R.id.undo_button -> {
                drawingView.undoPath()
            }
            R.id.save_button -> {
                Toast.makeText(this@MainActivity, "Save button has been pressed", Toast.LENGTH_SHORT).show()
            }
            R.id.gallery_button -> {
                Toast.makeText(this@MainActivity, "Gallery button has been pressed", Toast.LENGTH_SHORT).show()
            }
            R.id.color_picker_button -> {
                showColorPickerDialog()
            }

        }
    }
}