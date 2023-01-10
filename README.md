# Weather App
A weather app that displays Weather for the current location (day and hourly) and the weather for the coming days.
Weather for a particular city can also be displayed. Cities are searchable and can be added to favorites for faster access.

# Built with
### Architecture
The app is built with MVVM. The layers from bottom to top are:
- Data Sources (Remote/Local)
- Repositories (Offline first)
- Use Cases 
- View Models
- Fragments (Single Activity Architecture) 

### Tech
- Kotlin
- Coroutines & Flow
- Kodein (for DI)
- Retrofit
- Room
- Navigation Component
- View Binding
- ViewModels (Architecture component)   
- LiveData

### API
The free tier from [OpenWeatherMap API](https://openweathermap.org/api) is used. API key can be found in Build.gradle.

# Flavors
- Staging
- Production