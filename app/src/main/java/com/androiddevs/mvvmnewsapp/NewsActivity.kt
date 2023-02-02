package com.androiddevs.mvvmnewsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.databinding.ActivityNewsBinding
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

       bottomNavigationView.setupWithNavController(binding.newsNavHostFrag.getFragment<NavHostFragment>().navController)
    }
}
