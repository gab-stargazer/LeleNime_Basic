package com.lelestacia.lelenimexml.core.model.review

data class Review(
    val malID: Int,
    val type: String,
    val reactions: ReviewReaction,
    val date: String,
    val review: String,
    val score: Int,
    val tags: List<String>,
    val isSpoiler: Boolean,
    val isPreliminary: Boolean,
    val user: UserReview
) {

    data class UserReview(
        val userName: String,
        val images: String
    )

    data class ReviewReaction(
        val overall: Int,
        val niceReaction: Int,
        val loveReaction: Int,
        val funnyReaction: Int,
        val confusingReaction: Int,
        val informativeReaction: Int,
        val wellWrittenReaction: Int,
        val creativeReaction: Int
    )
}
