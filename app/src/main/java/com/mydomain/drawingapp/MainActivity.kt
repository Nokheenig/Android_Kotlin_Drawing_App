package com.mydomain.drawingapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            findViewById<ImageView>(R.id.gallery_image).setImageURI(result.data?.data)
        }

    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value

                if (isGranted && permissionName == android.Manifest.permission.READ_EXTERNAL_STORAGE) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)
                } else {
                    if (permissionName == android.Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

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

    private fun requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            // Case where the user tries to access a function requirign a permission he declined beforehand
            showRationaleDialog()
        } else {
            // Case the user agreed to leave access to the external storage to the application
            requestPermission.launch(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        }
    }

    private fun showRationaleDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Storage permission")
            .setMessage("We need this permission in order to access the internal storage")
            .setPositiveButton(R.string.dialog_yes) {dialog, _ ->
                requestPermission.launch(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                )
                dialog.dismiss()
            }
        builder.create().show()
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
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission()
                } else {
                    // get the image
                    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)
                }
            }
            R.id.color_picker_button -> {
                showColorPickerDialog()
            }

        }
    }
}