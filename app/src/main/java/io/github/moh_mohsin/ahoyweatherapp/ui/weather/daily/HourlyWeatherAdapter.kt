package io.github.moh_mohsin.ahoyweatherapp.ui.weather.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherDetail
import io.github.moh_mohsin.ahoyweatherapp.databinding.SingleDayWeatherBinding
import io.github.moh_mohsin.ahoyweatherapp.databinding.SingleHourWeatherBinding
import io.github.moh_mohsin.ahoyweatherapp.util.format
import java.util.*

class HourlyWeatherAdapter :
    ListAdapter<WeatherDetail, RecyclerView.ViewHolder>(WeatherDetailsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HourWeatherViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weatherDetail = getItem(position)
        val weatherDetailViewHolder = (holder as HourWeatherViewHolder)
        weatherDetailViewHolder.bind(weatherDetail)
    }

    class HourWeatherViewHolder private constructor(private val binding: SingleHourWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            weatherDetail: WeatherDetail
        ) {
            binding.time.text = weatherDetail.dt.format("hh:mm a")
            binding.temp.text = "${weatherDetail.temp}°"
            weatherDetail.weather.firstOrNull()?.let { weatherDesc ->
                binding.weatherIcon.load(weatherDesc.icon)
            }
        }

        companion object {
            fun from(parent: ViewGroup): HourWeatherViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SingleHourWeatherBinding
                    .inflate(layoutInflater, parent, false)
                return HourWeatherViewHolder(binding)
            }
        }
    }

    class WeatherDetailsDiffCallback : DiffUtil.ItemCallback<WeatherDetail>() {
        override fun areItemsTheSame(oldItem: WeatherDetail, newItem: WeatherDetail) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: WeatherDetail, newItem: WeatherDetail) =
            oldItem == newItem

    }
}
