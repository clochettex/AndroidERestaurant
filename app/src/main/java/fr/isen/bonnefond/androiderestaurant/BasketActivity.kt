package fr.isen.bonnefond.androiderestaurant


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import fr.isen.bonnefond.androiderestaurant.databinding.ActivityBasketBinding
import org.json.JSONArray
import java.io.File

class BasketActivity : AppCompatActivity() {

    lateinit var binding: ActivityBasketBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MonApplicationPreferences", Context.MODE_PRIVATE)

        val basketListString = sharedPreferences.getString("basketList", "[]")
        val basketList = JSONArray(basketListString)

        val basketStringBuilder = StringBuilder()
        var total = 0.0

        for (i in 0 until basketList.length()) {
            val item = basketList.getJSONObject(i)
            val countValue = item.getInt("countValue")
            val nameValue = item.getString("nameValue")
            val totValue = item.getString("totValue")
            val basketTextView = findViewById<TextView>(R.id.textViewBasket)
            val buttonClear = findViewById<Button>(R.id.buttonClear)

            if(countValue != 0) {
                basketStringBuilder.append("$countValue $nameValue pour $totValue €\n")
                basketTextView.text = basketStringBuilder.toString()
                total += totValue.toDouble()
            }

            if (basketTextView.text == "Votre panier est vide.. T_T") {
                buttonClear.isEnabled = false
            }
            if (basketTextView.text != "Votre panier est vide.. T_T") {
                buttonClear.isEnabled = true
            }

            buttonClear.setOnClickListener {
                val sharedPreferences = getSharedPreferences("MonApplicationPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                val file = File(applicationContext.filesDir, "basket.json")
                file.delete()
                basketTextView.text = ""

                val intent = Intent(this, FinishActivity::class.java)
                startActivity(intent)

            }
        }
        val totalTextView = findViewById<TextView>(R.id.textViewTotal)
        val formattedTotal = "%.2f €".format(total)
        totalTextView.text = formattedTotal
        totalTextView.text = "Total de la commande: $total €"
    }
}