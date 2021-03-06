package com.coddeaddict.githubrepositories.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.coddeaddict.githubrepositories.R
import com.coddeaddict.githubrepositories.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.repoListFragment))
        val navController = findNavController(R.id.repoListFragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!findNavController(R.id.repoListFragment).navigateUp()) finish()
        return true
    }
}