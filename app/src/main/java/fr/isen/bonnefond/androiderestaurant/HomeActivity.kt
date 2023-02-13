package fr.isen.bonnefond.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import fr.isen.bonnefond.androiderestaurant.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonsListener()
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "HomeActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "HomeActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "HomeActivity onPause")
    }

    override fun onDestroy() {
        Log.d("LifeCycle", "HomeActivity onDestroy")
        super.onDestroy()
    }

    private fun buttonsListener(){
        binding.entrees.setOnClickListener {
            Log.d("button", "Click sur button entrées")
            Toast.makeText(this, "Entrées", Toast.LENGTH_LONG).show()
            showCategory(Category.ENTREES)
        }

        binding.plats.setOnClickListener {
            Log.d("button", "Click sur button plats")
            Toast.makeText(this, "Plats", Toast.LENGTH_LONG).show()
            showCategory(Category.PLATS)
        }

        binding.desserts.setOnClickListener {
            Log.d("button", "Click sur button desserts")
            Toast.makeText(this, "Desserts", Toast.LENGTH_LONG).show()
            showCategory(Category.DESSERTS)
        }
    }

    private fun showCategory(category: Category){
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.extraKey, category)
        startActivity(intent)
    }
}