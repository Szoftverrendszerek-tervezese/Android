package com.example.application.home.models

class CommentItem(
    var commentId: Int,
    var ownerId: Int,
    var userName: String?,
    var commentText: String,
    var timeStamp: String
) {
}