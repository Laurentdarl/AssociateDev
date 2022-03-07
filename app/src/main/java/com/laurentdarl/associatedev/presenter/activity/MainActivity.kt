package com.laurentdarl.associatedev.presenter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.welcome_container) as NavHostFragment
        val navController = navHost.navController

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.signinFragment
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
    }
}