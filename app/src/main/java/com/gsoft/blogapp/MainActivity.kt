package com.gsoft.blogapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gsoft.blogapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.profileScreenFragment -> {
                    binding.navBar.visibility = View.GONE
                }
                R.id.agregarPropiedadScreenFragment -> {
                    binding.navBar.visibility = View.GONE
                }
                R.id.loginScreenFragment -> {
                    binding.navBar.visibility = View.GONE
                }
                R.id.crearCuentaFragment -> {
                    binding.navBar.visibility = View.GONE
                }

                R.id.homeScreenFragment -> {
                    binding.navBar.visibility = View.GONE
                }

                R.id.agregarFotosFragment -> {
                    binding.navBar.visibility = View.GONE
                }


                R.id.buscarEnMapaFragment -> {
                    binding.navBar.visibility = View.GONE
                }

                R.id.mapsFragment -> {
                    binding.navBar.visibility = View.GONE
                }

                R.id.pagarFragment -> {
                    binding.navBar.visibility = View.GONE
                }

                else -> {
                    binding.navBar.visibility = View.VISIBLE

                }
            }
        }


    }
}