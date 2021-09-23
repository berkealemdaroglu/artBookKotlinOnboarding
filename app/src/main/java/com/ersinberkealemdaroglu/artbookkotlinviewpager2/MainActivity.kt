package com.ersinberkealemdaroglu.artbookkotlinviewpager2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ersinberkealemdaroglu.artbookkotlinviewpager2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var artList = ArrayList<ArtManager>()
    private lateinit var artAdapter: ArtAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        artList = ArrayList()
        artAdapter = ArtAdapter(artList)

        binding.receylerView.layoutManager = LinearLayoutManager(this)
        binding.receylerView.adapter = artAdapter

        binding.nullReceylerViewImage.visibility = View.INVISIBLE
        binding.nullReceylerViewText.visibility = View.INVISIBLE
        binding.receylerView.visibility = View.VISIBLE


        try {
            val database = this.openOrCreateDatabase("ArtsDataBase", MODE_PRIVATE,null)
            val cursor = database.rawQuery("SELECT * FROM arts",null)

            val artNameIndex = cursor.getColumnIndex("artname")
            val artIdIndex = cursor.getColumnIndex("id")

            while (cursor.moveToNext()){
                val name = cursor.getString(artNameIndex)
                var id = cursor.getInt(artIdIndex)
                val artManager = ArtManager(name,id)

                artList.add(artManager)


            }

            artAdapter.notifyDataSetChanged()
            cursor.close()
            if (artAdapter.itemCount.equals(0)){
                binding.nullReceylerViewText.visibility = View.VISIBLE
                binding.nullReceylerViewImage.visibility = View.VISIBLE
                binding.receylerView.visibility = View.INVISIBLE


            }



        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    fun goArtActivity(view: View){
        val intent = Intent(this,ArtActivity::class.java)
        intent.putExtra("infos","newArt")
        startActivity(intent)
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.artMenuId){
            val intent = Intent(this,ArtActivity::class.java)
            intent.putExtra("infos","newArt")
            startActivity(intent)

        }

        //finish()
        return super.onOptionsItemSelected(item)
    }

}