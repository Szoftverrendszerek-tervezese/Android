package com.example.application.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.home.models.CommentItem
import com.example.application.home.models.RecyclerItem

class GeneralViewModel : ViewModel() {
     var articleId: Int = 0
     var userId : Int = 0

     //I will need this to display the comment on the choosen article
     var comments : MutableLiveData<MutableList<CommentItem>> = MutableLiveData<MutableList<CommentItem>>()

     //this I will need for the search fragment
     var articles : MutableList<RecyclerItem> = mutableListOf()
}