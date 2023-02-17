package fr.isen.bonnefond.androiderestaurant

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request.Method
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.isen.bonnefond.androiderestaurant.databinding.ActivityMenuBinding
import fr.isen.bonnefond.androiderestaurant.network.MenuResult
import fr.isen.bonnefond.androiderestaurant.network.NetworkConstants
import fr.isen.bonnefond.androiderestaurant.network.Plate
import org.json.JSONArray
import org.json.JSONObject


enum class Category {ENTREES, PLATS, DESSERTS}

class MenuActivity : AppCompatActivity() {

    companion object {
        val extraKey = "extraKey"
    }

    lateinit var binding: ActivityMenuBinding
    lateinit var currentCategory : Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getSerializableExtra(extraKey) as? Category
        currentCategory = category ?: Category.ENTREES

        supportActionBar?.title = categoryName()
        makeRequest()

    }

    private fun makeRequest(){
        val queue = Volley.newRequestQueue(this)
        val params = JSONObject()
        params.put(NetworkConstants.idShopKey, 1)
        val request = JsonObjectRequest(
            Method.POST,
            NetworkConstants.url,
            params,
            {result ->
                //success of request
                Log.d("request", result.toString(2))
                parseData(result.toString())
            },
            {error ->
                //Error when request
                Log.d("request", error.toString())
            }
        )
        queue.add(request)
        //showDatas()
    }

    private fun parseData(data: String){
        val result = GsonBuilder().create().fromJson(data, MenuResult::class.java)
        val category = result.data.first { it.name == categoryFilterKey() }
        showDatas(category)
    }

    private fun showDatas(category: fr.isen.bonnefond.androiderestaurant.network.Category){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CustomAdapter(category.items){ plate ->
            showPlate(plate)
        }
    }

    private fun categoryName(): String{
        return when(this.currentCategory){
            Category.ENTREES -> getString(R.string.entrees)
            Category.PLATS -> getString(R.string.plats)
            Category.DESSERTS -> getString(R.string.desserts)
        }
    }

    private fun categoryFilterKey(): String{
        return when(currentCategory){
            Category.ENTREES -> "Entrées"
            Category.PLATS -> "Plats"
            Category.DESSERTS -> "Desserts"
        }
    }

    private fun showPlate(plate: Plate){
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.extraKeyDetail, plate)
        startActivity(intent)

    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "MenuActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "MenuActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "MenuActivity onPause")
    }

    override fun onDestroy() {
        Log.d("LifeCycle", "MenuActivity onDestroy")
        super.onDestroy()
    }
}