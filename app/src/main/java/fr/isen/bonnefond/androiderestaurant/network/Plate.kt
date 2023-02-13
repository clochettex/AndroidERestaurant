package fr.isen.bonnefond.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Plate(
    @SerializedName("name_fr") val name:String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("prices") val price: List<Price>,
    @SerializedName("ingredients") val ingredient: List<Ingredient>
): Serializable

