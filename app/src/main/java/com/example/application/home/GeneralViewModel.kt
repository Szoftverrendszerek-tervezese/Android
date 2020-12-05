package com.example.application.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.home.models.CommentItem

class GeneralViewModel : ViewModel() {
     var articleId: Int = 0
     var userId : Int = 0
     var comments : MutableLiveData<MutableList<CommentItem>> = MutableLiveData<MutableList<CommentItem>>()
}