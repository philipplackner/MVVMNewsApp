package com.androiddevs.mvvmnewsapp.ui.ui

import androidx.lifecycle.ViewModel
import com.androiddevs.mvvmnewsapp.ui.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel(){

}