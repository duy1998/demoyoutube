package com.example.demoyoutubeapi.data

import com.google.gson.annotations.SerializedName

data class InfoItem (
    @SerializedName("kind") val kind : String,
    @SerializedName("etag") val etag : String,
    @SerializedName("id") val id : String,
    @SerializedName("statistics") val statistics : Statistics,
    @SerializedName("snippet") val snippet : Snippet
)