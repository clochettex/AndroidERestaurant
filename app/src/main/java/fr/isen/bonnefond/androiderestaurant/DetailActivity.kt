package fr.isen.bonnefond.androiderestaurant

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import fr.isen.bonnefond.androiderestaurant.databinding.ActivityDetailBinding
import fr.isen.bonnefond.androiderestaurant.network.Plate
import org.json.JSONArray
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    private var count = 0

    companion object {
        val extraKeyDetail = "extraKeyDetail"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val plate = intent.getSerializableExtra(extraKeyDetail) as? Plate

        val ingredients = plate?.ingredient?.map {
            it.name }?.joinToString(",\n")

        val price = plate?.price?.map {
            it.price }?.joinToString()

        binding.descriptionMeal.text = ingredients ?: ""
        binding.textView.text = plate?.name
        plate?.let {
            binding.viewPager2.adapter = PhotoAdapter(it.images, this)
        }

        val textViewQuantity = findViewById<TextView>(R.id.textViewQuantity)
        val buttonPlus = findViewById<Button>(R.id.buttonPlus)
        val buttonMinus = findViewById<Button>(R.id.buttonMinus)
        val buttonTotal = findViewById<Button>(R.id.buttonAddToBasket)
        val imageViewBasket = findViewById<ImageView>(R.id.imageViewBasket)

        buttonPlus.setOnClickListener {
            count++
            textViewQuantity.text = count.toString()

            val newPrice = convertString(price)

            val tot = newPrice?.let { it1 -> multiply(it1,count) }
            binding.buttonAddToBasket.text = ("Ajouter au panier: $tot €").toString()

        }

        buttonMinus.setOnClickListener {
            if (count > 0) {
            count--
            textViewQuantity.text = count.toString()

            val newPrice = convertString(price)

            val tot = newPrice?.let { it1 -> multiply(it1,count) }
            binding.buttonAddToBasket.text = ("Ajouter au panier: $tot €").toString()
            }
        }

        buttonTotal.setOnClickListener {

            val newPrice = convertString(price)
            val tot = newPrice?.let { it1 -> multiply(it1,count) }
            if (count != 0) {
                val basketFile = mapOf("Nouvel ajout " to count.toString() + " " + plate?.name.toString() + " pour " + tot.toString() + "€")
                val basketJson = Gson().toJson(basketFile)
                val fileOutputStream = openFileOutput("basket.json", Context.MODE_APPEND)
                fileOutputStream.write(basketJson.toByteArray())
                fileOutputStream.close()
                val view = findViewById<View>(android.R.id.content)
                Snackbar.make(view, "Vous avez ajouté " + (tot.toString()) + "€ au panier", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        imageViewBasket.setOnClickListener {
            val newPrice = convertString(price)
            val tot = newPrice?.let { it1 -> multiply(it1,count) }
            val basketFile = mapOf("Nouvel ajout " to count.toString() + " " + plate?.name.toString() + " pour " + tot.toString() + "€")
            val basketJson = Gson().toJson(basketFile)
            val intent = Intent(this, BasketActivity::class.java)
            intent.putExtra("basketJson", basketJson)

            val sharedPreferences = getSharedPreferences("MonApplicationPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val basketListString = sharedPreferences.getString("basketList", "[]")
            val basketList = JSONArray(basketListString)

            val newBasketItem = JSONObject()
            newBasketItem.put("countValue", count)
            newBasketItem.put("nameValue", plate?.name)
            newBasketItem.put("totValue", tot.toString())
            basketList.put(newBasketItem)

            editor.putString("basketList", basketList.toString())
            editor.apply()

            startActivity(intent)

        }
    }

    fun convertString(price: String?): Any? {
        return try {
            price?.toInt()
        } catch (e: NumberFormatException) {
            price?.toFloat()
        }
    }

    fun multiply(newPrice: Any, count: Int): Any {
        return when (newPrice) {
            is Int -> {
                newPrice * count
            }
            is Float -> {
                newPrice * count
            }
            else -> {
                "Incompatible types"
            }
        }
    }

}


