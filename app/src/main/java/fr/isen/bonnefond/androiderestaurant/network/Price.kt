package fr.isen.bonnefond.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Price(
    @SerializedName("price") val price: String
) : Serializable