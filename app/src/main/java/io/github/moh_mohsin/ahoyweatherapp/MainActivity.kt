package io.github.moh_mohsin.ahoyweatherapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.moh_mohsin.ahoyweatherapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { buildAppBarConfiguration() }
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            navHostFragment.navController
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            handleDestinationChange(destination)
        }
    }

    private fun handleDestinationChange(destination: NavDestination) {
        binding.bottomNavigation.visibility  = when (destination.id) {
            in appBarConfiguration.topLevelDestinations -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun buildAppBarConfiguration(): AppBarConfiguration {
        return AppBarConfiguration.Builder(hashSetOf(R.id.current_weather, R.id.favorite_cities))
            .build()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}