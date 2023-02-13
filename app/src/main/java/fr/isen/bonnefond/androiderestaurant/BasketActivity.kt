package fr.isen.bonnefond.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.bonnefond.androiderestaurant.databinding.ActivityBasketBinding

class BasketActivity : AppCompatActivity() {

    lateinit var binding: ActivityBasketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}