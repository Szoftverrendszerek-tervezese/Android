package com.example.application.home

data class RecyclerItem(
    var title: String = "",
    var rating: String = "",
    var description: String = "",
    var content: String = "",
    var date: String = "",
    var author: String = "",
    var comments: Long = 0
)