package com.androiddevs.mvvmnewsapp

import androidx.lifecycle.ViewModel
import com.androiddevs.mvvmnewsapp.db.NewsRepository

class NewsViewModel(private val newsRepo : NewsRepository):ViewModel() {
}