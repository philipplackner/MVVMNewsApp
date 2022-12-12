package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val repository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository,application)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController= navHostFragment.navController
        val bottomNavigationView = findViewById<MeowBottomNavigation>(R.id.bottomNavigationView)
        bottomNavigationView.add(MeowBottomNavigation.Model(1, R.drawable.ic_favorite))
        bottomNavigationView.add(MeowBottomNavigation.Model(2, R.drawable.ic_breaking_news))
        bottomNavigationView.add(MeowBottomNavigation.Model(3, R.drawable.ic_all_news))

        bottomNavigationView.show(2)

        bottomNavigationView.setOnClickMenuListener {
            when (it.id) {
                1 ->  {
                    navController.navigateUp()
                    navController.navigate(R.id.savedNewsFragment)
                }
                2 -> {
                    navController.navigateUp()
                    navController.navigate(R.id.breakingNewsFragment)
                }
                3 -> {
                    navController.navigateUp()
                    navController.navigate(R.id.searchNewsFragment)
                }
            }
        }
    }
}
