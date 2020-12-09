package com.example.application.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.home.models.CommentItem
import com.example.application.home.models.RecyclerItem

/*

This is a viewModel where we store any datas which
will be needed across the applcation like:

--> users
--> ratings
--> the currently clicked article
--> the comments of the current article

etc.
 */

class GeneralViewModel : ViewModel() {
    var articleId: Int = 0
    var userId: Int = 0
    var currentArticle: MutableLiveData<RecyclerItem> = MutableLiveData()
    var ratingPair: MutableLiveData<Pair<Double, Int>> = MutableLiveData()
    var ratedArticles: MutableLiveData<MutableList<String>> = MutableLiveData()
    var activityList: MutableLiveData<MutableList<String>> = MutableLiveData()
    var userCredentials: MutableLiveData<MutableList<Pair<String, String>>> = MutableLiveData()

    //to display the comment on the choosen article
    var comments: MutableLiveData<MutableList<CommentItem>> =
        MutableLiveData()

    //this I will need for the search fragment
    var articles: MutableList<RecyclerItem> =
        mutableListOf()


}