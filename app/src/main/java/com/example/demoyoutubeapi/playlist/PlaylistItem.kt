package com.example.demoyoutubeapi.playlist

import com.google.gson.annotations.SerializedName

class PlaylistItem(@SerializedName("snippet") val snippet: Snippet) {

    class ResourceId(@SerializedName("kind") val kind: String,
                     @SerializedName("videoId") val videoId: String)

    class Snippet(id: String, resId: ResourceId) {

        @SerializedName("playlistId")
        val playlistId: String = id
        @SerializedName("resourceId")
        val resourceId: ResourceId = resId
    }
}