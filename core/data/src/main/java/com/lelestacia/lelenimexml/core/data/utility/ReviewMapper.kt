package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.model.review.Review
import com.lelestacia.lelenimexml.core.network.model.GenericReviewResponse

fun GenericReviewResponse.asReview(): Review =
    Review(
        malID = malID,
        type = type,
        reactions = Review.ReviewReaction(
            overall = reactions.overall,
            niceReaction = reactions.niceReaction,
            loveReaction = reactions.loveReaction,
            funnyReaction = reactions.funnyReaction,
            confusingReaction = reactions.confusingReaction,
            informativeReaction = reactions.informativeReaction,
            wellWrittenReaction = reactions.wellWrittenReaction,
            creativeReaction = reactions.creativeReaction
        ),
        date = date,
        review = review,
        score = score,
        tags = tags,
        isSpoiler = isSpoiler,
        isPreliminary = isPreliminary,
        user = Review.UserReview(
            userName = user.userName,
            images = user.images.webp.imageURL
        )
    )