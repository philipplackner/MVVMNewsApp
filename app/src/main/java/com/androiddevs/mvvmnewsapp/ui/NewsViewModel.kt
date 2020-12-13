package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.ViewModel
import com.androiddevs.mvvmnewsapp.repositories.NewsRepository

class NewsViewModel(
    val repository: NewsRepository
) : ViewModel() {
}
