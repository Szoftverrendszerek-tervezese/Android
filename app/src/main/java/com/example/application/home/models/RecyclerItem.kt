package com.example.application.home.models

data class RecyclerItem(
    var articleId : Int = 0 ,
    var title: String = "",
    var rating: String = "",
    var description: String = "",
    var content: String = "",
    var date: String = "",
    var author: String = "",
    var comments: Long = 0
)