package io.github.moh_mohsin.ahoyweatherapp.ui.weather.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import io.github.moh_mohsin.ahoyweatherapp.data.model.Daily
import io.github.moh_mohsin.ahoyweatherapp.databinding.SingleDayWeatherBinding
import io.github.moh_mohsin.ahoyweatherapp.databinding.SingleHourWeatherBinding
import io.github.moh_mohsin.ahoyweatherapp.util.format
import java.util.*

class DailyWeatherAdapter :
    ListAdapter<Daily, RecyclerView.ViewHolder>(DailysDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DailyWeatherViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val daily = getItem(position)
        val dailyWeatherViewHolder = (holder as DailyWeatherViewHolder)
        dailyWeatherViewHolder.bind(daily)
    }

    class DailyWeatherViewHolder private constructor(private val binding: SingleDayWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: Daily) {
            binding.date.text =
                daily.dt.format("d, MMM") //TODO: put "tomorrow" on the next day
            binding.temp.text = "${daily.temp.max}° / ${daily.temp.min}°"
            daily.weather.firstOrNull()?.let { weatherDesc ->
                binding.weatherIcon.load(weatherDesc.icon)
            }
        }

        companion object {
            fun from(parent: ViewGroup): DailyWeatherViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SingleDayWeatherBinding
                    .inflate(layoutInflater, parent, false)
                return DailyWeatherViewHolder(binding)
            }
        }
    }

    class DailysDiffCallback : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily) =
            oldItem == newItem

    }
}
