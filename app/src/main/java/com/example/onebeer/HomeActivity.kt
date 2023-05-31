package com.example.onebeer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.setPadding
import androidx.fragment.app.commit
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

        supportFragmentManager.commit {
            val homePage = HomePage()
            replace(R.id.container, homePage)
        }

        binding.bottomAppBar.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.home_page -> {
                    supportFragmentManager.commit {
                        val homePage = HomePage()
                        replace(R.id.container, homePage)
                    }
                    true
                }
                R.id.market_page -> {
                    supportFragmentManager.commit {
                        val marketPage = MarketPage()
                        replace(R.id.container, marketPage)
                    }
                    true
                }
                R.id.cart_page -> {
                    supportFragmentManager.commit {
                        val homePage = HomePage()
                        replace(R.id.container, homePage)
                    }
                    true
                }
                R.id.profile_page -> {
                    supportFragmentManager.commit {
                        val profilePage = ProfilePage()
                        replace(R.id.container, profilePage)
                    }
                    true
                }
                else -> false
            }

        }



    }
}