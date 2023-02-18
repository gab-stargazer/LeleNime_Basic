package com.lelestacia.lelenimexml

import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lelestacia.lelenimexml.databinding.ActivityMainBinding
import com.lelestacia.lelenimexml.feature.collection.R.id.collectionFragment
import com.lelestacia.lelenimexml.feature.explore.R.id.exploreFragment
import com.lelestacia.lelenimexml.feature.others.R.id.settingsFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 10f
        setSupportActionBar(binding.toolbar)
        val navView: BottomNavigationView = binding.bottomNavigationMain
        val fragmentContainerView =
            supportFragmentManager.findFragmentById(binding.navHostFragmentContentMain.id) as NavHostFragment
        val navController = fragmentContainerView.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top-level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                exploreFragment,
                collectionFragment,
                settingsFragment,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (arguments?.getBoolean("fullscreen", false) == true) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }

            if (arguments?.getBoolean("dashboard", false) == true) {
                val white = ResourcesCompat.getColor(
                    resources,
                    com.lelestacia.lelenimexml.feature.common.R.color.white,
                    resources.newTheme()
                )
                binding.toolbar.visibility = View.GONE
                window.statusBarColor = white
            } else {
                val colorPrimary = ResourcesCompat.getColor(
                    resources,
                    com.lelestacia.lelenimexml.feature.common.R.color.light_primary,
                    resources.newTheme()
                )
                binding.toolbar.visibility = View.VISIBLE
                window.statusBarColor = colorPrimary
            }
            navView.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(binding.navHostFragmentContentMain.id)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
