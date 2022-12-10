package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.ViewModel
import com.androiddevs.mvvmnewsapp.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
): ViewModel(){
}