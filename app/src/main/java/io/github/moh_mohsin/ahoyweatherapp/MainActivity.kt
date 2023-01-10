package io.github.moh_mohsin.ahoyweatherapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import io.github.moh_mohsin.ahoyweatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController }
    private val appBarConfiguration by lazy { buildAppBarConfiguration() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setUpNavigation()
    }
    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            navHostFragment!!.navController
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            handleDestinationChange(destination)
        }
    }

    private fun handleDestinationChange(destination: NavDestination) {
        when (destination.id) {
            in appBarConfiguration.topLevelDestinations -> {
                showNav()
            }
            else -> {
                hideNav()
            }
        }
    }

    private fun hideNav() {
        binding.bottomNavigation.visibility = View.GONE
    }

    private fun showNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun buildAppBarConfiguration(): AppBarConfiguration {
        return AppBarConfiguration.Builder(hashSetOf(R.id.current_weather, R.id.favorite_cities))
            .build()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}