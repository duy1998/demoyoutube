package com.example.demoyoutubeapi.data

import com.google.gson.annotations.SerializedName

data class Statistics (
    @SerializedName("viewCount") val viewCount : Int,
    @SerializedName("likeCount") val likeCount : Int,
    @SerializedName("dislikeCount") val dislikeCount : Int,
    @SerializedName("favoriteCount") val favoriteCount : Int,
    @SerializedName("commentCount") val commentCount : Int
)