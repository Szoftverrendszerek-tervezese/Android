package com.example.application.home.models

class CommentItem(
    var commentId: Int,
    var commentText: String,
    var ownerId: Int,
    var timestamp: String,
    var username: String?
) {
}