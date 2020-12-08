package com.example.application.home.models

class Activities(
    var acticityId: Int,
    var activity: String,
    var starterUser: UserAct,
    var targetArticle: ArticleAct
)

class UserAct(
    var userId: String,
    var username: String
)

class ArticleAct(
    var articleId: String,
    var articletitle: String
)
