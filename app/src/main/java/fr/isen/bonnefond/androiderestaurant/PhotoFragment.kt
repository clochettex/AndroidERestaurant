package fr.isen.bonnefond.androiderestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.isen.bonnefond.androiderestaurant.databinding.FragmentPhotoBinding
import com.squareup.picasso.Picasso

class PhotoFragment : Fragment() {

    private var image: String? = null
    lateinit var binding: FragmentPhotoBinding

    companion object {
        val ARG_PARAM1 = "ARG_PARAM1"

        fun newInstance(image: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, image)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        Picasso.get().load(image).into(binding.imageView)
        return binding.root
    }
}