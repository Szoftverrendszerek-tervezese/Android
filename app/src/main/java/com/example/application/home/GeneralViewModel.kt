package com.example.application.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.home.models.RecyclerItem

class GeneralViewModel : ViewModel() {
     var articleId: Int = 0
     var userId : Int = 0
     var currentArticle : MutableLiveData<RecyclerItem> = MutableLiveData<RecyclerItem>()
}