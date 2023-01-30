package fr.isen.bonnefond.androiderestaurant

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import fr.isen.bonnefond.androiderestaurant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonsListener()
    }

    private fun buttonsListener(){
        binding.entrees.setOnClickListener {
            Log.d("button", "Click sur button entrées")
            Toast.makeText(this, "Entrées", Toast.LENGTH_LONG).show()
        }

        binding.plats.setOnClickListener {
            Log.d("button", "Click sur button plats")
            Toast.makeText(this, "Plats", Toast.LENGTH_LONG).show()
        }

        binding.desserts.setOnClickListener {
            Log.d("button", "Click sur button desserts")
            Toast.makeText(this, "Desserts", Toast.LENGTH_LONG).show()
        }
    }
}