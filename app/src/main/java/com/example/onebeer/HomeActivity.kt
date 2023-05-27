package com.example.onebeer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.setPadding
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.onebeer.databinding.ActivityHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.bottomAppBar.setOnApplyWindowInsetsListener(null)
        binding.bottomAppBar.setPadding(0)
        setContentView(binding.root)

        Log.d("Home", "Rendered home")

        val navController = findNavController(R.id.nav_host_fragment_content_home)

    }
}