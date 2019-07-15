package com.example.demoyoutubeapi.data

import com.google.gson.annotations.SerializedName

data class InfoResponse (

    @SerializedName("kind") val kind : String,
    @SerializedName("etag") val etag : String,
    @SerializedName("pageInfo") val pageInfo : PageInfo,
    @SerializedName("items") val items : List<InfoItem>
)