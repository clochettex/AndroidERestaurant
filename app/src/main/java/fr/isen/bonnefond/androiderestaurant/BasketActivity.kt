package fr.isen.bonnefond.androiderestaurant


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.bonnefond.androiderestaurant.databinding.ActivityBasketBinding
import org.json.JSONArray
import java.io.File

class BasketActivity : AppCompatActivity() {

    lateinit var binding: ActivityBasketBinding
    private val TOTAL_MESSAGE_PREFIX = "Total de la commande: "
    var total = 0.0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MonApplicationPreferences", Context.MODE_PRIVATE)

        val basketListString = sharedPreferences.getString("basketList", "[]")
        val basketList = JSONArray(basketListString)

        val basketStringBuilder = StringBuilder()

        var articleCount = 0

        for (i in 0 until basketList.length()) {
            if (articleCount >= 10) {
                break
            }
            val item = basketList.getJSONObject(i)
            val countValue = item.getInt("countValue")
            val nameValue = item.getString("nameValue")
            val totValue = item.getString("totValue")

            if(countValue != 0) {
                basketStringBuilder.append("$countValue $nameValue pour $totValue €\n")
                binding.textViewBasket.text = basketStringBuilder.toString()
                total += totValue.toDouble()
                articleCount++
            }

            binding.buttonCommande.isEnabled = binding.textViewBasket.text != "Votre panier est vide.. T_T"

            binding.buttonCommande.setOnClickListener {
                clearBasket()

                val intent = Intent(this, FinishActivity::class.java)
                startActivity(intent)
            }
            binding.textViewSup.setOnClickListener{
                clearBasket()
                total = 0.0
                val formattedTotal = "%.1f €".format(total)
                binding.textViewTotal.text = TOTAL_MESSAGE_PREFIX + formattedTotal
                binding.buttonCommande.isEnabled = false
            }
        }
        val formattedTotal = "%.1f €".format(total)
        binding.textViewTotal.text = TOTAL_MESSAGE_PREFIX + formattedTotal
    }
    @SuppressLint("SetTextI18n")
    private fun clearBasket() {
        val sharedPreferences = getSharedPreferences("MonApplicationPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val file = File(applicationContext.filesDir, "basket.json")
        file.delete()
        binding.textViewBasket.text = "Votre panier est vide.. T_T"
    }
}