package com.example.application.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.home.models.CommentItem
import com.example.application.home.models.RecyclerItem

class GeneralViewModel : ViewModel() {
     var articleId: Int = 0
     var userId : Int = 0
     var comments : MutableLiveData<MutableList<CommentItem>> = MutableLiveData<MutableList<CommentItem>>()
     var currentArticle : MutableLiveData<RecyclerItem> = MutableLiveData<RecyclerItem>()
     var ratingPair: MutableLiveData<Pair<Double,Int>> = MutableLiveData<Pair<Double,Int>>()
     var ratedArticles: MutableLiveData<MutableList<String>> = MutableLiveData<MutableList<String>>()
}