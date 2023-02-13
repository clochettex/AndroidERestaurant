package fr.isen.bonnefond.androiderestaurant


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.bonnefond.androiderestaurant.databinding.CellCustomBinding
import fr.isen.bonnefond.androiderestaurant.network.Plate

class CustomAdapter(val items: List<Plate>, val clickListener: (Plate) -> Unit): RecyclerView.Adapter<CustomAdapter.CellViewHolder>() {
    class CellViewHolder(binding : CellCustomBinding) : RecyclerView.ViewHolder(binding.root){
        val textView : TextView = binding.textView
        val imageView : ImageView = binding.imageView
        val priceView : TextView = binding.priceView
        val root : ConstraintLayout = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val binding = CellCustomBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return CellViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val plate = items[position]
        holder.textView.text = plate.name
        holder.priceView.text = plate.price[0].price + " €"
        Picasso.get().load(getThumbnail(plate)).placeholder(R.drawable.no_image).into(holder.imageView)
        holder.root.setOnClickListener{
            Log.d("click", "Clic sur l'item$plate")
            clickListener(plate)
        }
    }

    fun getThumbnail(plate: Plate): String? {
        return if(plate.images.isNotEmpty() && plate.images.firstOrNull()?.isNotEmpty() == true){
            plate.images.firstOrNull()
        } else {
            null
        }
    }
}