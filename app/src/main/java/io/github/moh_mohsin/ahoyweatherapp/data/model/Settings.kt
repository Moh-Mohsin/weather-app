package io.github.moh_mohsin.ahoyweatherapp.data.model

import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale

data class Settings(
    val tempScale: TempScale = TempScale.METRIC
)
