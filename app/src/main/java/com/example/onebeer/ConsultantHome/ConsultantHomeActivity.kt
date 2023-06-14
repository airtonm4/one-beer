package com.example.onebeer.ConsultantHome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.example.onebeer.R
import com.example.onebeer.databinding.ConsultantActivityBinding

class ConsultantHomeActivity: AppCompatActivity() {
    private lateinit var binding: ConsultantActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ConsultantActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        /**
         * Iniciando o fragment.
         */
        supportFragmentManager.commit {
            val homePage = ConsultantHomePage()
            replace(R.id.container, homePage)
        }
        /**
         * Configurando o floating button.
         */
        binding.floatingActionButton.setOnClickListener {
            val modalBottomSheet = AddProductBottomSheet()
            val ft = this.supportFragmentManager.beginTransaction()
            modalBottomSheet.show(ft, AddProductBottomSheet.TAG)
        }
    }
}