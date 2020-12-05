package com.example.application.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GeneralViewModel : ViewModel() {
     var articleId: MutableLiveData<Int> = MutableLiveData<Int>()
     var userId : MutableLiveData<Int> = MutableLiveData<Int>()
}