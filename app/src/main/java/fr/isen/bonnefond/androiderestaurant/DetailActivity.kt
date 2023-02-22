package fr.isen.bonnefond.androiderestaurant

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    @SuppressLint("SetTextI18n")
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

        binding.buttonPlus.setOnClickListener {
            count++
            binding.textViewQuantity.text = count.toString()
            val newPrice = convertString(price)
            val tot = newPrice?.let { it1 -> multiply(it1,count) }
            binding.buttonAddToBasket.text = ("Ajouter au panier: $tot €").toString()
        }

        binding.buttonMinus.setOnClickListener {
            if (count > 0) {
            count--
            binding.textViewQuantity.text = count.toString()
            val newPrice = convertString(price)
            val tot = newPrice?.let { it1 -> multiply(it1,count) }
            binding.buttonAddToBasket.text = ("Ajouter au panier: $tot €").toString()
            }
        }

        binding.buttonAddToBasket.setOnClickListener {
                val newPrice = convertString(price)
                val tot = newPrice?.let { it1 -> multiply(it1, count) }
                if (count != 0) {
                    val basketFile =
                        mapOf("Nouvel ajout " to count.toString() + " " + plate?.name.toString() + " pour " + tot.toString() + "€")
                    val basketJson = Gson().toJson(basketFile)
                    val fileOutputStream = openFileOutput("basket.json", Context.MODE_APPEND)
                    fileOutputStream.write(basketJson.toByteArray())
                    fileOutputStream.close()

                    val view = findViewById<View>(android.R.id.content)
                    Snackbar.make(
                        view,
                        "Vous avez ajouté " + (tot.toString()) + "€ au panier",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
                val sharedPreferences =
                    getSharedPreferences("MonApplicationPreferences", Context.MODE_PRIVATE)
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
        }

        binding.imageViewBasket.setOnClickListener {
            val newPrice = convertString(price)
            val tot = newPrice?.let { it1 -> multiply(it1,count) }
            val basketFile = mapOf("Nouvel ajout " to count.toString() + " " + plate?.name.toString() + " pour " + tot.toString() + "€")
            val basketJson = Gson().toJson(basketFile)
            val intent = Intent(this, BasketActivity::class.java)

            intent.putExtra("basketJson", basketJson)
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