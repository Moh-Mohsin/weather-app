package io.github.moh_mohsin.ahoyweatherapp.ui.city.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.databinding.SingleCityBinding
import io.github.moh_mohsin.ahoyweatherapp.ui.citysearch.CityWithFavorite

class CityAdapter(
    private val onClick: (CityWithFavorite) -> Unit,
    private val addToFavorite: (CityWithFavorite) -> Unit = {},
    private val removeFromFavorite: (CityWithFavorite) -> Unit = {},
) :
    ListAdapter<CityWithFavorite, RecyclerView.ViewHolder>(CitesDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CityWithFavoriteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val city = getItem(position)
        val cityViewHolder = (holder as CityWithFavoriteViewHolder)
        cityViewHolder.bind(city, onClick, addToFavorite, removeFromFavorite)
    }

    class CityWithFavoriteViewHolder private constructor(private val binding: SingleCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            cityWithFavorite: CityWithFavorite,
            onClick: (CityWithFavorite) -> Unit,
            addToFavorite: (CityWithFavorite) -> Unit,
            removeFromFavorite: (CityWithFavorite) -> Unit
        ) {
            binding.city.text = "${cityWithFavorite.city.name} (${cityWithFavorite.city.country})"
            binding.root.setOnClickListener {
                onClick(cityWithFavorite)
            }

            val res =
                if (cityWithFavorite.favorite) R.drawable.ic_baseline_favorite else R.drawable.ic_baseline_favorite_border
            val drawable = AppCompatResources.getDrawable(binding.root.context, res)
            binding.favorite.setImageDrawable(drawable)
            binding.favorite.setOnClickListener {
                if (!cityWithFavorite.favorite)
                    addToFavorite(cityWithFavorite)
                else
                    removeFromFavorite(cityWithFavorite)
            }
        }

        companion object {
            fun from(parent: ViewGroup): CityWithFavoriteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SingleCityBinding
                    .inflate(layoutInflater, parent, false)
                return CityWithFavoriteViewHolder(binding)
            }
        }
    }

    class CitesDiffCallback : DiffUtil.ItemCallback<CityWithFavorite>() {
        override fun areItemsTheSame(oldItem: CityWithFavorite, newItem: CityWithFavorite) =
            oldItem.city == newItem.city

        override fun areContentsTheSame(oldItem: CityWithFavorite, newItem: CityWithFavorite) =
            oldItem == newItem

    }
}
