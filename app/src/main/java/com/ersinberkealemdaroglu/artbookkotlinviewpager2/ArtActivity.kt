package com.ersinberkealemdaroglu.artbookkotlinviewpager2

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.nfc.Tag
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.databinding.ActivityArtBinding
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.ByteArrayOutputStream
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import java.security.KeyStore


class ArtActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedBitmap : Bitmap? = null
    private lateinit var database: SQLiteDatabase
    var isImageFit : Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val actionBar = supportActionBar
        actionBar?.setTitle("Art Activity")

        registerLauncher()
        database = this.openOrCreateDatabase("ArtsDataBase", MODE_PRIVATE,null)

        val intent = intent
        val infos = intent.getStringExtra("infos")

        if (infos.equals("newArt")){
            binding.artNameText.setText("")
            binding.artistNameText.setText("")
            binding.yearArtText.setText("")
            binding.imageView.setImageResource(R.drawable.click_default)
            binding.button2.visibility = View.VISIBLE
        }else{
            val selectedId = intent.getIntExtra("id",2)
            val cursor = database.rawQuery("SELECT * FROM arts WHERE id = ?", arrayOf(selectedId.toString()))
            val artNameIndex = cursor.getColumnIndex("artname")
            val artistNameIndex = cursor.getColumnIndex("artsitname")
            val artYearIndex = cursor.getColumnIndex("artyear")
            val imageIndex = cursor.getColumnIndex("artimage")

            while (cursor.moveToNext()){
                binding.artNameText.setText(cursor.getString(artNameIndex))
                binding.artistNameText.setText(cursor.getString(artistNameIndex))
                binding.yearArtText.setText(cursor.getString(artYearIndex))

                binding.artNameText.isEnabled = false
                binding.artistNameText.isEnabled = false
                binding.yearArtText.isEnabled = false
                binding.button2.visibility =  View.INVISIBLE

                val byteArray = cursor.getBlob(imageIndex)
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                binding.imageView.setImageBitmap(bitmap)
            }
            cursor.close()

            binding.imageView.setOnClickListener {
                if (isImageFit){
                    isImageFit = false
                    binding.imageView.setLayoutParams(
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                    )
                    binding.imageView.adjustViewBounds = true
                    val intents = intent
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                }else{
                    val intent = intent
                    intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
                    startActivity(intent)
                }
            }
        }

    }

    fun save(view : View){
        val artNameText = binding.artNameText.text.toString()
        val artistNameText = binding.artistNameText.text.toString()
        val yearArtText = binding.yearArtText.text.toString()

        if (selectedBitmap != null){
            val smallerBitmap = makeSmallerBitmap(selectedBitmap!!,300)
            val outputStream = ByteArrayOutputStream()
            smallerBitmap.compress(Bitmap.CompressFormat.PNG, 100,outputStream)
            val byteArray = outputStream.toByteArray()


            database.execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER PRIMARY KEY, artname VARCHAR, artsitname VARCHAR, artyear VARCHAR, artimage BLOB)")

            val sqlString = "INSERT INTO arts (artname, artsitname, artyear, artimage) VALUES (?,?,?,?)"
            val statement = database.compileStatement(sqlString)
            statement.bindString(1,artNameText)
            statement.bindString(2,artistNameText)
            statement.bindString(3,yearArtText)
            statement.bindBlob(4,byteArray)
            statement.execute()

            val intent = Intent(this@ArtActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)


        }

    }

    private fun makeSmallerBitmap(image : Bitmap, maximumSize : Int) : Bitmap{
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1){
            //landspace
            width = maximumSize
            val scaleHeight = width / bitmapRatio
            height = scaleHeight.toInt()
        }else{
            //portrait
            height = maximumSize
            val scaleWidth = width / bitmapRatio
            width = scaleWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)
    }

    fun selectImage(view: View){


        when{
            ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {

                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)

            }
            else -> {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }

        /*if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission For Needed Gallery",Snackbar.LENGTH_LONG).setAction("Give Permission",View.OnClickListener {
                    //Request Permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                })
            }else{
                view.setOnClickListener {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }
        }else{
            //app gallery intent
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }*/

    }

    private fun registerLauncher(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if (result.resultCode == RESULT_OK){
                println("OK")
                val intentFromResult = result.data
                if (intentFromResult != null){
                    val imageUri = intentFromResult.data

                if (imageUri != null){
                    println("ok?")
                    try {
                        if (Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(this@ArtActivity.contentResolver, imageUri)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(selectedBitmap)
                        }else{
                            selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                            binding.imageView.setImageBitmap(selectedBitmap)

                        }

                    } catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result){
                //Permission Granted
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)

            }else{
                //permission denide
                    //Request Permission
                val showRationale : Boolean = shouldShowRequestPermissionRationale(permissionLauncher.toString())
                if (! showRationale){

                    Toast.makeText(this,"Gallery Permission Needed",Toast.LENGTH_SHORT).show()

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    activityResultLauncher.launch(intent)


                }else if(Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissionLauncher)) {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                        //Toast.makeText(this,"Needed Permission",Toast.LENGTH_SHORT).show()
            }
        }

    }

}

